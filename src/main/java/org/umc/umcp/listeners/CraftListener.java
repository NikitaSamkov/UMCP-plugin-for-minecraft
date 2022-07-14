package org.umc.umcp.listeners;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.umc.umcp.enums.UmcpItem;
import org.umc.umcp.misc.Crafter;
import org.umc.umcp.Main;

import org.bukkit.Color;
import org.umc.umcp.enums.InstituteNames;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

public class CraftListener implements Listener {
    private Map<InstituteNames, List<String>> keys = new HashMap<>();
    private Map<PotionType, Integer[]> potions = new HashMap<>();
    private Map<String, Function<ItemStack, ItemMeta>> upgrades = new HashMap<>();

    public CraftListener() {
        keys.put(InstituteNames.IFKSIMP, Arrays.asList("sporthelmet", "sportchestplate", "sportleggings", "sportboots"));
        keys.put(InstituteNames.URALENIN, Arrays.asList("bomb", "adrenaline", "burn", "monster", "redbull"));
        keys.put(InstituteNames.HTI, Arrays.asList("beer", "porter", "redale"));
        keys.put(InstituteNames.UGI, Arrays.asList("ugi_book"));
        keys.put(InstituteNames.FTI, Arrays.asList("thunderbow"));
        FillUpgrades();
    }

    private void FillUpgrades() {
        upgrades.put("durability_upgrade",
                item -> BuffEnchant(item, Enchantment.DURABILITY, 1));

        upgrades.put("damage_all_upgrade",
                item -> BuffEnchant(item, Enchantment.DAMAGE_ALL, 1));

        upgrades.put("dig_speed_upgrade",
                item -> BuffEnchant(item, Enchantment.DIG_SPEED, 1));

        upgrades.put("silk_touch_upgrade",
                item -> BuffEnchant(item, Enchantment.SILK_TOUCH, 1));

        upgrades.put("loot_bonus_blocks_upgrade",
                item -> BuffEnchant(item, Enchantment.LOOT_BONUS_BLOCKS, 1));

        upgrades.put("luck_upgrade",
                item -> BuffEnchant(item, Enchantment.LUCK, 1));

        upgrades.put("lure_upgrade",
                item -> BuffEnchant(item, Enchantment.LURE, 1));

        upgrades.put("mending_upgrade",
                item -> BuffEnchant(item, Enchantment.MENDING, 1));

        upgrades.put("protection_environmental_upgrade",
                item -> BuffEnchant(item, Enchantment.PROTECTION_ENVIRONMENTAL, 1));

        upgrades.put("protection_fall_upgrade",
                item -> BuffEnchant(item, Enchantment.PROTECTION_FALL, 1));

        upgrades.put("oxygen_upgrade",
                item -> BuffEnchant(item, Enchantment.OXYGEN, 1));

        upgrades.put("water_worker_upgrade",
                item -> BuffEnchant(item, Enchantment.WATER_WORKER, 1));

        upgrades.put("thorns_upgrade",
                item -> BuffEnchant(item, Enchantment.THORNS, 1));

        upgrades.put("depth_strider_upgrade",
                item -> BuffEnchant(item, Enchantment.DEPTH_STRIDER, 1));

        upgrades.put("frost_walker_upgrade",
                item -> BuffEnchant(item, Enchantment.FROST_WALKER, 1));

        upgrades.put("knockback_upgrade",
                item -> BuffEnchant(item, Enchantment.KNOCKBACK, 1));

        upgrades.put("fire_aspect_upgrade",
                item -> BuffEnchant(item, Enchantment.FIRE_ASPECT, 1));

        upgrades.put("loot_bonus_mobs_upgrade",
                item -> BuffEnchant(item, Enchantment.LOOT_BONUS_MOBS, 1));

        upgrades.put("sweeping_edge_upgrade",
                item -> BuffEnchant(item, Enchantment.SWEEPING_EDGE, 1));

        upgrades.put("arrow_damage_upgrade",
                item -> BuffEnchant(item, Enchantment.ARROW_DAMAGE, 1));

        upgrades.put("arrow_knockback_upgrade",
                item -> BuffEnchant(item, Enchantment.ARROW_KNOCKBACK, 1));

        upgrades.put("arrow_fire_upgrade",
                item -> BuffEnchant(item, Enchantment.ARROW_FIRE, 1));

        upgrades.put("arrow_infinite_upgrade",
                item -> BuffEnchant(item, Enchantment.ARROW_INFINITE, 1));

        upgrades.put("loyalty_upgrade",
                item -> BuffEnchant(item, Enchantment.LOYALTY, 1));

        upgrades.put("impaling_upgrade",
                item -> BuffEnchant(item, Enchantment.IMPALING, 1));

        upgrades.put("riptide_upgrade",
                item -> BuffEnchant(item, Enchantment.RIPTIDE, 1));

        upgrades.put("channeling_upgrade",
                item -> BuffEnchant(item, Enchantment.CHANNELING, 1));

        upgrades.put("multishot_upgrade",
                item -> BuffEnchant(item, Enchantment.MULTISHOT, 1));

        upgrades.put("quick_charge_upgrade",
                item -> BuffEnchant(item, Enchantment.QUICK_CHARGE, 1));

        upgrades.put("piercing_upgrade",
                item -> BuffEnchant(item, Enchantment.PIERCING, 1));

        upgrades.put("protection_fire_upgrade",
                item -> BuffEnchant(item, Enchantment.PROTECTION_FIRE, 1));

        upgrades.put("protection_explosions_upgrade",
                item -> BuffEnchant(item, Enchantment.PROTECTION_EXPLOSIONS, 1));

        upgrades.put("protection_projectile_upgrade",
                item -> BuffEnchant(item, Enchantment.PROTECTION_PROJECTILE, 1));

        upgrades.put("damage_undead_upgrade",
                item -> BuffEnchant(item, Enchantment.DAMAGE_UNDEAD, 1));

        upgrades.put("damage_arthropods_upgrade",
                item -> BuffEnchant(item, Enchantment.DAMAGE_ARTHROPODS, 1));
    }

