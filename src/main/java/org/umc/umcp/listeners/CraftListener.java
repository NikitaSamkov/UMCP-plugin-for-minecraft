package org.umc.umcp.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.umc.umcp.Main;
import org.umc.umcp.connection.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CraftListener implements Listener {
    @EventHandler
    public void onCraftItem(PrepareItemCraftEvent e) {
        Player player = (Player) e.getViewers().get(0);
        String institute = Main.conn.GetInstitute(player.getUniqueId().toString());
        if (e.getRecipe().getResult().equals(new ItemStack(Material.DIAMOND)) && !player.hasPermission("umcp.rtf")) {
            e.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }
}
