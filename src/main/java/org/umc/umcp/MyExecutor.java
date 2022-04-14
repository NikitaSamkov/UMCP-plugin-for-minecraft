package org.umc.umcp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.umc.umcp.enums.UmcpItem;
import org.umc.umcp.misc.Crafter;

import java.util.ArrayList;
import java.util.List;


public class MyExecutor implements TabExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (strings.length != 0 && strings[0].equals("beer") && sender instanceof Player) {
            Player player = (Player) sender;
            player.getInventory().setItemInMainHand(Crafter.CreateItem(UmcpItem.INMIT_BEER, 1));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> result = new ArrayList<>();
        if (args.length == 0) {
            result.add("beer");
        }

        return result;
    }
}
