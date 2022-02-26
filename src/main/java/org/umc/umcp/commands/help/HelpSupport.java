package org.umc.umcp.commands.help;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.umc.umcp.commands.UmcpCommand;

import java.util.List;

public abstract class HelpSupport implements TabExecutor {
    private UmcpCommand commandTree;
    private Help helper;

    @Override
    public abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args);

    @Override
    public abstract List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args);

    protected abstract UmcpCommand GetTree();
}
