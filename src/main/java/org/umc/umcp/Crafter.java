package org.umc.umcp;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Crafter {
    public static ItemStack Vape;
    public static ShapedRecipe VapeRecipe;

    public static void CreateCrafts(Plugin plugin) {
        CreateItems();
        //Вейп
        VapeRecipe = new ShapedRecipe(new NamespacedKey(plugin, "vape"), Vape);
        VapeRecipe.shape(" s ", " w ", " c ");
        VapeRecipe.setIngredient('s', Material.SUGAR);
        VapeRecipe.setIngredient('w', Material.POTION);
        VapeRecipe.setIngredient('c', Material.COAL);
    }

    public static void CreateItems() {
        //Вейп
        Vape = new ItemStack(Material.POTION, 1);
        PotionMeta vapeMeta = (PotionMeta) Vape.getItemMeta();
        vapeMeta.setDisplayName("Вейп");
        List<String> lore = new ArrayList<>();
        lore.add("Одноразовый вейп радиста.");
        lore.add("Слишком частое употребление вредно для здоровья!");
        vapeMeta.setLore(lore);
        vapeMeta.setCustomModelData(1337);
        vapeMeta.setColor(Color.fromRGB(255, 255, 255));
        vapeMeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 0, false, false, true), true);
        Vape.setItemMeta(vapeMeta);
    }
}
