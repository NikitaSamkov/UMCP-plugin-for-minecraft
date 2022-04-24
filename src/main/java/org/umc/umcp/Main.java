package org.umc.umcp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.umc.umcp.armorset.ArmorEquipEvent.ArmorListener;
import org.umc.umcp.armorset.ArmorEquipEvent.DispenserArmorListener;
import org.umc.umcp.armorset.SetMaster;
import org.umc.umcp.armorset.UmcpArmorSet;
import org.umc.umcp.commands.IenimTabExecutor;
import org.umc.umcp.commands.InstituteTabExecutor;
import org.umc.umcp.commands.Painter;
import org.umc.umcp.connection.DBConnection;
import org.umc.umcp.enums.InstituteNames;
import org.umc.umcp.enums.UmcpItem;
import org.umc.umcp.listeners.CraftListener;
import org.umc.umcp.listeners.GlobalListener;
import org.umc.umcp.listeners.IENIMListener;
import org.umc.umcp.listeners.PlayerChatListener;
import org.umc.umcp.misc.Crafter;

import java.util.Arrays;

public final class Main extends JavaPlugin {
    public static DBConnection conn;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = this.getConfig();
        conn = new DBConnection(config.getString("database.url"), config.getString("database.login"), config.getString("database.password"));
        addArmorEquipEvent();
        addSets();
        this.getLogger().info("Я ЖИВОЙ!!1!!");
        Painter.PreparePaints();
        getCommand("test").setExecutor(new MyExecutor());
        getCommand("urfu").setExecutor(new InstituteTabExecutor());
        getCommand("ienim").setExecutor(new IenimTabExecutor());
        Bukkit.getServer().getPluginManager().registerEvents(new MyListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new CraftListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new GlobalListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new IENIMListener(), this);
        addCrafts();
    }

    private void addSets() {
        SetMaster.SetPlugin(this);
        SetMaster.AddSet(new UmcpArmorSet(
                null,
                null,
                UmcpItem.LONGSOCKS,
                UmcpItem.SOCKS,
                Arrays.asList(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false)),
                false,
                InstituteNames.RTF
        ));
        SetMaster.AddSet(new UmcpArmorSet(
                UmcpItem.SPORT_HELMET,
                UmcpItem.SPORT_CHESTPLATE,
                UmcpItem.SPORT_LEGGINGS,
                UmcpItem.SPORT_BOOTS,
                Arrays.asList(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false),
                        new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false)),
                false,
                InstituteNames.IFKSIMP
        ));
    }

    private void addArmorEquipEvent() {
        getServer().getPluginManager().registerEvents(new ArmorListener(getConfig().getStringList("blocked")), this);
        try{
            //Better way to check for this? Only in 1.13.1+?
            Class.forName("org.bukkit.event.block.BlockDispenseArmorEvent");
            getServer().getPluginManager().registerEvents(new DispenserArmorListener(), this);
        }catch(Exception ignored){}
    }

    @Override
    public void onDisable() {
        this.getLogger().info("ПРАЩЯЙ ЖИСТОКИЙ МИР!11!!1!!");
    }

    private void addCrafts() {
        Crafter.CreateCrafts(this);

        ShapedRecipe diamond = new ShapedRecipe(new ItemStack(Material.DIAMOND, 1));
        diamond.shape("***", "***", "***");
        diamond.setIngredient('*', Material.GRASS_BLOCK);
        getServer().addRecipe(diamond);

        getServer().addRecipe(Crafter.VapeRecipe);
        getServer().addRecipe(Crafter.SocksRecipe);
        getServer().addRecipe(Crafter.LongsocksRecipe);
        getServer().addRecipe(Crafter.CatEarsRecipe);
        getServer().addRecipe(Crafter.SportHelmetRecipe);
        getServer().addRecipe(Crafter.SportChestplateRecipe);
        getServer().addRecipe(Crafter.SportLeggingsRecipe);
        getServer().addRecipe(Crafter.SportBootsRecipe);
        getServer().addRecipe(Crafter.BombRecipe);
        getServer().addRecipe(Crafter.AdrenalineRecipe);
        getServer().addRecipe(Crafter.BurnRecipe);
        getServer().addRecipe(Crafter.MonsterRecipe);
        getServer().addRecipe(Crafter.RedbullRecipe);
        getServer().addRecipe(Crafter.BeerRecipe);
        getServer().addRecipe(Crafter.PorterRecipe);
        getServer().addRecipe(Crafter.RedAleRecipe);
        getServer().addRecipe(Crafter.BetterPotionRecipe);
        getServer().addRecipe(Crafter.UgiBookRecipe);
    }
}
