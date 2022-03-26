package org.umc.umcp.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
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
    private Help helper;
    private Boolean haveCooldown;
    private ConfigurationSection messages;

    public InstituteTabExecutor() {
        conn = Main.conn;
        institutes = conn.GetInstitutes();
        commandTree = GetTree();
        painter = Painter.GetPainter(new ArrayList<>(institutes.keySet()));
        helper = new Help(commandTree);
        haveCooldown = true;
        messages = Main.config.getConfigurationSection("urfu.messages");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1)
            return false;
        if (args[args.length - 1].equalsIgnoreCase("help"))
            return helper.GetHelp(sender, command, label, args);
        UmcpCommand curr = commandTree;
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
        List<String> path = new LinkedList<>(Arrays.asList(args));
        if (path.size() > 0)
            path.remove(path.size() - 1);

        UmcpCommand comm = commandTree.GetSubcommand(path);

        if (comm == null)
            return new ArrayList<>();

        List<String> subs = comm.GetSubcommands();
        subs.addAll(comm.GetArguments());
        subs.add("help");

        return subs.stream().filter((String sub)->sub.startsWith(args[args.length - 1])).collect(Collectors.toList());
    }

    protected UmcpCommand GetTree() {
        List<String> institutesList = institutes.keySet().stream().sorted().collect(Collectors.toList());
        ConfigurationSection source = Main.config.getConfigurationSection("urfu.commands");
        UmcpCommand tree = new UmcpCommand("urfu", this::NoCommand,
                source.getString("BaseDesc"), new LinkedList<>(Arrays.asList(
                new UmcpCommand(source.getString("join.Name"), this::Join, source.getString("join.Desc"), null, institutesList),
                new UmcpCommand(source.getString("info.Name"), this::Info, source.getString("info.Desc"), null, institutesList, true),
                new UmcpCommand(source.getString("list.Name"), this::InstitutesList, source.getString("list.Desc")),
                new UmcpCommand("cooldown", this::SwitchCooldown, "", null, new LinkedList<>(Arrays.asList("ON", "OFF")))
        )));
        tree.GetSubcommand(source.getString("info.Name")).arguments.add("me");
        return tree;
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

    private boolean Join(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0)
            return false;
        if (!(sender instanceof Player)) return false;

        ConfigurationSection jm = messages.getConfigurationSection("join");

        Player player = (Player) sender;
        if (haveCooldown && !Cooldowns.CanUse(player.getUniqueId(), CooldownType.INSTITUTE_JOIN)) {
            sender.sendMessage(jm.getString("Cooldown"));
            return true;
        }
        String lastInstitute = conn.GetInstitute(player.getUniqueId().toString());
        String instituteName = args[0];
        if (lastInstitute.equals(instituteName)) {
            sender.sendMessage(String.format(jm.getString("AlreadyInTarget"), painter.get(instituteName)));
            return true;
        }
        if (JoinInstitute(player.getUniqueId().toString(), instituteName)) {
            sender.sendMessage(String.format(jm.getString("JoinSuccess"), painter.get(instituteName)));
            if (lastInstitute.equals(InstituteNames.IFKSIMP.name)) {
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
            }
            if (instituteName.equals(InstituteNames.IFKSIMP.name)) {
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(24);
            }
            if (lastInstitute.equals(InstituteNames.IENIM.name)) {
                DowngradeOverloadedItems(player);
            }
            SetMaster.RemoveAllSets(player);
            SetMaster.CheckSets(player);
            Cooldowns.Update(player.getUniqueId(), CooldownType.INSTITUTE_JOIN);
            return true;
        } else {
            sender.sendMessage(jm.getString("JoinError"));
            return false;
        }
    }

    private boolean NoCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    private boolean SwitchCooldown(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (Objects.equals(args[0], "ON")) {
            haveCooldown = true;
            return true;
        }
        if (Objects.equals(args[0], "OFF")) {
            haveCooldown = false;
            return true;
        }
        return false;
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

    private boolean InfoMe(Player player) {
        String institute = conn.GetInstitute(player.getUniqueId().toString());
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
        TextComponent msg = new TextComponent(messages.getString("info.PlayerFirst"));

        TextComponent targetName = new TextComponent(target.getName());
        targetName.setColor(ChatColor.AQUA);

        msg.addExtra(targetName);
        msg.addExtra(messages.getString("info.PlayerMid"));
        msg.addExtra(GetInteractiveInstitute(conn.GetInstitute(target.getUniqueId().toString())));
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

    private boolean JoinInstitute(String uuid, String instituteName) {
        try {
            conn.Connect();
            ResultSet instituteID = conn.MakeQuery(String.format("select id from institutes where name='%s'", instituteName));
            if (!instituteID.next()) {
                return false;
            }
            int iid = instituteID.getInt("id");
            instituteID.close();
            ResultSet players = conn.MakeQuery(
                    String.format("select uuid, name from players inner join institutes i on i.id = institute where uuid='%s'", uuid));
            SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = fdate.format(new Date());
            if (players.next()) {
                conn.MakeUpdate(String.format("update players set institute=%d, last_change='%s' where uuid='%s'", iid, date, uuid));
            } else {
                conn.MakeUpdate(String.format("insert into players values ('%s', %d, '%s')", uuid, iid, date));
            }
            conn.Close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.Close();
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
}


