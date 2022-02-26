package org.umc.umcp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@FunctionalInterface
interface UmcpSubcommand{
    public Boolean execute(CommandSender cs, Command c, String s, String[] sa);
}