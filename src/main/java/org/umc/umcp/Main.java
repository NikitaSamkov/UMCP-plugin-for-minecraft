package org.umc.umcp;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Statistic;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.umc.umcp.armorset.ArmorEquipEvent.ArmorListener;
import org.umc.umcp.armorset.ArmorEquipEvent.DispenserArmorListener;
import org.umc.umcp.armorset.SetMaster;
import org.umc.umcp.armorset.UmcpArmorSet;
import org.umc.umcp.commands.InstituteTabExecutor;
import org.umc.umcp.commands.Painter;
import org.umc.umcp.connection.DBConnection;
import org.umc.umcp.enums.InstituteNames;
import org.umc.umcp.enums.UmcpItem;
import org.umc.umcp.listeners.CraftListener;
import org.umc.umcp.listeners.GlobalListener;
import org.umc.umcp.listeners.IENIMListener;
import org.umc.umcp.misc.Cooldowns;
import org.umc.umcp.misc.Crafter;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {
    public static DBConnection conn;
    public static FileConfiguration config;
    public static FileConfiguration cd;
    private static Economy econ = null;
    public static final Logger log = Logger.getLogger("Minecraft");
    private static int scholarshipAmount;
    private static int scholarshipCooldown;
    private static LuckPerms LPapi;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = this.getConfig();
        cd = Cooldowns.GetCooldowns(this);

        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!setupLP() ) {
            log.severe(String.format("[%s] - Disabled due to no LuckPerms dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        conn = new DBConnection(config.getString("database.url"), config.getString("database.login"), config.getString("database.password"));

        addArmorEquipEvent();
        addSets();

        Painter.PreparePaints();

        getCommand("test").setExecutor(new MyExecutor());
        getCommand("urfu").setExecutor(new InstituteTabExecutor());

        Bukkit.getServer().getPluginManager().registerEvents(new MyListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new CraftListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new GlobalListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new IENIMListener(), this);

        this.getLogger().info("Я ЖИВОЙ!!1!!");

        addCrafts();

        scholarshipAmount = config.getInt("urfu.scholarship.Amount");
        scholarshipCooldown = ((config.getInt("urfu.scholarship.Cooldown.Hours") * 60 +
                config.getInt("urfu.scholarship.Cooldown.Minutes")) * 60 +
                config.getInt("urfu.scholarship.Cooldown.Seconds")) * 20;
        CheckScholarship();
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
        saveDefaultConfig();
        Cooldowns.SaveCooldowns(this);
        this.getLogger().info("ПРАЩЯЙ ЖИСТОКИЙ МИР!11!!1!!");
    }

    private void addCrafts() {
        Crafter.AddCrafts(this);

        ShapedRecipe diamond = new ShapedRecipe(new ItemStack(Material.DIAMOND, 1));
        diamond.shape("***", "***", "***");
        diamond.setIngredient('*', Material.GRASS_BLOCK);
        getServer().addRecipe(diamond);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupLP() {
        if (getServer().getPluginManager().getPlugin("LuckPerms") == null) {
            return false;
        }
        RegisteredServiceProvider<LuckPerms> rsp = getServer().getServicesManager().getRegistration(LuckPerms.class);
        if (rsp == null) {
            return false;
        }
        LPapi = rsp.getProvider();
        return LPapi != null;
    }

    private void CheckScholarship() {
        Server server = this.getServer();
        Collection<? extends Player> players = server.getOnlinePlayers();
        server.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                for(Player p: players) {
                    int ticks = p.getStatistic(Statistic.PLAY_ONE_MINUTE);
                    if (ticks > scholarshipCooldown) {
                        p.setStatistic(Statistic.PLAY_ONE_MINUTE, ticks - scholarshipCooldown);
                        econ.depositPlayer(p, scholarshipAmount);
                        p.sendMessage(String.format("Начислена стипендия в размере %d!", scholarshipAmount));
                    }
                }
                CheckScholarship();
            }
        }, 12000L);
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static LuckPerms getLPapi() {
        return LPapi;
    }

    public static void addPermission(UUID uuid, String permission) {
        User user = LPapi.getUserManager().getUser(uuid);
        user.data().add(Node.builder(permission).value(true).build());
        LPapi.getUserManager().saveUser(user);
    }

    public static void removePermission(UUID uuid, String permission) {
        User user = LPapi.getUserManager().getUser(uuid);
        user.data().remove(Node.builder(permission).build());
        LPapi.getUserManager().saveUser(user);
    }
}
