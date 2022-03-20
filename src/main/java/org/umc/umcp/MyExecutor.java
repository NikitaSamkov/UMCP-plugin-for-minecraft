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
        String test = Main.config.getConfigurationSection("test.subTest").getString("finalTest", "aboba");
        sender.sendMessage((test == null) ? "увы, null" : test);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            result.add("Test2");
            result.add("Test3");
        }
        result.add(args.length + ": " + String.join(", ", args));

        return result;
    }
}
