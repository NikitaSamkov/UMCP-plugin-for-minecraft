package org.umc.umcp.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.umc.umcp.misc.Crafter;
import org.umc.umcp.Main;

import org.bukkit.Color;
import org.umc.umcp.enums.InstituteNames;

import java.lang.reflect.Array;
import java.util.*;

public class CraftListener implements Listener {
    private Map<InstituteNames, List<String>> keys = new HashMap<>();
    private Map<PotionType, Integer[]> potions = new HashMap<>();

    public CraftListener() {
        keys.put(InstituteNames.IFKSIMP, Arrays.asList("sporthelmet", "sportchestplate", "sportleggings", "sportboots"));
        keys.put(InstituteNames.URALENIN, Arrays.asList("bomb", "adrenaline", "burn", "monster", "redbull"));
        keys.put(InstituteNames.HTI, Arrays.asList("beer", "porter", "redale"));
        keys.put(InstituteNames.UGI, Arrays.asList("ugi_book"));
    }

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

        if (recipeKey.equals("betterpotion")) {
            ItemStack origPotion = e.getInventory().getMatrix()[4];
            if (origPotion.hasItemMeta()) {
                PotionMeta origMeta = (PotionMeta) origPotion.getItemMeta();
                if (origMeta.hasCustomModelData()) {
                    e.getInventory().setResult(new ItemStack(Material.AIR));
                    return;
                }
                ItemStack newPotion = e.getInventory().getResult();
                PotionMeta newMeta = (PotionMeta) newPotion.getItemMeta();

                if (origMeta.hasCustomEffects()) {
                    List<PotionEffect> effects = origMeta.getCustomEffects();
                    PotionEffect upgraded = effects.get(0);
                    newMeta.addCustomEffect(new PotionEffect(upgraded.getType(),
                            upgraded.getDuration(),
                            upgraded.getAmplifier() + 1,
                            false,
                            false), true);
                    for (int i = 1; i < effects.size(); i += 1) {
                        newMeta.addCustomEffect(effects.get(i), false);
                    }
                } else {
                    PotionData origData = origMeta.getBasePotionData();
                    if (origData.getType().getEffectType() == null) {
                        return;
                    }
                    PotionType potionType = origData.getType();
                    int lvl = ((origData.isUpgraded()) ?
                            (potionType.equals(PotionType.SLOWNESS)) ? 3 : 1
                            : 0) + 1;
                    int duration =
                            (potionType.equals(PotionType.SPEED) ||
                                    potionType.equals(PotionType.JUMP) ||
                                    potionType.equals(PotionType.STRENGTH) ||
                                    potionType.equals(PotionType.FIRE_RESISTANCE) ||
                                    potionType.equals(PotionType.WATER_BREATHING) ||
                                    potionType.equals(PotionType.NIGHT_VISION) ||
                                    potionType.equals(PotionType.INVISIBILITY)) ? 3600 :

                                    (potionType.equals(PotionType.SLOWNESS) ||
                                            potionType.equals(PotionType.SLOW_FALLING)) ? 1800 :

                                            (potionType.equals(PotionType.POISON) ||
                                                    potionType.equals(PotionType.REGEN)) ? 900 :

                                                    (potionType.equals(PotionType.TURTLE_MASTER)) ? 400 : 0;

                    if (origData.isExtended()) {
                        duration = (duration == 3600) ? 9600 :
                                (duration == 1800) ? 4800 : duration * 2;
                    }

                    if (origData.isUpgraded()) {
                        duration = (duration == 3600) ? 1800 :
                                (duration == 1800) ? 400 :
                                        (potionType.equals(PotionType.POISON)) ? 420 :
                                                (potionType.equals(PotionType.REGEN)) ? 440 : duration;
                    }

                    newMeta.addCustomEffect(new PotionEffect(origData.getType().getEffectType(),
                            duration,
                            lvl,
                            true,
                            true), true);
                }

                newPotion.setItemMeta(newMeta);
                e.getInventory().setResult(newPotion);
            }
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
