package org.umc.umcp.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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

    public IenimTabExecutor() {
        commandTree = GetTree();
        helper = new Help(commandTree);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String institute = Main.conn.GetInstitute(((Player) sender).getUniqueId().toString());
        if (!institute.equals(InstitutesNames.IENIM.name)) {
            sender.sendMessage("Чтобы использовать /ienim нужно вступить в ИЕНиМ!");
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
        UmcpCommand tree = new UmcpCommand("ienim", this::NoCommand, "Команды института ИЕНиМ.", new LinkedList<>(Arrays.asList(
                new UmcpCommand("plus", this::Plus, "Преодолеть максимальный уровень зачарования одной чары предмета в руке на +1")
        )));
        return tree;
    }

    private Boolean Plus(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            commandSender.sendMessage("Возьми зачарованный предмет в руки, чтобы повысить его зачаровние!");
            return true;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        if (!meta.hasEnchants()) {
            commandSender.sendMessage("У предмета должно быть хотя бы одно зачарование максимального уровня, чтобы преодолеть его!");
            return true;
        }

        Map<Enchantment, Integer> enchants = meta.getEnchants();
        List<Enchantment> upgrade = new ArrayList<>();
        for (Enchantment ench: enchants.keySet()) {
            if (enchants.get(ench) > ench.getMaxLevel()) {
                commandSender.sendMessage("У предмета уже есть зачарование с уровнем, который выше макимального: " + ench.getKey().getKey());
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
            commandSender.sendMessage("У предмета должно быть хотя бы одно зачарование максимального уровня, чтобы преодолеть его!");
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
            player.sendMessage("Зачарование " + arg + " не максимального уровня!");
            return true;
        }

        meta.addEnchant(ench, ench.getMaxLevel() + 1, true);
        player.getInventory().getItemInMainHand().setItemMeta(meta);
        player.sendMessage("Успешно зачаровано на " + arg + " " + (ench.getMaxLevel() + 1));

        return false;
    }

    private @NotNull TextComponent GetClickableCommand(String text, String command) {
        TextComponent result = new TextComponent(text);
        result.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return result;
    }

    private @NotNull TextComponent GetClickableCommand(@NotNull Enchantment ench) {
        return GetClickableCommand(String.format("[%s]", ench.getKey().getKey().toUpperCase(Locale.ROOT)),
                String.format("/ienim plus %s", ench.getKey().getKey()));
    }

    private @NotNull TextComponent GetClickableEnchants(@NotNull List<Enchantment> enchants) {
        TextComponent result = new TextComponent();
        for (Enchantment ench: enchants) {
            result.addExtra(GetClickableCommand(ench));
        }
        return result;
    }

    private boolean NoCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}
