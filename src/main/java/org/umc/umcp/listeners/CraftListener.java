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
import org.umc.umcp.enums.InstitutesNames;

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
        String recipeKey = ((ShapedRecipe) e.getRecipe()).getKey().getKey();
        if (recipeKey.equals("vape") || recipeKey.equals("socks") || recipeKey.equals("longsocks") || recipeKey.equals("catears")) {
            String institute = Main.conn.GetInstitute(player.getUniqueId().toString());
            if (!institute.equals(InstitutesNames.RTF.name)) {
                e.getInventory().setResult(new ItemStack(Material.AIR));
            } else {
                if (recipeKey.equals("vape")) {
                    ItemStack vape = Crafter.VapeRecipe.getResult();
                    PotionMeta vapeMeta = (PotionMeta) vape.getItemMeta();
                    Random r = new Random();
                    vapeMeta.setColor(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
                    vape.setItemMeta(vapeMeta);
                    e.getInventory().setResult(vape);
                }
            }
        }
    }
}
