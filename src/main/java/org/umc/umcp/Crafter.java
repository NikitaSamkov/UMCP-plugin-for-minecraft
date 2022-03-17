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
import org.umc.umcp.enums.UmcpItem;

public class Crafter {
    public static ItemStack Vape;
    public static ShapedRecipe VapeRecipe;

    public static ItemStack Socks;
    public static ShapedRecipe SocksRecipe;
    public static ItemStack Longsocks;
    public static ShapedRecipe LongsocksRecipe;

    public static void CreateCrafts(Plugin plugin) {
        CreateItems();
        //<editor-fold desc="Вейп">
        VapeRecipe = new ShapedRecipe(new NamespacedKey(plugin, "vape"), Vape);
        VapeRecipe.shape(" s ", " w ", " c ");
        VapeRecipe.setIngredient('s', Material.SUGAR);
        VapeRecipe.setIngredient('w', Material.POTION);
        VapeRecipe.setIngredient('c', Material.COAL);
        //</editor-fold>
        //<editor-fold desc="Чулки - нижняя часть">
        SocksRecipe = new ShapedRecipe(new NamespacedKey(plugin, "socks"), Socks);
        SocksRecipe.shape("   ", "w w", "w w");
        SocksRecipe.setIngredient('w', Material.WHITE_WOOL);
        //</editor-fold>
        //<editor-fold desc="Чулки - верхняя часть">
        LongsocksRecipe = new ShapedRecipe(new NamespacedKey(plugin, "longsocks"), Longsocks);
        LongsocksRecipe.shape("w w", "w w", "w w");
        LongsocksRecipe.setIngredient('w', Material.WHITE_WOOL);
        //</editor-fold>
    }

    public static void CreateItems() {
        //<editor-fold desc="Вейп">
        Vape = new ItemStack(UmcpItem.VAPE.getMaterial(), 1);
        PotionMeta vapeMeta = (PotionMeta) Vape.getItemMeta();
        vapeMeta.setDisplayName(UmcpItem.VAPE.getDisplayName());
        vapeMeta.setLore(UmcpItem.VAPE.getLore());
        vapeMeta.setCustomModelData(UmcpItem.VAPE.getCustomModelData());
        vapeMeta.setColor(Color.fromRGB(255, 255, 255));
        vapeMeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 0, false, false, true), true);
        Vape.setItemMeta(vapeMeta);
        //</editor-fold>
        //<editor-fold desc="Чулки - нижняя часть">
        Socks = new ItemStack(UmcpItem.SOCKS.getMaterial(), 1);
        ItemMeta socksMeta = Socks.getItemMeta();
        socksMeta.setDisplayName(UmcpItem.SOCKS.getDisplayName());
        socksMeta.setLore(UmcpItem.SOCKS.getLore());
        socksMeta.setCustomModelData(UmcpItem.SOCKS.getCustomModelData());
        Socks.setItemMeta(socksMeta);
        //</editor-fold>
        //<editor-fold desc="Чулки - верхняя часть">
        Longsocks = new ItemStack(UmcpItem.LONGSOCKS.getMaterial(), 1);
        ItemMeta longSocksMeta = Longsocks.getItemMeta();
        longSocksMeta.setDisplayName(UmcpItem.LONGSOCKS.getDisplayName());
        longSocksMeta.setLore(UmcpItem.LONGSOCKS.getLore());
        longSocksMeta.setCustomModelData(UmcpItem.LONGSOCKS.getCustomModelData());
        Longsocks.setItemMeta(longSocksMeta);
        //</editor-fold>
    }
}

