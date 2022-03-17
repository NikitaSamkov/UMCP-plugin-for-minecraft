package org.umc.umcp;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.umc.umcp.enums.UmcpItem;

public class Crafter {
    public static ShapedRecipe VapeRecipe;
    public static ShapedRecipe SocksRecipe;
    public static ShapedRecipe LongsocksRecipe;
    public static ShapedRecipe CatEarsRecipe;

    public static void CreateCrafts(Plugin plugin) {
        //<editor-fold desc="Вейп">
        ItemStack vape = CreateItem(UmcpItem.VAPE, 1);
        PotionMeta vapeMeta = (PotionMeta) vape.getItemMeta();
        vapeMeta.setColor(Color.fromRGB(255, 255, 255));
        vapeMeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 0, false, false, true), true);
        vape.setItemMeta(vapeMeta);

        VapeRecipe = new ShapedRecipe(new NamespacedKey(plugin, "vape"), vape);
        VapeRecipe.shape(" s ", " w ", " c ");
        VapeRecipe.setIngredient('s', Material.SUGAR);
        VapeRecipe.setIngredient('w', Material.POTION);
        VapeRecipe.setIngredient('c', Material.COAL);
        //</editor-fold>
        //<editor-fold desc="Чулки - нижняя часть">
        SocksRecipe = new ShapedRecipe(new NamespacedKey(plugin, "socks"), CreateItem(UmcpItem.SOCKS, 1));
        SocksRecipe.shape("   ", "w w", "w w");
        SocksRecipe.setIngredient('w', Material.WHITE_WOOL);
        //</editor-fold>
        //<editor-fold desc="Чулки - верхняя часть">
        LongsocksRecipe = new ShapedRecipe(new NamespacedKey(plugin, "longsocks"), CreateItem(UmcpItem.LONGSOCKS, 1));
        LongsocksRecipe.shape("w w", "w w", "w w");
        LongsocksRecipe.setIngredient('w', Material.WHITE_WOOL);
        //</editor-fold>
        //<editor-fold desc="Кошачьи ушки">
        CatEarsRecipe = new ShapedRecipe(new NamespacedKey(plugin, "catears"), CreateItem(UmcpItem.CAT_EARS, 1));
        CatEarsRecipe.shape("   ", "fsf", "sis");
        CatEarsRecipe.setIngredient('f', Material.FEATHER);
        CatEarsRecipe.setIngredient('s', Material.STICK);
        CatEarsRecipe.setIngredient('i', Material.IRON_INGOT);
        //</editor-fold>
    }

    static @NotNull ItemStack CreateItem(@NotNull UmcpItem umcpItem, int amount) {
        ItemStack item = new ItemStack(umcpItem.getMaterial(), amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(umcpItem.getDisplayName());
        meta.setLore(umcpItem.getLore());
        meta.setCustomModelData(umcpItem.getCustomModelData());
        item.setItemMeta(meta);
        return item;
    }
}

