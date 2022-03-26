package org.umc.umcp.enums;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.umc.umcp.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum UmcpItem {
    VAPE("rtf", "vape", Material.POTION),
    SOCKS("rtf", "socks", Material.GOLDEN_BOOTS),
    LONGSOCKS("rtf", "longsocks", Material.GOLDEN_LEGGINGS),
    CAT_EARS("rtf", "catears", Material.LEATHER_HELMET),

    SPORT_HELMET("ifksimp", "sporthelmet", Material.DIAMOND_HELMET),
    SPORT_CHESTPLATE("ifksimp", "sportchestplate", Material.DIAMOND_CHESTPLATE),
    SPORT_LEGGINGS("ifksimp", "sportleggings", Material.DIAMOND_LEGGINGS),
    SPORT_BOOTS("ifksimp", "sportboots", Material.DIAMOND_BOOTS),

    DIRTY_BOMB("uralenin", "bomb", Material.LINGERING_POTION),
    ADRENALINE("uralenin", "adrenaline", Material.POTION),
    BURN("uralenin", "burn", Material.POTION),
    MONSTER("uralenin", "monster", Material.POTION),
    REDBULL("uralenin", "redbull", Material.POTION);

    private final String displayName;
    private final List<String> lore;
    private final Material material;
    private final int customModelData;


    private UmcpItem(String displayName, List<String> lore, Material material, int customModelData) {
        this.displayName = displayName;
        this.lore = lore;
        this.material = material;
        this.customModelData = customModelData;
    }

    private UmcpItem(String namePath, String lorePath, Material material, String cmdPath) {
        this(Main.config.getString(namePath), Main.config.getStringList(lorePath), material, Main.config.getInt(cmdPath));
    }

    private UmcpItem(String institutePath, String itemPath, Material material) {
        this(String.format("%s.items.%s.Name", institutePath, itemPath),
                String.format("%s.items.%s.Desc", institutePath, itemPath),
                material,
                String.format("%s.items.%s.ModelData", institutePath, itemPath));
    }

    public static Boolean check(@NotNull ItemStack itemStack, @NotNull UmcpItem umcpItem) {
        return (itemStack.getType().equals(umcpItem.getMaterial()) && itemStack.getItemMeta().hasCustomModelData() &&
                itemStack.getItemMeta().getCustomModelData() == umcpItem.getCustomModelData());
    }

    public Material getMaterial() {
        return material;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public List<String> getLore() {
        return lore;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Boolean check(@NotNull ItemStack itemStack) {
        return UmcpItem.check(itemStack, this);
    }
}
