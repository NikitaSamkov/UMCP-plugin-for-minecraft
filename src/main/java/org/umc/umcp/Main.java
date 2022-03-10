package org.umc.umcp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.umc.umcp.commands.InstituteTabExecutor;
import org.umc.umcp.connection.DBConnection;
import org.umc.umcp.listeners.CraftListener;
import org.umc.umcp.listeners.PlayerChatListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Main extends JavaPlugin {
    public static DBConnection conn = new DBConnection("jdbc:mysql://umcraft.scalacubes.org:2163/UMCraft?useSSL=false", "root", "4o168PPYSIdyjFU");

    @Override
    public void onEnable() {
        this.getLogger().info("Я ЖИВОЙ!!1!!");
        getCommand("test").setExecutor(new MyExecutor());
        getCommand("institute").setExecutor(new InstituteTabExecutor());
        Bukkit.getServer().getPluginManager().registerEvents(new MyListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new CraftListener(), this);
        addCrafts();

    }

    @Override
    public void onDisable() {
        this.getLogger().info("ПРАЩЯЙ ЖИСТОКИЙ МИР!11!!1!!");
    }

    private void addCrafts() {
        ShapedRecipe diamond = new ShapedRecipe(new ItemStack(Material.DIAMOND, 1));
        diamond.shape("***", "***", "***");
        diamond.setIngredient('*', Material.GRASS_BLOCK);
        getServer().addRecipe(diamond);

        ItemStack Vape = new ItemStack(Material.POTION, 1);
        ItemMeta vapeMeta = Vape.getItemMeta();
        vapeMeta.setDisplayName("Вейп");
        List<String> lore = new ArrayList<>();
        lore.add("Одноразовый вейп радиста.");
        lore.add("Слишком частое употребление вредно для здоровья!");
        vapeMeta.setLore(lore);
        vapeMeta.setCustomModelData(1337);
        Vape.setItemMeta(vapeMeta);
        ShapedRecipe VapeRecipe = new ShapedRecipe(NamespacedKey.minecraft("vape"), Vape);
        VapeRecipe.shape(" s ", " w ", " c ");
        VapeRecipe.setIngredient('s', Material.SUGAR);
        VapeRecipe.setIngredient('w', Material.POTION);
        VapeRecipe.setIngredient('c', Material.COAL);
        getServer().addRecipe(VapeRecipe);
    }
}
