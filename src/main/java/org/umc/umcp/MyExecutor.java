package org.umc.umcp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;


public class MyExecutor implements TabExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        sender.sendMessage("Test");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> result = new ArrayList<>();
            result.add("Test2");
            result.add("Test3");
            result.add("Test4");
            return result;
        }

        return null;
    }
}
