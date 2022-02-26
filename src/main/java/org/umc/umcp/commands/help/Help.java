package org.umc.umcp.commands.help;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.units.qual.A;
import org.umc.umcp.commands.UmcpCommand;

import java.util.*;
import java.util.stream.Collectors;

public class Help {
    private UmcpCommand commandsTree;

    public Help(UmcpCommand commandsTree) {
        this.commandsTree = commandsTree;
    }

    public Boolean GetHelp(CommandSender sender, Command command, String label, String[] args) {
        List<String> fullComm = new LinkedList<>(Arrays.asList("/" + label));

        int i = 0;
        UmcpCommand last = commandsTree;
        while (!args[i].toLowerCase(Locale.ROOT).equals("help")) {
            UmcpCommand sub = last.GetSubcommand(args[i]);
            if (sub == null) {
                return false;
            }
            last = sub;
            fullComm.add(args[i]);
            i++;
        }
        if (i < args.length - 1) {
            return false;
        }

        String start = String.join(" ", fullComm);

        List<String> variants = new ArrayList<>();
        for(UmcpCommand sub: last.subcommands) {
            variants.add(String.format("%s §a%s§f %s", start, sub.toString(), (sub.subcommands.size() > 0) ? "..." : ""));
        }
        for (String arg: last.GetArguments()) {
            variants.add(String.format("%s §e%s§f", start, arg));
        }
        sender.sendMessage(String.format("Возможные дополнения:\n%s\n%s",
                variants.stream().sorted().collect(Collectors.joining("\n")), last.description));
        return true;
    }
}
