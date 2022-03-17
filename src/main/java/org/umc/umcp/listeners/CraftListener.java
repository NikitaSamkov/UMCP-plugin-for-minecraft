package org.umc.umcp.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.umc.umcp.Crafter;
import org.umc.umcp.Main;

import org.bukkit.Color;
import org.umc.umcp.enums.InstitutesNames;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

public class CraftListener implements Listener {
    @EventHandler
    public void onCraftItem(PrepareItemCraftEvent e) {
        if (e.getRecipe() == null) {
            return;
        }
        Player player = (Player) e.getViewers().get(0);
        String recipeKey = (e.getRecipe() instanceof ShapedRecipe) ? ((ShapedRecipe) e.getRecipe()).getKey().getKey() :
                ((ShapelessRecipe) e.getRecipe()).getKey().getKey();

        if (Arrays.asList("vape", "socks", "longsocks", "catears").contains(recipeKey)) {
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

        if (Arrays.asList("sporthelmet", "sportchestplate", "sportleggings", "sportboots").contains(recipeKey)) {
            String institute = Main.conn.GetInstitute(player.getUniqueId().toString());
            if (!institute.equals(InstitutesNames.IFKSIMP.name)) {
                e.getInventory().setResult(new ItemStack(Material.AIR));
            }
        }
    }
}
