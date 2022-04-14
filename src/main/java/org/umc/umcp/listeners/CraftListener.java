package org.umc.umcp.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.umc.umcp.misc.Crafter;
import org.umc.umcp.Main;

import org.bukkit.Color;
import org.umc.umcp.enums.InstituteNames;

import java.lang.reflect.Array;
import java.util.*;

public class CraftListener implements Listener {
    private Map<InstituteNames, List<String>> keys = new HashMap<>();

    public CraftListener() {
        keys.put(InstituteNames.IFKSIMP, Arrays.asList("sporthelmet", "sportchestplate", "sportleggings", "sportboots"));
        keys.put(InstituteNames.URALENIN, Arrays.asList("bomb", "adrenaline", "burn", "monster", "redbull"));
        keys.put(InstituteNames.HTI, Arrays.asList("beer", "porter", "redale"));
    }

    @EventHandler
    public void onCraftItem(PrepareItemCraftEvent e) {
        if (e.getRecipe() == null) {
            return;
        }
        Player player = (Player) e.getViewers().get(0);
        String recipeKey = (e.getRecipe() instanceof ShapedRecipe) ? ((ShapedRecipe) e.getRecipe()).getKey().getKey() :
                ((ShapelessRecipe) e.getRecipe()).getKey().getKey();
        player.sendMessage(recipeKey);
        if (Arrays.asList("vape", "socks", "longsocks", "catears").contains(recipeKey)) {
            String institute = Main.conn.GetInstitute(player.getUniqueId().toString());
            if (!institute.equals(InstituteNames.RTF.name)) {
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
            return;
        }

        CheckCrafts(recipeKey, player, e.getInventory());
    }

    private boolean CancelCraft(List<String> keys, String recipeKey, InstituteNames institute, Player player, CraftingInventory inv) {
        if (keys.contains(recipeKey)) {
            String playerInstitute = Main.conn.GetInstitute(player.getUniqueId().toString());
            if (!playerInstitute.equals(institute.name)) {
                inv.setResult(new ItemStack(Material.AIR));
                return true;
            }
        }
        return false;
    }

    private void CheckCrafts(String key, Player player, CraftingInventory inv) {
        for (InstituteNames institute: keys.keySet()) {
            if (CancelCraft(keys.get(institute), key, institute, player, inv)) {
                return;
            }
        }
    }
}
