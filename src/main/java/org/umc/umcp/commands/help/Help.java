package org.umc.umcp.commands.help;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Help {
    private UmcpCommand commandsTree;

    public Help(UmcpCommand commandsTree) {
        this.commandsTree = commandsTree;
    }

    public Boolean GetHelp(CommandSender sender, Command command, String label, String[] args) {
        StringBuilder start = new StringBuilder("/" + label + " ");

        int i = 0;
        UmcpCommand last = commandsTree;
        while (!args[i].toLowerCase(Locale.ROOT).equals("help")) {
            UmcpCommand sub = last.GetSubcommand(args[i]);
            if (sub == null) {
                return false;
            }
            last = sub;
            start.append(args[i]).append(" ");
            i++;
        }
        if (i < args.length - 1) {
            return false;
        }

        if (last.subcommands.size() == 0) {
            sender.sendMessage(String.format("%s\n%s", start, last.description));
            return true;
        }

        List<String> variants = new ArrayList<>();
        for(UmcpCommand sub: last.subcommands) {
            variants.add(String.format("%s §a%s§f %s", start, sub.toString(), (sub.subcommands.size() > 0) ? "..." : ""));
        }
        sender.sendMessage(String.format("Возможные дополнения:\n%s\n%s",
                variants.stream().sorted().collect(Collectors.joining("\n")), last.description));
        return true;
    }
}
