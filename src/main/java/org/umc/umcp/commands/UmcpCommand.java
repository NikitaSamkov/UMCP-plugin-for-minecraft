package org.umc.umcp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UmcpCommand {
    public final String name;
    public final List<UmcpCommand> subcommands;
    public final List<String> arguments;
    public final String description;
    public final UmcpSubcommand func;


    //region Constructor
    public UmcpCommand(String name, UmcpSubcommand func, String description, List<UmcpCommand> subcommands, List<String> arguments) {
        this.name = name;
        this.func = func;
        this.description = description;
        this.subcommands = (subcommands == null) ? new ArrayList<>() : subcommands;
        this.arguments = arguments;
    }

    public UmcpCommand(String name, UmcpSubcommand func, String description, List<UmcpCommand> subcommands) {
        this(name, func, description, subcommands, new ArrayList<>());
    }

    public UmcpCommand(String name, UmcpSubcommand func, String description) {
        this(name, func, description, new ArrayList<>(), new ArrayList<>());
    }

    public UmcpCommand(String name, UmcpSubcommand func, List<UmcpCommand> subcommands) {
        this(name, func, "", subcommands, new ArrayList<>());
    }
    //endregion

    public UmcpCommand GetSubcommand(String name) {
        for (UmcpCommand sub: subcommands) {
            if (sub.name.equalsIgnoreCase(name))
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

    public List<String> GetArguments() {
        return arguments;
    }

    public  List<String> GetArguments(List<String> path) {
        UmcpCommand sub = GetSubcommand(path);
        if (sub == null)
            return null;
        return sub.arguments;
    }

    public void AddSubcommand(UmcpCommand subcommand) {
        this.subcommands.add(subcommand);
    }

    public void AddArgument(String argument) {
        this.arguments.add(argument);
    }

    public boolean Execute(CommandSender sender, Command command, String label, String[] args) {
        return func.execute(sender, command, label, args);
    }

    @Override
    public String toString() {
        return name;
    }
}
