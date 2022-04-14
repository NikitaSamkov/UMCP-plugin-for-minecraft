package org.umc.umcp.misc;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.umc.umcp.Main;
import org.umc.umcp.enums.UmcpItem;

import java.util.Arrays;

public class Crafter {
    public static ShapedRecipe VapeRecipe;
    public static ShapedRecipe SocksRecipe;
    public static ShapedRecipe LongsocksRecipe;
    public static ShapedRecipe CatEarsRecipe;
    public static ShapedRecipe AdrenalineRecipe;
    public static ShapedRecipe BurnRecipe;
    public static ShapedRecipe MonsterRecipe;
    public static ShapedRecipe RedbullRecipe;

    public static ShapelessRecipe SportHelmetRecipe;
    public static ShapelessRecipe SportChestplateRecipe;
    public static ShapelessRecipe SportLeggingsRecipe;
    public static ShapelessRecipe SportBootsRecipe;
    public static ShapelessRecipe BombRecipe;
    public static ShapelessRecipe BeerRecipe;
    public static ShapelessRecipe PorterRecipe;
    public static ShapelessRecipe RedAleRecipe;

    public static void CreateCrafts(Plugin plugin) {
        //<editor-fold desc="Создание бутылки воды" defaultstate="collapsed">
        ItemStack waterbottle = new ItemStack(Material.POTION, 1);
        PotionMeta waterbottleMeta = (PotionMeta) waterbottle.getItemMeta();
        waterbottleMeta.setBasePotionData(new PotionData(PotionType.WATER));
        waterbottle.setItemMeta(waterbottleMeta);
        //</editor-fold>

        //<editor-fold desc="Вейп" defaultstate="collapsed">
        ItemStack vape = CreateItem(UmcpItem.VAPE, 1);
        PotionMeta vapeMeta = (PotionMeta) vape.getItemMeta();
        vapeMeta.setColor(Color.fromRGB(255, 255, 255));
        vape.setItemMeta(vapeMeta);

        VapeRecipe = new ShapedRecipe(new NamespacedKey(plugin, "vape"), vape);
        VapeRecipe.shape(" s ", " w ", " c ");
        VapeRecipe.setIngredient('s', Material.SUGAR);
        VapeRecipe.setIngredient('w', Material.POTION);
        VapeRecipe.setIngredient('c', new RecipeChoice.MaterialChoice(Arrays.asList(Material.COAL, Material.CHARCOAL)));
        //</editor-fold>
        //<editor-fold desc="Чулки - нижняя часть" defaultstate="collapsed">
        SocksRecipe = new ShapedRecipe(new NamespacedKey(plugin, "socks"), CreateItem(UmcpItem.SOCKS, 1));
        SocksRecipe.shape("   ", "w w", "w w");
        SocksRecipe.setIngredient('w', Material.WHITE_WOOL);
        //</editor-fold>
        //<editor-fold desc="Чулки - верхняя часть" defaultstate="collapsed">
        LongsocksRecipe = new ShapedRecipe(new NamespacedKey(plugin, "longsocks"), CreateItem(UmcpItem.LONGSOCKS, 1));
        LongsocksRecipe.shape("w w", "w w", "w w");
        LongsocksRecipe.setIngredient('w', Material.WHITE_WOOL);
        //</editor-fold>
        //<editor-fold desc="Кошачьи ушки" defaultstate="collapsed">
        CatEarsRecipe = new ShapedRecipe(new NamespacedKey(plugin, "catears"), CreateItem(UmcpItem.CAT_EARS, 1));
        CatEarsRecipe.shape("   ", "fsf", "sis");
        CatEarsRecipe.setIngredient('f', Material.FEATHER);
        CatEarsRecipe.setIngredient('s', Material.STICK);
        CatEarsRecipe.setIngredient('i', Material.IRON_INGOT);
        //</editor-fold>
        //<editor-fold desc="Адреналин" defaultstate="collapsed">
        AdrenalineRecipe = new ShapedRecipe(new NamespacedKey(plugin, "adrenaline"), CreateItem(UmcpItem.ADRENALINE, 1));
        AdrenalineRecipe.shape(" l ", " w ", " g ");
        AdrenalineRecipe.setIngredient('l', Material.LAPIS_LAZULI);
        AdrenalineRecipe.setIngredient('w', new RecipeChoice.ExactChoice(waterbottle));
        AdrenalineRecipe.setIngredient('g', Material.GOLD_INGOT);
        //</editor-fold>
        //<editor-fold desc="Бёрн" defaultstate="collapsed">
        BurnRecipe = new ShapedRecipe(new NamespacedKey(plugin, "burn"), CreateItem(UmcpItem.BURN, 1));
        BurnRecipe.shape(" p ", " w ", " i ");
        BurnRecipe.setIngredient('p', Material.BLAZE_POWDER);
        BurnRecipe.setIngredient('w', new RecipeChoice.ExactChoice(waterbottle));
        BurnRecipe.setIngredient('i', Material.IRON_INGOT);
        //</editor-fold>
        //<editor-fold desc="Монстр" defaultstate="collapsed">
        MonsterRecipe = new ShapedRecipe(new NamespacedKey(plugin, "monster"), CreateItem(UmcpItem.MONSTER, 1));
        MonsterRecipe.shape(" r ", " w ", " i ");
        MonsterRecipe.setIngredient('r', Material.REDSTONE);
        MonsterRecipe.setIngredient('w', new RecipeChoice.ExactChoice(waterbottle));
        MonsterRecipe.setIngredient('i', Material.IRON_INGOT);
        //</editor-fold>
        //<editor-fold desc="Рэдбулл" defaultstate="collapsed">
        RedbullRecipe = new ShapedRecipe(new NamespacedKey(plugin, "redbull"), CreateItem(UmcpItem.REDBULL, 1));
        RedbullRecipe.shape(" l ", " w ", " d ");
        RedbullRecipe.setIngredient('l', Material.LAPIS_LAZULI);
        RedbullRecipe.setIngredient('w', Material.WATER_BUCKET);
        RedbullRecipe.setIngredient('d', Material.DIAMOND);
        //</editor-fold>

        //<editor-fold desc="Козырёк 'Абибас'" defaultstate="collapsed">
        SportHelmetRecipe = new ShapelessRecipe(new NamespacedKey(plugin, "sporthelmet"), CreateItem(UmcpItem.SPORT_HELMET, 1));
        SportHelmetRecipe.addIngredient(Material.IRON_HELMET);
        SportHelmetRecipe.addIngredient(Material.BLACK_DYE);
        SportHelmetRecipe.addIngredient(Material.IRON_SWORD);
        //</editor-fold>
        //<editor-fold desc="Куртка 'Адик даст'" defaultstate="collapsed">
        SportChestplateRecipe = new ShapelessRecipe(new NamespacedKey(plugin, "sportchestplate"), CreateItem(UmcpItem.SPORT_CHESTPLATE, 1));
        SportChestplateRecipe.addIngredient(Material.IRON_CHESTPLATE);
        SportChestplateRecipe.addIngredient(Material.BLACK_DYE);
        SportChestplateRecipe.addIngredient(Material.OXEYE_DAISY);
        //</editor-fold>
        //<editor-fold desc="Спортивные штаны 'Бабидас'" defaultstate="collapsed">
        SportLeggingsRecipe = new ShapelessRecipe(new NamespacedKey(plugin, "sportleggings"), CreateItem(UmcpItem.SPORT_LEGGINGS, 1));
        SportLeggingsRecipe.addIngredient(Material.IRON_LEGGINGS);
        SportLeggingsRecipe.addIngredient(Material.BLACK_DYE);
        SportLeggingsRecipe.addIngredient(Material.LEVER);
        //</editor-fold>
        //<editor-fold desc="Фирменные кроссовки 'Адибас'" defaultstate="collapsed">
        SportBootsRecipe = new ShapelessRecipe(new NamespacedKey(plugin, "sportboots"), CreateItem(UmcpItem.SPORT_BOOTS, 1));
        SportBootsRecipe.addIngredient(Material.IRON_BOOTS);
        SportBootsRecipe.addIngredient(Material.BLACK_DYE);
        SportBootsRecipe.addIngredient(Material.STRING);
        SportBootsRecipe.addIngredient(Material.STRING);
        //</editor-fold>
        //<editor-fold desc="Банка с говном" defaultstate="collapsed">
        ItemStack dirtyBomb = CreateItem(UmcpItem.DIRTY_BOMB, 1);
        PotionMeta dirtyBombMeta = (PotionMeta) dirtyBomb.getItemMeta();
        dirtyBombMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        dirtyBombMeta.setColor(Color.fromRGB(
                Main.config.getInt("uralenin.params.bomb.Color.R"),
                Main.config.getInt("uralenin.params.bomb.Color.G"),
                Main.config.getInt("uralenin.params.bomb.Color.B")));
        dirtyBomb.setItemMeta(dirtyBombMeta);
        BombRecipe = new ShapelessRecipe(new NamespacedKey(plugin, "bomb"), dirtyBomb);
        BombRecipe.addIngredient(Material.GLASS_BOTTLE);
        BombRecipe.addIngredient(Material.BROWN_DYE);
        BombRecipe.addIngredient(Material.BROWN_DYE);
        BombRecipe.addIngredient(Material.BROWN_DYE);
        //</editor-fold>
        //<editor-fold desc="Обычное пшеничное пиво" defaultstate="collapsed">
        BeerRecipe = new ShapelessRecipe(new NamespacedKey(plugin, "beer"), CreateItem(UmcpItem.BEER, 1));
        BeerRecipe.addIngredient(new RecipeChoice.ExactChoice(waterbottle));
        BeerRecipe.addIngredient(Material.WHEAT_SEEDS);
        BeerRecipe.addIngredient(Material.BONE_MEAL);
        //</editor-fold>
        //<editor-fold desc="Портер" defaultstate="collapsed">
        PorterRecipe = new ShapelessRecipe(new NamespacedKey(plugin, "porter"), CreateItem(UmcpItem.PORTER, 1));
        PorterRecipe.addIngredient(new RecipeChoice.ExactChoice(waterbottle));
        PorterRecipe.addIngredient(Material.WHEAT_SEEDS);
        PorterRecipe.addIngredient(Material.BONE_MEAL);
        PorterRecipe.addIngredient(new RecipeChoice.MaterialChoice(Arrays.asList(Material.COAL, Material.CHARCOAL)));
        //</editor-fold>
        //<editor-fold desc="Красный Эль" defaultstate="collapsed">
        RedAleRecipe = new ShapelessRecipe(new NamespacedKey(plugin, "redale"), CreateItem(UmcpItem.RED_ALE, 1));
        RedAleRecipe.addIngredient(new RecipeChoice.ExactChoice(waterbottle));
        RedAleRecipe.addIngredient(Material.WHEAT_SEEDS);
        RedAleRecipe.addIngredient(Material.BONE_MEAL);
        RedAleRecipe.addIngredient(Material.REDSTONE);
        //</editor-fold>
    }

    public static @NotNull ItemStack CreateItem(@NotNull UmcpItem umcpItem, int amount) {
        ItemStack item = new ItemStack(umcpItem.getMaterial(), amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(umcpItem.getDisplayName());
        meta.setLore(umcpItem.getLore());
        meta.setCustomModelData(umcpItem.getCustomModelData());
        item.setItemMeta(meta);
        return item;
    }
}

