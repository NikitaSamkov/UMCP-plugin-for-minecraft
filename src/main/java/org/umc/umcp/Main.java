package org.umc.umcp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.umc.umcp.armorset.ArmorEquipEvent.ArmorListener;
import org.umc.umcp.armorset.ArmorEquipEvent.DispenserArmorListener;
import org.umc.umcp.armorset.SetMaster;
import org.umc.umcp.armorset.UmcpArmorSet;
import org.umc.umcp.commands.InstituteTabExecutor;
import org.umc.umcp.connection.DBConnection;
import org.umc.umcp.enums.UmcpItem;
import org.umc.umcp.listeners.CraftListener;
import org.umc.umcp.listeners.GlobalListener;
import org.umc.umcp.listeners.PlayerChatListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Main extends JavaPlugin {
    public static DBConnection conn = new DBConnection("jdbc:mysql://umcraft.scalacubes.org:2163/UMCraft?useSSL=false", "root", "4o168PPYSIdyjFU");

    @Override
    public void onEnable() {
        saveDefaultConfig();
        addArmorEquipEvent();
        this.getLogger().info("Я ЖИВОЙ!!1!!");
        getCommand("test").setExecutor(new MyExecutor());
        getCommand("institute").setExecutor(new InstituteTabExecutor());
        Bukkit.getServer().getPluginManager().registerEvents(new MyListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new CraftListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new GlobalListener(this), this);
        addCrafts();
        addSets();
    }

    private void addSets() {
        SetMaster.SetPlugin(this);
        SetMaster.AddSet(new UmcpArmorSet(
                null,
                null,
                UmcpItem.LONGSOCKS,
                UmcpItem.SOCKS,
                Arrays.asList(new PotionEffect(PotionEffectType.SPEED, 100000, 1, false, false)), false
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
    }
}
