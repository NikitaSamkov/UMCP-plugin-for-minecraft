package org.umc.umcp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.umc.umcp.commands.InstituteTabExecutor;
import org.umc.umcp.connection.DBConnection;
import org.umc.umcp.listeners.CraftListener;
import org.umc.umcp.listeners.PlayerChatListener;

public final class Main extends JavaPlugin {
    public static DBConnection conn = new DBConnection("jdbc:mysql://umcraft.scalacubes.org:2163/UMCraft", "root", "4o168PPYSIdyjFU");

    @Override
    public void onEnable() {
        this.getLogger().info("Я ЖИВОЙ!!1!!");
        PermissionMaster.plugin = this;
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
    }
}