    @EventHandler
    public void onCraftItem(PrepareItemCraftEvent e) {
        Player player = (Player) e.getViewers().get(0);
        if (e.getRecipe() == null) {
            return;
        }
        String recipeKey = (e.getRecipe() instanceof ShapedRecipe) ? ((ShapedRecipe) e.getRecipe()).getKey().getKey() :
                ((ShapelessRecipe) e.getRecipe()).getKey().getKey();
        ItemStack result = e.getRecipe().getResult();

        if (Arrays.asList("vape", "socks", "longsocks", "catears").contains(recipeKey)) {
            if (!player.hasPermission(String.format("group.%s", InstituteNames.RTF.permission))) {
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
                if (!player.hasPermission(String.format("group.%s", InstituteNames.HTI.permission))) {
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
            return;
        }

        if (CheckMaterialForSubstrings(result,
                Arrays.asList(
                        "AXE",
                        "SHOVEL",
                        "FISHING_ROD",
                        "HOE",
                        "SWORD",
                        "BOW",
                        "TRIDENT",
                        "HELMET",
                        "CHESTPLATE",
                        "LEGGINGS",
                        "BOOTS",
                        "SHEARS",
                        "SHIELD"))) {
            if (player.hasPermission(String.format("group.%s", InstituteNames.INMIT.permission))) {
                ItemMeta resultMeta = result.getItemMeta();
                assert resultMeta != null;
                resultMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                String material = result.getType().toString();
                if (CheckMaterialForSubstrings(result, Arrays.asList("AXE", "SHOVEL", "SHEARS"))) {
                    resultMeta.addEnchant(Enchantment.DIG_SPEED, 1, true);
                }
                else if (material.contains("FISHING_ROD")) {
                    resultMeta.addEnchant(Enchantment.LUCK, 1, true);
                    resultMeta.addEnchant(Enchantment.LURE, 1, true);
                }
                else if (material.contains("HOE")) {
                    resultMeta.addEnchant(Enchantment.DIG_SPEED, 5, true);
                }
                else if (material.contains("SWORD")) {
                    resultMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                    resultMeta.addEnchant(Enchantment.LOOT_BONUS_MOBS, 1, true);
                }
                else if (material.contains("CROSSBOW")) {
                    resultMeta.addEnchant(Enchantment.QUICK_CHARGE, 1, true);
                }
                else if (material.contains("BOW")) {
                    resultMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                }
                else if (CheckMaterialForSubstrings(result, Arrays.asList("HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS"))) {
                    resultMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                }
                result.setItemMeta(resultMeta);
                e.getInventory().setResult(result);
            }
        }

        if (recipeKey.contains("_upgrade")) {
            if (UmcpItem.THUNDERBOW.check(e.getInventory().getMatrix()[4])) {
                e.getInventory().setResult(new ItemStack(Material.AIR));
                return;
            }
            boolean inmit = player.hasPermission(String.format("group.%s", InstituteNames.INMIT.permission));
            boolean ugi = player.hasPermission(String.format("group.%s", InstituteNames.UGI.permission));
            if ((!inmit && !ugi) ||
                    (ugi &&
                            !e.getInventory().getMatrix()[4].getType().equals(Material.BOOK) &&
                            !e.getInventory().getMatrix()[4].getType().equals(Material.ENCHANTED_BOOK)) ||
                    (inmit &&
                            (e.getInventory().getMatrix()[4].getType().equals(Material.BOOK) ||
                                    e.getInventory().getMatrix()[4].getType().equals(Material.ENCHANTED_BOOK)))) {
                e.getInventory().setResult(new ItemStack(Material.AIR));
                return;
            }
            Material type = e.getInventory().getMatrix()[4].getType();
            ItemStack res = new ItemStack(type.equals(Material.BOOK) ? Material.ENCHANTED_BOOK : type, 1);
            ItemMeta upgraded = upgrades.get(recipeKey).apply(e.getInventory().getMatrix()[4]);
            if (upgraded == null) {
                e.getInventory().setResult(new ItemStack(Material.AIR));
                return;
            }
            res.setItemMeta(upgraded);
            e.getInventory().setResult(res);
            return;
        }

        CheckCrafts(recipeKey, player, e.getInventory());
    }

    private boolean CancelCraft(List<String> keys, String recipeKey, InstituteNames institute, Player player, CraftingInventory inv) {
        if (keys.contains(recipeKey)) {
            if (!player.hasPermission(String.format("group.%s", institute.permission))) {
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

    private boolean CheckMaterialForSubstrings(ItemStack item, List<String> subs) {
        String material = item.getType().toString();
        for (String sub: subs) {
            if (material.contains(sub)) {
                return true;
            }
        }
        return false;
    }

    private ItemMeta BuffEnchant(ItemStack item, Enchantment ench, int lvl) {
        int lvlplus = 0;
        if (!ench.canEnchantItem(item) && !(item.getType().equals(Material.BOOK) || item.getType().equals(Material.ENCHANTED_BOOK))) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta.hasEnchant(ench)) {
            lvlplus = meta.getEnchantLevel(ench);
        } else {
            for (Enchantment enchantment : item.getEnchantments().keySet()) {
                if (ench.conflictsWith(enchantment)) {
                    return null;
                }
            }
        }
        meta.addEnchant(ench, lvl + lvlplus, false);
        return meta;
    }
}
