package org.umc.umcp;

import com.earth2me.essentials.Essentials;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.umc.umcp.enums.UmcpItem;
import org.umc.umcp.misc.Crafter;

import java.util.ArrayList;
import java.util.List;


public class MyExecutor implements TabExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (strings.length != 0 && sender instanceof Player) {
            Player player = (Player) sender;
            if (strings[0].equals("beer")) {
                player.getInventory().setItemInMainHand(Crafter.CreateItem(UmcpItem.INMIT_BEER, 1));
            }
            if (strings[0].equals("leviathan")) {
                ItemStack leviathan = Crafter.CreateItem(UmcpItem.LEVIATHAN, 1);
                ItemMeta meta = leviathan.getItemMeta();
                meta.addEnchant(Enchantment.DIG_SPEED, 5, true);
                meta.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
                meta.addEnchant(Enchantment.DURABILITY, 3, true);
                leviathan.setItemMeta(meta);
                player.getInventory().setItemInMainHand(leviathan);
            }
            if (strings[0].equals("money")) {
                Economy economy = Main.getEconomy();
                economy.depositPlayer(player, 2000);
                player.sendMessage("got $2000!");
            }
            if (strings[0].equals("time")) {
                player.sendMessage(String.format("you are %d sec on this server!", player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            result.add("beer");
            result.add("leviathan");
            result.add("money");
            result.add("time");
        }

        return result;
    }
}
