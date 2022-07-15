package org.umc.umcp.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.umc.umcp.misc.Cooldowns;
import org.umc.umcp.Main;
import org.umc.umcp.armorset.SetMaster;
import org.umc.umcp.commands.help.Help;
import org.umc.umcp.commands.help.HelpSupport;
import org.umc.umcp.connection.DBConnection;

import net.md_5.bungee.api.chat.TextComponent;
import org.umc.umcp.enums.CooldownType;
import org.umc.umcp.enums.InstituteNames;

public class InstituteTabExecutor extends HelpSupport {

    private final DBConnection conn;
    private final Map<String, Map<String, String>> institutes;
    private final Map<String, String> painter;
    private UmcpCommand commandTree;
    private UmcpCommand ienimTree;
    private Help helper;
    private Help ienimHelper;
    private ConfigurationSection messages;
    private ConfigurationSection ienimMessages;

    public InstituteTabExecutor() {
        conn = Main.conn;
        institutes = conn.GetInstitutes();
        commandTree = GetTree();
        ienimTree = GetIenimTree();
        painter = Painter.GetPainter(new ArrayList<>(institutes.keySet()));
        helper = new Help(commandTree);
        ienimHelper = new Help(ienimTree);
        messages = Main.config.getConfigurationSection("urfu.messages");
        ienimMessages = Main.config.getConfigurationSection("ienim.messages");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1)
            return false;
        boolean ienim = sender.hasPermission(
                String.format("group.%s", institutes.get(InstituteNames.IENIM.name).get("permission")));
        if (args[args.length - 1].equalsIgnoreCase("help"))
            return ienim ? ienimHelper.GetHelp(sender, command, label, args) : helper.GetHelp(sender, command, label, args);
        UmcpCommand curr = ienim ?
                ienimTree :
                commandTree;
        for (int i = 0; i < args.length; i++) {
            UmcpCommand next = curr.GetSubcommand(args[i]);
            if (next == null) {
                return curr.Execute(sender, command, label, Arrays.copyOfRange(args, i, args.length));
            }
            curr = next;
        }
        return curr.Execute(sender, command, label, new String[0]);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        boolean ienim = sender.hasPermission(
                String.format("group.%s", institutes.get(InstituteNames.IENIM.name).get("permission")));
        boolean director = sender.hasPermission("group.director");
        List<String> path = new LinkedList<>(Arrays.asList(args));
        if (path.size() > 0)
            path.remove(path.size() - 1);

        UmcpCommand comm = ienim ? ienimTree.GetSubcommand(path) : commandTree.GetSubcommand(path);

        if (comm == null)
            return new ArrayList<>();

