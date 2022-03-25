package org.umc.umcp.armorset;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.umc.umcp.enums.InstituteNames;
import org.umc.umcp.enums.UmcpItem;

import java.util.List;

public class UmcpArmorSet {
    private final UmcpItem helmet;
    private final UmcpItem chestplate;
    private final UmcpItem leggings;
    private final UmcpItem boots;
    private final List<PotionEffect> effects;
    private final Boolean strict;
    private final InstituteNames institute;

    public UmcpArmorSet(UmcpItem helmet, UmcpItem chestplate, UmcpItem leggings, UmcpItem boots, List<PotionEffect> effects, Boolean strictSet) {
        this(helmet, chestplate, leggings, boots, effects, strictSet, null);
    }

    public UmcpArmorSet(UmcpItem helmet, UmcpItem chestplate, UmcpItem leggings, UmcpItem boots, List<PotionEffect> effects, Boolean strictSet, InstituteNames institute) {
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.effects = effects;
        this.strict = strictSet;
        this.institute = institute;
    }

    public Boolean CheckSet(@NotNull Player player) {
        PlayerInventory pInv = player.getInventory();
        return CheckSetPart(helmet, pInv.getHelmet()) &&
                CheckSetPart(chestplate, pInv.getChestplate()) &&
                CheckSetPart(leggings, pInv.getLeggings()) &&
                CheckSetPart(boots, pInv.getBoots());
    }

    private Boolean CheckSetPart(UmcpItem part, ItemStack playerItem) {
        if (playerItem == null) {
            return part == null;
        }
        if (part == null) {
            return !strict;
        }
        return part.check(playerItem);
    }

    public List<PotionEffect> GetEffects() {
        return effects;
    }

    public InstituteNames getInstitute() {
        return institute;
    }
}
