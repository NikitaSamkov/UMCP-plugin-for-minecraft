package org.umc.umcp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.umc.umcp.commands.InstituteTabExecutor;
import org.umc.umcp.listeners.PlayerChatListener;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getLogger().info("Я ЖИВОЙ!!1!!");
        getCommand("test").setExecutor(new MyExecutor());
        getCommand("institute").setExecutor(new InstituteTabExecutor());
        Bukkit.getServer().getPluginManager().registerEvents(new MyListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
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
