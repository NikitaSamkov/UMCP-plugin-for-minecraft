package org.umc.umcp.enums;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public enum UmcpItem {
    VAPE("Вейп", Arrays.asList("Одноразовый вейп радиста.", "Слишком частое употребление вредно для здоровья!"), Material.POTION, 1337);

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
