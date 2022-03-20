package org.umc.umcp.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.umc.umcp.Main;
import org.umc.umcp.commands.help.Help;
import org.umc.umcp.commands.help.HelpSupport;
import org.umc.umcp.enums.InstitutesNames;

import net.md_5.bungee.api.chat.TextComponent;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class IenimTabExecutor extends HelpSupport {
    private UmcpCommand commandTree;
    private Help helper;

    //config
    private ConfigurationSection messages;

    public IenimTabExecutor() {
        commandTree = GetTree();
        helper = new Help(commandTree);
        messages = Main.config.getConfigurationSection("ienim.messages");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String institute = Main.conn.GetInstitute(((Player) sender).getUniqueId().toString());
        if (!institute.equals(InstitutesNames.IENIM.name)) {
            sender.sendMessage(messages.getString("NotIenim"));
            return true;
        }
        if (args.length < 1) {
            return false;
        }
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

    @Override
    protected UmcpCommand GetTree() {
        ConfigurationSection commands = Main.config.getConfigurationSection("ienim.commands");
        UmcpCommand tree = new UmcpCommand("ienim", this::NoCommand, commands.getString("BaseDesc"), new LinkedList<>(Arrays.asList(
                new UmcpCommand(commands.getString("Upgrade"), this::Plus, commands.getString("UpgradeDesc"))
        )));
        return tree;
    }

    private Boolean Plus(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            commandSender.sendMessage(messages.getString("HandsEmpty"));
            return true;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        if (!meta.hasEnchants()) {
            commandSender.sendMessage(messages.getString("NoMaxEnchants"));
            return true;
        }

        Map<Enchantment, Integer> enchants = meta.getEnchants();
        List<Enchantment> upgrade = new ArrayList<>();
        for (Enchantment ench: enchants.keySet()) {
            if (enchants.get(ench) > ench.getMaxLevel()) {
                commandSender.sendMessage(String.format(messages.getString("HasOverloaded"), ench.getKey().getKey()));
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
            commandSender.sendMessage(messages.getString("NoMaxEnchants"));
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
            player.sendMessage(String.format(messages.getString("NotMaxLvl"), arg));
            return true;
        }
        int power = Main.config.getInt("ienim.params.UpgradePower");
        meta.addEnchant(ench, ench.getMaxLevel() + power, true);
        player.getInventory().getItemInMainHand().setItemMeta(meta);
        player.sendMessage(String.format(messages.getString("UpgradeSuccess"), arg, ench.getMaxLevel() + power));

        return true;
    }

    private @NotNull TextComponent GetClickableCommand(String text, String command) {
        TextComponent result = new TextComponent(text);
        result.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return result;
    }

    private @NotNull TextComponent GetClickableCommand(@NotNull Enchantment ench) {
        return GetClickableCommand(String.format(messages.getString("PlusEnch"), ench.getKey().getKey().toUpperCase(Locale.ROOT)),
                String.format("/ienim %s %s", Main.config.getString("ienim.commands.Upgrade"), ench.getKey().getKey()));
    }

    private @NotNull TextComponent GetClickableEnchants(@NotNull List<Enchantment> enchants) {
        TextComponent result = new TextComponent(messages.getString("PlusSeparator"));
        result.addExtra(messages.getString("PlusMessage"));
        for (Enchantment ench: enchants) {
            TextComponent extra = GetClickableCommand(ench);
            extra.setColor(ChatColor.AQUA);
            result.addExtra(extra);
            result.addExtra("\n\n");
        }
        result.addExtra(messages.getString("PlusSeparator"));
        return result;
    }

    private boolean NoCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}