        List<String> subs = comm.GetSubcommands();
        subs.addAll(comm.GetArguments());
        subs.add("help");
        List<String> result = subs.stream().filter((String sub)->sub.startsWith(args[args.length - 1])).collect(Collectors.toList());
        if (args.length == 1 && result.contains("otchislen") && !director) {
            result.remove("otchislen");
        }
        return result;
    }

    protected UmcpCommand GetTree() {
        List<String> institutesList = institutes.keySet().stream().sorted().collect(Collectors.toList());
        ConfigurationSection source = Main.config.getConfigurationSection("urfu.commands");
        UmcpCommand tree = new UmcpCommand("urfu", this::NoCommand,
                source.getString("BaseDesc"), new LinkedList<>(Arrays.asList(
                new UmcpCommand(source.getString("join.Name"), this::Join, source.getString("join.Desc"), null, institutesList),
                new UmcpCommand(source.getString("info.Name"), this::Info, source.getString("info.Desc"), null, institutesList, true),
                new UmcpCommand(source.getString("list.Name"), this::InstitutesList, source.getString("list.Desc")),
                new UmcpCommand(source.getString("leave.Name"), this::Leave, source.getString("leave.Desc")),
                new UmcpCommand(source.getString("otchislen.Name"), this::Otchislen, source.getString("otchislen.Desc"), null, null, true)
        )));
        tree.GetSubcommand(source.getString("info.Name")).arguments.add("me");
        return tree;
    }

    private UmcpCommand GetIenimTree() {
        ConfigurationSection commands = Main.config.getConfigurationSection("ienim.commands");
        UmcpCommand base = GetTree();
        UmcpCommand ienim = new UmcpCommand(base.name, base.func, base.description, base.subcommands);
        ienim.AddSubcommand(new UmcpCommand("ienim", this::NoCommand, commands.getString("BaseDesc"), new LinkedList<>(Arrays.asList(
                new UmcpCommand(commands.getString("Upgrade"), this::IenimPlus, commands.getString("UpgradeDesc"))
        ))));
        return ienim;
    }

    private boolean NoCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    private boolean Leave(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        String institute = Main.conn.GetInstitute(player);
        if (institute == null) {
            player.sendMessage(messages.getString("leave.NoInstitute"));
            return true;
        }
        Main.removePermission(player.getUniqueId(), String.format("group.%s", institutes.get(institute).get("permission")));
        player.sendMessage(messages.getString("leave.Success"));

        CheckInstitutePerms(player, institute, null);

        SetMaster.RemoveAllSets(player);
        SetMaster.CheckSets(player);
        return true;
    }

    private boolean Otchislen(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        boolean director = player.hasPermission("group.director");
        if (!director) {
            return false;
        }
        ConfigurationSection om = messages.getConfigurationSection("otchislen");
        boolean rector = player.hasPermission(String.format("group.%s", "rector"));
        if (!Cooldowns.CanUse(player.getUniqueId(), CooldownType.OTCHISLEN)) {
            player.sendMessage(om.getString("Cooldown"));
            return true;
        }

        String institute = Main.conn.GetInstitute(player);
        if (!rector && institute == null) {
            player.sendMessage(om.getString("DirectorNoInstitute"));
            return true;
        }

        List<Player> targets = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            String pinst = Main.conn.GetInstitute(p);
            if (p.hasPermission("group.aspirant") || pinst == null || (!rector && !institute.equals(pinst))) {
                continue;
            }
            targets.add(p);
        }

        if (targets.size() == 0) {
            player.sendMessage(om.getString("NoTargets"));
            return true;
        }

        List<Player> victims = new ArrayList<>();

        if (rector && targets.size() < 4) {
            victims = targets;
        } else {
            int victimCount = rector ? 3 : 1;
            Random random = new Random();
            for (int i = 0; i < victimCount; i++) {
                boolean success = false;
                while (!success) {
                    Player potVictim = targets.get(random.nextInt(targets.size()));
                    if (victims.contains(potVictim)) {
                        continue;
                    }
                    victims.add(potVictim);
                    success = true;
                }
            }
        }

        for (Player victim : victims) {
            String inst = Main.conn.GetInstitute(victim);
            Main.removePermission(victim.getUniqueId(), String.format("group.%s", institutes.get(inst).get("permission")));
            victim.sendMessage(om.getString("YouOtchislenFirst") + player.getDisplayName() + om.getString("YouOtchislenLast"));
            CheckInstitutePerms(victim, institute, null);
            SetMaster.RemoveAllSets(victim);
            SetMaster.CheckSets(victim);
        }

        Cooldowns.Update(player.getUniqueId(), CooldownType.OTCHISLEN);
        return true;
    }

    private boolean Join(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0)
            return false;
        if (!(sender instanceof Player)) return false;

        ConfigurationSection jm = messages.getConfigurationSection("join");

        Player player = (Player) sender;
        if (!player.hasPermission("umcp.nocooldown") && !Cooldowns.CanUse(player.getUniqueId(), CooldownType.INSTITUTE_JOIN)) {
            sender.sendMessage(jm.getString("Cooldown"));
            return true;
        }
        String lastInstitute = conn.GetInstitute(player);
        String instituteName = args[0];
        if (lastInstitute != null && lastInstitute.equals(instituteName)) {
            sender.sendMessage(String.format(jm.getString("AlreadyInTarget"), painter.get(instituteName)));
            return true;
        }
        if (!player.hasPermission("umcp.nojoinpay") && Main.getEconomy().getBalance(player) < 5000) {
            sender.sendMessage(jm.getString("NotEnoughMoney"));
            return true;
        }
        if (JoinInstitute(player, instituteName)) {
            sender.sendMessage(String.format(jm.getString("JoinSuccess"), painter.get(instituteName)));

            if (!player.hasPermission("umcp.nojoinpay")) {
                Main.getEconomy().withdrawPlayer(player, 5000);
            }
            player.setStatistic(Statistic.PLAY_ONE_MINUTE, 0);

            CheckInstitutePerms(player, lastInstitute, instituteName);

            SetMaster.RemoveAllSets(player);
            SetMaster.CheckSets(player);
            Cooldowns.Update(player.getUniqueId(), CooldownType.INSTITUTE_JOIN);
            return true;
        } else {
            sender.sendMessage(jm.getString("JoinError"));
            return false;
        }
    }

    private boolean Info(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0)
            return false;

        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[0]);

        if (args[0].equalsIgnoreCase("me") || Objects.equals(target, player)) {
            return InfoMe(player);
        }

        if (target != null) {
            return InfoPlayer(player, target);
        }
        return InfoInstitute(player, args[0]);
    }

    private boolean InstitutesList(CommandSender sender, Command command, String label, String[] args) {
        List<String> institutes = new ArrayList<>(painter.keySet());
        TextComponent msg = new TextComponent();
        for (String institute: institutes) {
            msg.addExtra(GetInteractiveInstitute(institute));
            msg.addExtra("\n");
        }
        msg.addExtra(messages.getString("list.ClickHelp"));
        TextComponent cmd = new TextComponent(String.format("/urfu %s %s",
                Main.config.getString("urfu.commands.info.Name"), messages.getString("list.InstituteNameHelp")));
        cmd.setColor(ChatColor.GREEN);
        cmd.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                String.format("/urfu %s ", Main.config.getString("urfu.commands.info.Name"))));
        msg.addExtra(cmd);
        sender.spigot().sendMessage(msg);
        return true;
    }

    private boolean InfoMe(Player player) {
        String institute = conn.GetInstitute(player);
        TextComponent msg;
        if (institute != null) {
            msg = new TextComponent(messages.getString("info.MeFirst"));
            msg.addExtra(GetInteractiveInstitute(institute));
            msg.addExtra(messages.getString("info.MeLast"));
        } else {
            msg = new TextComponent(messages.getString("info.MeNoneFirst"));
            TextComponent extra = new TextComponent(String.format("/urfu %s %s",
                    Main.config.getString("urfu.commands.join.Name"), messages.getString("info.MeNoneCommandArg")));
            extra.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/urfu %s ",
                    Main.config.getString("urfu.commands.join.Name"))));
            extra.setColor(ChatColor.GREEN);
            msg.addExtra(extra);
            msg.addExtra(messages.getString("info.MeNoneLast"));
        }
        player.spigot().sendMessage(msg);
        return true;
    }

    private boolean InfoPlayer(Player player, Player target) {
        String targetInstitute = conn.GetInstitute(target);
        if (targetInstitute == null) {
            player.sendMessage("Данный игрок только абитуриент!");
            return true;
        }

        TextComponent msg = new TextComponent(messages.getString("info.PlayerFirst"));

        TextComponent targetName = new TextComponent(target.getName());
        targetName.setColor(ChatColor.AQUA);

        msg.addExtra(targetName);
        msg.addExtra(messages.getString("info.PlayerMid"));
        msg.addExtra(GetInteractiveInstitute(conn.GetInstitute(target)));
        msg.addExtra(messages.getString("info.PlayerLast"));
        player.spigot().sendMessage(msg);
        return true;

    }

    private boolean InfoInstitute(Player player, String instituteName) {
        if (!painter.containsKey(instituteName) || !institutes.containsKey(instituteName))
            return false;
        player.sendMessage(String.format(messages.getString("info.InstituteDelimer") + "\n" +
                "%s\n" +
                "%s\n" +
                messages.getString("info.InstituteDelimer"),
                painter.get(instituteName), institutes.get(instituteName).get("description")));
        return true;
    }

    private TextComponent GetInteractiveInstitute(String name) {
        TextComponent institute = new TextComponent(painter.get(name));
        institute.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(institutes.get(name).get("description")).create()));
        institute.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/urfu %s %s",
                Main.config.getString("urfu.commands.info.Name"), name)));
        return institute;
    }

    private boolean JoinInstitute(Player player, String instituteName) {
        if (!institutes.containsKey(instituteName)) {
            return false;
        }
        try {
            String newPerm = institutes.get(instituteName).get("permission");
            String currInst = conn.GetInstitute(player);

            if (currInst != null) {
                String lastPerm = institutes.get(currInst).get("permission");
                Main.removePermission(player.getUniqueId(), String.format("group.%s", lastPerm));
            }
            Main.addPermission(player.getUniqueId(), String.format("group.%s", newPerm));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void DowngradeOverloadedItems(Player player) {
        for (ItemStack item: player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasEnchants()) {
                ItemMeta meta = item.getItemMeta();
                Map<Enchantment, Integer> enchants = meta.getEnchants();
                for (Enchantment ench: enchants.keySet()) {
                    if (enchants.get(ench) > ench.getMaxLevel()) {
                        meta.removeEnchant(ench);
                        meta.addEnchant(ench, ench.getMaxLevel(), false);
                        item.setItemMeta(meta);
                    }
                }
            }
        }
    }

    private void CheckInstitutePerms(Player player, String lastInstitute, String newInstitute) {
        if (newInstitute != null) {
            if (newInstitute.equals(InstituteNames.INFO.name)) {
                player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE,
                        Main.config.getInt("info.params.FireResistAmplifier"), false, false));
            }
            if (newInstitute.equals(InstituteNames.IFKSIMP.name)) {
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(24);
            }
            if (newInstitute.equals(InstituteNames.FTI.name)) {
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE,
                        Main.config.getInt("fti.params.StrengthAmplifier"), false, false));
            }
        }
        if (lastInstitute != null) {
            if (lastInstitute.equals(InstituteNames.IFKSIMP.name)) {
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
            }
            if (lastInstitute.equals(InstituteNames.IENIM.name)) {
                DowngradeOverloadedItems(player);
            }
            if (lastInstitute.equals(InstituteNames.INFO.name)) {
                player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            }
            if (lastInstitute.equals(InstituteNames.FTI.name)) {
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            }
        }
    }

    private Boolean IenimPlus(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            commandSender.sendMessage(ienimMessages.getString("HandsEmpty"));
            return true;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        if (!meta.hasEnchants()) {
            commandSender.sendMessage(ienimMessages.getString("NoMaxEnchants"));
            return true;
        }

        Map<Enchantment, Integer> enchants = meta.getEnchants();
        List<Enchantment> upgrade = new ArrayList<>();
        for (Enchantment ench: enchants.keySet()) {
            if (enchants.get(ench) > ench.getMaxLevel()) {
                commandSender.sendMessage(String.format(ienimMessages.getString("HasOverloaded"), ench.getKey().getKey()));
                return true;
            }
            if (enchants.get(ench) == ench.getMaxLevel()) {
                upgrade.add(ench);
            }
        }

        if (strings.length > 0) {
            return AddEnchantment(player, strings[0]);
        }

        if (upgrade.size() == 0) {
            commandSender.sendMessage(ienimMessages.getString("NoMaxEnchants"));
            return true;
        }

        commandSender.spigot().sendMessage(GetClickableEnchants(upgrade));

        return true;
    }

    private Boolean AddEnchantment(Player player, String arg) {
        Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(arg));

        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();

        Map<Enchantment, Integer> enchants = meta.getEnchants();

        if (enchants.get(ench) != ench.getMaxLevel()) {
            player.sendMessage(String.format(ienimMessages.getString("NotMaxLvl"), arg));
            return true;
        }
        int power = Main.config.getInt("ienim.params.UpgradePower");
        meta.addEnchant(ench, ench.getMaxLevel() + power, true);
        player.getInventory().getItemInMainHand().setItemMeta(meta);
        player.sendMessage(String.format(ienimMessages.getString("UpgradeSuccess"), arg, ench.getMaxLevel() + power));

        return true;
    }

    private @NotNull TextComponent GetClickableCommand(String text, String command) {
        TextComponent result = new TextComponent(text);
        result.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return result;
    }

    private @NotNull TextComponent GetClickableCommand(@NotNull Enchantment ench) {
        return GetClickableCommand(String.format(ienimMessages.getString("PlusEnch"), ench.getKey().getKey().toUpperCase(Locale.ROOT)),
                String.format("/urfu ienim %s %s", Main.config.getString("ienim.commands.Upgrade"), ench.getKey().getKey()));
    }

    private @NotNull TextComponent GetClickableEnchants(@NotNull List<Enchantment> enchants) {
        TextComponent result = new TextComponent(ienimMessages.getString("PlusSeparator"));
        result.addExtra(ienimMessages.getString("PlusMessage"));
        for (Enchantment ench: enchants) {
            TextComponent extra = GetClickableCommand(ench);
            extra.setColor(ChatColor.AQUA);
            result.addExtra(extra);
            result.addExtra("\n\n");
        }
        result.addExtra(ienimMessages.getString("PlusSeparator"));
        return result;
    }
}


