package org.umc.umcp.enums;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.umc.umcp.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum UmcpItem {
    VAPE("rtf.items.vape.Name", "rtf.items.vape.Desc", Material.POTION, "rtf.items.vape.ModelData"),
    SOCKS("rtf.items.socks.Name", "rtf.items.socks.Desc", Material.GOLDEN_BOOTS, "rtf.items.socks.ModelData"),
    LONGSOCKS("rtf.items.longsocks.Name", "rtf.items.longsocks.Desc", Material.GOLDEN_LEGGINGS, "rtf.items.longsocks.ModelData"),
    CAT_EARS("rtf.items.catears.Name", "rtf.items.catears.Desc", Material.LEATHER_HELMET, "rtf.items.catears.ModelData"),

    SPORT_HELMET("ifksimp.items.sporthelmet.Name", "ifksimp.items.sporthelmet.Desc", Material.DIAMOND_HELMET, "ifksimp.items.sporthelmet.ModelData"),
    SPORT_CHESTPLATE("ifksimp.items.sportchestplate.Name", "ifksimp.items.sportchestplate.Desc", Material.DIAMOND_CHESTPLATE, "ifksimp.items.sportchestplate.ModelData"),
    SPORT_LEGGINGS("ifksimp.items.sportleggings.Name", "ifksimp.items.sportleggings.Desc", Material.DIAMOND_LEGGINGS, "ifksimp.items.sportleggings.ModelData"),
    SPORT_BOOTS("ifksimp.items.sportboots.Name", "ifksimp.items.sportboots.Desc", Material.DIAMOND_BOOTS, "ifksimp.items.sportboots.ModelData");

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

    public static Boolean check(@NotNull ItemStack itemStack, @NotNull UmcpItem umcpItem) {
        return (itemStack.getType().equals(umcpItem.getMaterial()) && itemStack.getItemMeta().hasCustomModelData() && itemStack.getItemMeta().getCustomModelData() == umcpItem.getCustomModelData());
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
