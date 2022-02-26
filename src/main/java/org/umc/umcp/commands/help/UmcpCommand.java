package org.umc.umcp.commands.help;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class UmcpCommand {
    public final String command;
    public final List<UmcpCommand> subcommands;
    public final String description;

    public UmcpCommand(String command, String description, List<UmcpCommand> subcommands) {
        this.command = command;
        this.description = description;
        this.subcommands = (subcommands == null) ? new ArrayList<>() : subcommands;
    }

    public UmcpCommand GetSubcommand(String name) {
        for (UmcpCommand sub: subcommands) {
            if (sub.command.equalsIgnoreCase(name))
                return sub;
        }
        return null;
    }

    public UmcpCommand GetSubcommand(List<String> path) {
        UmcpCommand current = this;
        for (String e: path) {
            UmcpCommand next = current.GetSubcommand(e);
            if (next == null) {
                return null;
            }
            current = next;
        }
        return current;
    }

    public List<String> GetSubcommands() {
        return subcommands.stream()
                .map(UmcpCommand::toString)
                .collect(Collectors.toList());
    }

    public List<String> GetSubcommands(List<String> path) {
        UmcpCommand sub = GetSubcommand(path);
        if (sub != null)
            return sub.GetSubcommands();
        return null;
    }

    @Override
    public String toString() {
        return command;
    }
}
