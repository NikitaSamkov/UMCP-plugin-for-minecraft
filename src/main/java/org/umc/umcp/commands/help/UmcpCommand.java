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

    public List<String> GetSubcommands() {
        return subcommands.stream()
                .map(UmcpCommand::toString)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return command;
    }
}
