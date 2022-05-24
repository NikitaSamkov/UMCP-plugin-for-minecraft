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
    public static ShapedRecipe BetterPotionRecipe;
    public static ShapedRecipe ThunderBowRecipe;

    public static ShapelessRecipe SportHelmetRecipe;
    public static ShapelessRecipe SportChestplateRecipe;
    public static ShapelessRecipe SportLeggingsRecipe;
    public static ShapelessRecipe SportBootsRecipe;
    public static ShapelessRecipe BombRecipe;
    public static ShapelessRecipe BeerRecipe;
    public static ShapelessRecipe PorterRecipe;
    public static ShapelessRecipe RedAleRecipe;
    public static ShapelessRecipe UgiBookRecipe;

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
        //<editor-fold desc="Улучшенное зелье" defaultstate="collapsed">
        BetterPotionRecipe = new ShapedRecipe(new NamespacedKey(plugin, "betterpotion"), CreateItem(UmcpItem.BETTER_POTION, 1));
        BetterPotionRecipe.shape("wgw", "wpw", "wdw");
        BetterPotionRecipe.setIngredient('w', Material.NETHER_WART);
        BetterPotionRecipe.setIngredient('p', Material.POTION);
        BetterPotionRecipe.setIngredient('g', Material.GOLD_INGOT);
        BetterPotionRecipe.setIngredient('d', Material.DIAMOND);
        //</editor-fold>
        //<editor-fold desc="лук, который при попадании может бить молнией игрока" defaultstate="collapsed">
        ItemStack thunderBow = CreateItem(UmcpItem.THUNDERBOW, 1);
        thunderBow.setDurability((short) (Material.BOW.getMaxDurability() - 3));
        ThunderBowRecipe = new ShapedRecipe(new NamespacedKey(plugin, "thunderbow"), thunderBow);
        ThunderBowRecipe.shape("sws", "sbs", "sss");
        ThunderBowRecipe.setIngredient('s', Material.SPRUCE_SAPLING);
        ThunderBowRecipe.setIngredient('w', Material.WATER_BUCKET);
        ThunderBowRecipe.setIngredient('b', Material.BOW);
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
        //<editor-fold desc="Книга" defaultstate="collapsed">
        UgiBookRecipe = new ShapelessRecipe(new NamespacedKey(plugin, "ugi_book"), CreateItem(UmcpItem.BOOK, 1));
        UgiBookRecipe.addIngredient(Material.BOOK);
        UgiBookRecipe.addIngredient(Material.IRON_NUGGET);
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

    public static void AddUpgrades(Plugin plugin) {
        RecipeChoice.MaterialChoice upgradeItem = new RecipeChoice.MaterialChoice(Arrays.asList(
                Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL,
                Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE,
                Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE,
                Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.DIAMOND_HOE,
                Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD,
                Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.DIAMOND_HELMET,
                Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.DIAMOND_CHESTPLATE,
                Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.DIAMOND_LEGGINGS,
                Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.DIAMOND_BOOTS,
                Material.BOW, Material.CROSSBOW,
                Material.TURTLE_HELMET,
                Material.FISHING_ROD,
                Material.SHEARS,
                Material.SHIELD,
                Material.TRIDENT,
                Material.BOOK
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.OBSIDIAN, Material.OBSIDIAN, Material.OBSIDIAN,
                        Material.OBSIDIAN, Material.AIR, Material.OBSIDIAN,
                        Material.OBSIDIAN, Material.OBSIDIAN, Material.OBSIDIAN
                }, upgradeItem, new NamespacedKey(plugin, "durability_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.REDSTONE, Material.DIAMOND, Material.REDSTONE,
                        Material.REDSTONE, Material.AIR, Material.REDSTONE,
                        Material.REDSTONE, Material.DIAMOND, Material.REDSTONE
                }, upgradeItem, new NamespacedKey(plugin, "damage_all_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.REDSTONE, Material.GHAST_TEAR, Material.REDSTONE,
                        Material.REDSTONE, Material.AIR, Material.REDSTONE,
                        Material.REDSTONE, Material.REDSTONE, Material.REDSTONE
                }, upgradeItem, new NamespacedKey(plugin, "dig_speed_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.COBWEB, Material.COBWEB, Material.COBWEB,
                        Material.COBWEB, Material.AIR, Material.COBWEB,
                        Material.COBWEB, Material.COBWEB, Material.COBWEB
                }, upgradeItem, new NamespacedKey(plugin, "silk_touch_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.LAPIS_LAZULI, Material.RABBIT_FOOT, Material.LAPIS_LAZULI,
                        Material.LAPIS_LAZULI, Material.AIR, Material.LAPIS_LAZULI,
                        Material.LAPIS_LAZULI, Material.LAPIS_LAZULI, Material.LAPIS_LAZULI
                }, upgradeItem, new NamespacedKey(plugin, "loot_bonus_blocks_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.TROPICAL_FISH, Material.REDSTONE, Material.TROPICAL_FISH,
                        Material.REDSTONE, Material.AIR, Material.REDSTONE,
                        Material.TROPICAL_FISH, Material.REDSTONE, Material.TROPICAL_FISH
                }, upgradeItem, new NamespacedKey(plugin, "luck_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.PUFFERFISH, Material.LAPIS_LAZULI, Material.PUFFERFISH,
                        Material.LAPIS_LAZULI, Material.AIR, Material.LAPIS_LAZULI,
                        Material.PUFFERFISH, Material.LAPIS_LAZULI, Material.PUFFERFISH
                }, upgradeItem, new NamespacedKey(plugin, "lure_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.OBSIDIAN, Material.DIAMOND_BLOCK, Material.OBSIDIAN,
                        Material.ENDER_EYE, Material.AIR, Material.ENDER_EYE,
                        Material.OBSIDIAN, Material.NAUTILUS_SHELL, Material.OBSIDIAN
                }, upgradeItem, new NamespacedKey(plugin, "mending_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.OBSIDIAN, Material.IRON_INGOT, Material.OBSIDIAN,
                        Material.IRON_INGOT, Material.AIR, Material.IRON_INGOT,
                        Material.OBSIDIAN, Material.IRON_INGOT, Material.OBSIDIAN
                }, upgradeItem, new NamespacedKey(plugin, "protection_environmental_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.SLIME_BALL, Material.SLIME_BALL, Material.SLIME_BALL,
                        Material.SLIME_BALL, Material.AIR, Material.SLIME_BALL,
                        Material.SLIME_BALL, Material.SLIME_BALL, Material.SLIME_BALL
                }, upgradeItem, new NamespacedKey(plugin, "protection_fall_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.GLASS, Material.TROPICAL_FISH, Material.GLASS,
                        Material.TROPICAL_FISH, Material.AIR, Material.TROPICAL_FISH,
                        Material.GLASS, Material.TROPICAL_FISH, Material.GLASS
                }, upgradeItem, new NamespacedKey(plugin, "oxygen_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.GLASS, Material.PUFFERFISH, Material.GLASS,
                        Material.PUFFERFISH, Material.AIR, Material.PUFFERFISH,
                        Material.GLASS, Material.PUFFERFISH, Material.GLASS
                }, upgradeItem, new NamespacedKey(plugin, "water_worker_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.SLIME_BALL, Material.DIAMOND_SWORD, Material.SLIME_BALL,
                        Material.SLIME_BALL, Material.AIR, Material.SLIME_BALL,
                        Material.SLIME_BALL, Material.GOLDEN_SWORD, Material.SLIME_BALL
                }, upgradeItem, new NamespacedKey(plugin, "thorns_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.BUCKET, Material.TROPICAL_FISH, Material.BUCKET,
                        Material.TROPICAL_FISH, Material.AIR, Material.TROPICAL_FISH,
                        Material.BUCKET, Material.TROPICAL_FISH, Material.BUCKET
                }, upgradeItem, new NamespacedKey(plugin, "depth_strider_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.PACKED_ICE, Material.PACKED_ICE, Material.PACKED_ICE,
                        Material.PACKED_ICE, Material.AIR, Material.PACKED_ICE,
                        Material.PACKED_ICE, Material.PACKED_ICE, Material.PACKED_ICE
                }, upgradeItem, new NamespacedKey(plugin, "frost_walker_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.BAMBOO, Material.PISTON, Material.BAMBOO,
                        Material.BAMBOO, Material.AIR, Material.BAMBOO,
                        Material.BAMBOO, Material.PISTON, Material.BAMBOO
                }, upgradeItem, new NamespacedKey(plugin, "knockback_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.BLAZE_POWDER, Material.BLAZE_POWDER, Material.BLAZE_POWDER,
                        Material.BLAZE_POWDER, Material.AIR, Material.BLAZE_POWDER,
                        Material.BLAZE_POWDER, Material.BLAZE_POWDER, Material.BLAZE_POWDER
                }, upgradeItem, new NamespacedKey(plugin, "fire_aspect_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.LAPIS_LAZULI, Material.LAPIS_LAZULI, Material.LAPIS_LAZULI,
                        Material.LAPIS_LAZULI, Material.AIR, Material.LAPIS_LAZULI,
                        Material.LAPIS_LAZULI, Material.RABBIT_FOOT, Material.LAPIS_LAZULI
                }, upgradeItem, new NamespacedKey(plugin, "loot_bonus_mobs_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.GOLD_INGOT, Material.TORCH, Material.GOLD_INGOT,
                        Material.IRON_INGOT, Material.AIR, Material.IRON_INGOT,
                        Material.GOLD_INGOT, Material.IRON_INGOT, Material.GOLD_INGOT
                }, upgradeItem, new NamespacedKey(plugin, "sweeping_edge_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.REDSTONE, Material.COOKED_BEEF, Material.REDSTONE,
                        Material.REDSTONE, Material.AIR, Material.REDSTONE,
                        Material.REDSTONE, Material.DIAMOND, Material.REDSTONE
                }, upgradeItem, new NamespacedKey(plugin, "arrow_damage_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.BAMBOO, Material.BAMBOO, Material.BAMBOO,
                        Material.PISTON, Material.AIR, Material.PISTON,
                        Material.BAMBOO, Material.BAMBOO, Material.BAMBOO
                }, upgradeItem, new NamespacedKey(plugin, "arrow_knockback_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.BLAZE_ROD, Material.BLAZE_POWDER, Material.BLAZE_ROD,
                        Material.BLAZE_POWDER, Material.AIR, Material.BLAZE_POWDER,
                        Material.BLAZE_ROD, Material.BLAZE_POWDER, Material.BLAZE_ROD
                }, upgradeItem, new NamespacedKey(plugin, "arrow_fire_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.GOLD_INGOT, Material.DIAMOND_BLOCK, Material.GOLD_INGOT,
                        Material.GOLD_INGOT, Material.AIR, Material.GOLD_INGOT,
                        Material.GOLD_INGOT, Material.EMERALD_BLOCK, Material.GOLD_INGOT
                }, upgradeItem, new NamespacedKey(plugin, "arrow_infinite_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.COOKED_BEEF, Material.BONE, Material.COOKED_BEEF,
                        Material.COOKED_BEEF, Material.AIR, Material.COOKED_BEEF,
                        Material.COOKED_BEEF, Material.LEAD, Material.COOKED_BEEF
                }, upgradeItem, new NamespacedKey(plugin, "loyalty_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.COD, Material.NAUTILUS_SHELL, Material.COD,
                        Material.COD, Material.AIR, Material.COD,
                        Material.COD, Material.FISHING_ROD, Material.COD
                }, upgradeItem, new NamespacedKey(plugin, "impaling_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.SLIME_BALL, Material.LEAD, Material.SLIME_BALL,
                        Material.SLIME_BALL, Material.AIR, Material.SLIME_BALL,
                        Material.SLIME_BALL, Material.FISHING_ROD, Material.SLIME_BALL
                }, upgradeItem, new NamespacedKey(plugin, "riptide_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.QUARTZ_BLOCK, Material.DIAMOND_BLOCK, Material.QUARTZ_BLOCK,
                        Material.SNOW_BLOCK, Material.AIR, Material.SNOW_BLOCK,
                        Material.QUARTZ_BLOCK, Material.JUNGLE_SAPLING, Material.QUARTZ_BLOCK
                }, upgradeItem, new NamespacedKey(plugin, "channeling_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.ARROW, Material.ARROW, Material.ARROW,
                        Material.CROSSBOW, Material.AIR, Material.CROSSBOW,
                        Material.ARROW, Material.ARROW, Material.ARROW
                }, upgradeItem, new NamespacedKey(plugin, "multishot_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.GLOWSTONE_DUST, Material.GHAST_TEAR, Material.GLOWSTONE_DUST,
                        Material.GLOWSTONE_DUST, Material.AIR, Material.GLOWSTONE_DUST,
                        Material.GLOWSTONE_DUST, Material.GLOWSTONE_DUST, Material.GLOWSTONE_DUST
                }, upgradeItem, new NamespacedKey(plugin, "quick_charge_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.SPECTRAL_ARROW, Material.SPECTRAL_ARROW, Material.SPECTRAL_ARROW,
                        Material.SPECTRAL_ARROW, Material.AIR, Material.SPECTRAL_ARROW,
                        Material.SPECTRAL_ARROW, Material.SPECTRAL_ARROW, Material.SPECTRAL_ARROW
                }, upgradeItem, new NamespacedKey(plugin, "piercing_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.BLAZE_POWDER, Material.BLAZE_ROD, Material.BLAZE_POWDER,
                        Material.BLAZE_ROD, Material.AIR, Material.BLAZE_ROD,
                        Material.BLAZE_POWDER, Material.BLAZE_ROD, Material.BLAZE_POWDER
                }, upgradeItem, new NamespacedKey(plugin, "protection_fire_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.GUNPOWDER, Material.TNT, Material.GUNPOWDER,
                        Material.TNT, Material.AIR, Material.TNT,
                        Material.GUNPOWDER, Material.TNT, Material.GUNPOWDER
                }, upgradeItem, new NamespacedKey(plugin, "protection_explosions_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.GUNPOWDER, Material.FIREWORK_STAR, Material.GUNPOWDER,
                        Material.FIREWORK_STAR, Material.AIR, Material.FIREWORK_STAR,
                        Material.GUNPOWDER, Material.FIREWORK_STAR, Material.GUNPOWDER
                }, upgradeItem, new NamespacedKey(plugin, "protection_projectile_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.LAPIS_LAZULI, Material.WATER_BUCKET, Material.LAPIS_LAZULI,
                        Material.LAPIS_LAZULI, Material.AIR, Material.LAPIS_LAZULI,
                        Material.LAPIS_LAZULI, Material.PISTON, Material.LAPIS_LAZULI
                }, upgradeItem, new NamespacedKey(plugin, "damage_undead_upgrade")
        ));
        plugin.getServer().addRecipe(CreateUpgrade(
                new Material[]{
                        Material.SPIDER_EYE, Material.COBWEB, Material.SPIDER_EYE,
                        Material.COBWEB, Material.AIR, Material.COBWEB,
                        Material.SPIDER_EYE, Material.COBWEB, Material.SPIDER_EYE
                }, upgradeItem, new NamespacedKey(plugin, "damage_arthropods_upgrade")
        ));
    }

    private static ShapedRecipe CreateUpgrade(Material[] materials, RecipeChoice.MaterialChoice items, NamespacedKey namespacedKey) {
        ShapedRecipe recipe = new ShapedRecipe(namespacedKey, new ItemStack(Material.BOOK, 1));
        recipe.shape("abc", "def", "ghi");
        recipe.setIngredient('a', materials[0]);
        recipe.setIngredient('b', materials[1]);
        recipe.setIngredient('c', materials[2]);
        recipe.setIngredient('d', materials[3]);
        recipe.setIngredient('e', items);
        recipe.setIngredient('f', materials[5]);
        recipe.setIngredient('g', materials[6]);
        recipe.setIngredient('h', materials[7]);
        recipe.setIngredient('i', materials[8]);
        return recipe;
    }
}

