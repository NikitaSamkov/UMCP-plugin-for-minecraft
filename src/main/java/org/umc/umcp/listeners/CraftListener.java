package org.umc.umcp.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.umc.umcp.Crafter;
import org.umc.umcp.Main;

import org.bukkit.Color;

import java.util.Random;

public class CraftListener implements Listener {
    @EventHandler
    public void onCraftItem(PrepareItemCraftEvent e) {
        if (e.getRecipe() == null) {
            return;
        }

        //end of shapless recipies
        if (!(e.getRecipe() instanceof ShapedRecipe)) {
            return;
        }
        Player player = (Player) e.getViewers().get(0);
        ShapedRecipe recipe = (ShapedRecipe) e.getRecipe();
        if (recipe.getKey().getKey().equals("vape")) {
            String institute = Main.conn.GetInstitute(player.getUniqueId().toString());
            if (!institute.equals("ИРИТ-РТФ")) {
                e.getInventory().setResult(new ItemStack(Material.AIR));
            } else {
                ItemStack vape = Crafter.Vape;
                PotionMeta vapeMeta = (PotionMeta) vape.getItemMeta();
                Random r = new Random();
                vapeMeta.setColor(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
                vape.setItemMeta(vapeMeta);
                e.getInventory().setResult(vape);
            }
        }
    }
}
