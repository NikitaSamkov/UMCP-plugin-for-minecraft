package org.umc.umcp.listeners;

import org.bukkit.Color;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.umc.umcp.Crafter;

public class GlobalListener implements Listener {
    @EventHandler
    public void onPotionMake(BrewEvent e) {
        for (int i = 0; i < 3; i++) {
            ItemStack item = e.getContents().getItem(i);
            if (item == null) {
                continue;
            }
            PotionMeta imeta = (PotionMeta) item.getItemMeta();
            if (imeta.hasCustomModelData() && imeta.getCustomModelData() == Crafter.Vape.getItemMeta().getCustomModelData()) {
                e.setCancelled(true);
                return;
            }
        }
    }
}
