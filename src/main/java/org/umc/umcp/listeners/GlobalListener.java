package org.umc.umcp.listeners;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.umc.umcp.misc.Cooldowns;
import org.umc.umcp.Main;
import org.umc.umcp.armorset.ArmorEquipEvent.ArmorEquipEvent;
import org.umc.umcp.armorset.SetMaster;
import org.umc.umcp.enums.CooldownType;
import org.umc.umcp.enums.InstituteNames;
import org.umc.umcp.enums.UmcpItem;
import org.umc.umcp.misc.Crafter;

import java.util.*;

public class GlobalListener implements Listener {
    private Plugin plugin;
    private final ConfigurationSection rtfParams;
    private final ConfigurationSection uraleninParams;

    public GlobalListener(Plugin plugin) {
        this.plugin = plugin;
        rtfParams = Main.config.getConfigurationSection("rtf.params");
        uraleninParams = Main.config.getConfigurationSection("uralenin.params");
    }


    @EventHandler
    public void onPotionMake(BrewEvent e) {
        for (int i = 0; i < 3; i++) {
            ItemStack item = e.getContents().getItem(i);
            if (item == null) {
                continue;
            }
            if (item.hasItemMeta() && item.getItemMeta().hasCustomModelData() &&
                    (UmcpItem.VAPE.check(item) ||
                            UmcpItem.ADRENALINE.check(item) ||
                            UmcpItem.BURN.check(item) ||
                            UmcpItem.MONSTER.check(item) ||
                            UmcpItem.REDBULL.check(item) ||
                            UmcpItem.BEER.check(item) ||
                            UmcpItem.PORTER.check(item) ||
                            UmcpItem.RED_ALE.check(item) ||
                            UmcpItem.INMIT_BEER.check(item))) {
                e.getContents().setIngredient(new ItemStack(Material.AIR));
                e.setCancelled(true);
                return;
            }
            //upgraded by HTI
            if (item.hasItemMeta() && item.getItemMeta().hasCustomModelData() &&
            item.getItemMeta().getCustomModelData() == 99) {
                ItemStack ingredient = e.getContents().getIngredient();
                if ((!ingredient.getType().equals(Material.GUNPOWDER) && !ingredient.getType().equals(Material.DRAGON_BREATH)) ||
                        (ingredient.getType().equals(Material.GUNPOWDER) && !item.getType().equals(Material.POTION)) ||
                        (ingredient.getType().equals(Material.DRAGON_BREATH) && !item.getType().equals(Material.SPLASH_POTION))) {
                    continue;
                }
                ItemStack newPotion = (ingredient.getType().equals(Material.GUNPOWDER)) ?
                        new ItemStack(Material.SPLASH_POTION) : new ItemStack(Material.LINGERING_POTION);
                newPotion.setItemMeta(item.getItemMeta());
                e.getContents().setItem(i, newPotion);
            }
        }
    }

    @EventHandler
    public void onItemUse(PlayerItemConsumeEvent e) {
        ItemStack item = e.getItem();
        if (UmcpItem.VAPE.check(item)) {
            Player player = e.getPlayer();
            if (Main.conn.GetInstitute(player.getUniqueId().toString()).equals(InstituteNames.RTF.name)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,
                        rtfParams.getInt("VapeDuration"),
                        rtfParams.getInt("VapeAmplifier"),
                        false, false));
            } else {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
                        rtfParams.getInt("VapePoisonDuration"),
                        rtfParams.getInt("VapePoisonAmplifier"),
                        true, true));
            }

            Location loc = player.getLocation();
            loc.setY(loc.getY() + rtfParams.getInt("SteamHeight"));
            //<editor-fold desc="Cloud creation" defaultstate="collapsed">
            AreaEffectCloud steam = (AreaEffectCloud) player.getWorld().spawnEntity(loc, EntityType.AREA_EFFECT_CLOUD);
            steam.setColor(Color.fromRGB(255, 255, 255));
            steam.setRadius(rtfParams.getInt("SteamRadius"));
            steam.setSource(player);
            steam.setDuration(rtfParams.getInt("SteamDuration"));
            steam.setParticle(Particle.CAMPFIRE_COSY_SMOKE);
            steam.setMetadata("isVapeSteam", new FixedMetadataValue(plugin, true));
            steam.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1, 0), false);
            //</editor-fold>
            if (!Cooldowns.UpdateWithDiff(player.getUniqueId(), CooldownType.VAPE)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
                        rtfParams.getInt("VapePenaltyDuration"), rtfParams.getInt("VapePenaltyAmplifier")));
            }
            return;
        }

        if (UmcpItem.ADRENALINE.check(item) ||
                UmcpItem.BURN.check(item) ||
                UmcpItem.MONSTER.check(item) ||
                UmcpItem.REDBULL.check(item)) {

            Player player = e.getPlayer();
            if (UmcpItem.ADRENALINE.check(item)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                        Main.config.getInt("uralenin.params.adrenaline.Speed.Duration"),
                        Main.config.getInt("uralenin.params.adrenaline.Speed.Amplifier"),
                        true, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,
                        Main.config.getInt("uralenin.params.adrenaline.Regen.Duration"),
                        Main.config.getInt("uralenin.params.adrenaline.Regen.Amplifier"),
                        true, true));
            }

            if (UmcpItem.BURN.check(item)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                        Main.config.getInt("uralenin.params.burn.Speed.Duration"),
                        Main.config.getInt("uralenin.params.burn.Speed.Amplifier"),
                        true, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,
                        Main.config.getInt("uralenin.params.burn.Fireresist.Duration"),
                        Main.config.getInt("uralenin.params.burn.Fireresist.Amplifier"),
                        true, true));
            }

            if (UmcpItem.MONSTER.check(item)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                        Main.config.getInt("uralenin.params.moster.Speed.Duration"),
                        Main.config.getInt("uralenin.params.moster.Speed.Amplifier"),
                        true, true));
            }

            if (UmcpItem.REDBULL.check(item)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                        Main.config.getInt("uralenin.params.redbull.Speed.Duration"),
                        Main.config.getInt("uralenin.params.redbull.Speed.Amplifier"),
                        true, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,
                        Main.config.getInt("uralenin.params.redbull.Fireresist.Duration"),
                        Main.config.getInt("uralenin.params.redbull.Fireresist.Amplifier"),
                        true, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,
                        Main.config.getInt("uralenin.params.redbull.Regen.Duration"),
                        Main.config.getInt("uralenin.params.redbull.Regen.Amplifier"),
                        true, true));
            }

            if (!Cooldowns.UpdateWithDiff(player.getUniqueId(), CooldownType.ENERGETICS)) {
                Cooldowns.Clear(player.getUniqueId());
                player.setHealth(0);
            }
            return;
        }

        if (UmcpItem.BEER.check(item) ||
                UmcpItem.PORTER.check(item) ||
                UmcpItem.RED_ALE.check(item) ||
                UmcpItem.INMIT_BEER.check(item)) {
            Player player = e.getPlayer();

            if (UmcpItem.INMIT_BEER.check(item)) {
                ConfigurationSection params = Main.config.getConfigurationSection("inmit.params.beer");
                if (Main.conn.GetInstitute(player.getUniqueId().toString()).equals(InstituteNames.INMIT.name)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,
                            params.getInt("Duration"),
                            params.getInt("Amplifier"),
                            false,
                            false));
                } else {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
                            params.getInt("Poison.Duration"),
                            params.getInt("Poison.Amplifier"),
                            true,
                            true));
                }
            } else {
                ConfigurationSection params = Main.config.getConfigurationSection("hti.params." +
                        ((UmcpItem.BEER.check(item)) ? "beer" :
                                (UmcpItem.PORTER.check(item)) ? "porter" : "red_ale"));
                player.setHealth(Math.min(player.getHealth() + params.getInt("Heal"),
                        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
                player.setFoodLevel(player.getFoodLevel() + params.getInt("Hunger"));
            }

            if (!Cooldowns.UpdateWithDiff(player.getUniqueId(), CooldownType.BEER)) {
                ConfigurationSection effectParams = Main.config.getConfigurationSection("cooldowns.beer.effects");
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,
                        effectParams.getInt("nausea.Duration"),
                        effectParams.getInt("nausea.Amplifier"),
                        true,
                        true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,
                        effectParams.getInt("weakness.Duration"),
                        effectParams.getInt("weakness.Amplifier"),
                        true,
                        true));
            }
        }
        if (item.getType().equals(Material.GOLDEN_APPLE)) {
            Player player = e.getPlayer();
            if (Main.conn.GetInstitute(player.getUniqueId().toString()).equals(InstituteNames.HTI.name)) {
                ConfigurationSection params = Main.config.getConfigurationSection("hti.params.golden_apple");
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,
                        2400 + params.getInt("AbsorbAdditionalDuration"),
                        0,
                        true,
                        true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,
                        100 + params.getInt("RegenAdditionalDuration"),
                        1,
                        true,
                        true));
            }
        }
        if (item.getType().equals(Material.GOLDEN_CARROT)) {
            Player player = e.getPlayer();
            if (Main.conn.GetInstitute(player.getUniqueId().toString()).equals(InstituteNames.HTI.name)) {
                ConfigurationSection params = Main.config.getConfigurationSection("hti.params.golden_carrot");
                player.setSaturation(player.getSaturation() + (float) params.getDouble("AdditionalSaturation"));
            }
        }
    }

    @EventHandler
    public void onAreaCloud(AreaEffectCloudApplyEvent e) {
        if (e.getEntity().hasMetadata("isVapeSteam") && e.getEntity().getMetadata("isVapeSteam").get(0).asBoolean()) {
            List<LivingEntity> entities = e.getAffectedEntities();
            for (LivingEntity entity: entities) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    String institute = Main.conn.GetInstitute(player.getUniqueId().toString());
                    if (!Objects.equals(institute, InstituteNames.RTF.name)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
                                rtfParams.getInt("SteamBlindnessDuration"),
                                rtfParams.getInt("SteamBlindnessAmplifier")));
                    }
                }
            }
        }
        if (e.getEntity().hasMetadata("isDirtyCloud") && e.getEntity().getMetadata("isDirtyCloud").get(0).asBoolean()) {
            for (LivingEntity entity: e.getAffectedEntities()) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    if (Main.conn.GetInstitute(player.getUniqueId().toString()).equals(InstituteNames.URALENIN.name)) {
                        continue;
                    }
                }
                entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
                        uraleninParams.getInt("bomb.Poison.Duration"),
                        uraleninParams.getInt("bomb.Poison.Amplifier")));
                entity.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,
                        uraleninParams.getInt("bomb.Nausea.Duration"),
                        uraleninParams.getInt("bomb.Nausea.Amplifier")));
                entity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,
                        uraleninParams.getInt("bomb.Weakness.Duration"),
                        uraleninParams.getInt("bomb.Weakness.Amplifier")));
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        SetMaster.CheckSets(e.getPlayer());
        if (Main.conn.GetInstitute(player.getUniqueId().toString()).equals(InstituteNames.INFO.name)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE,
                    Main.config.getInt("info.params.FireResistAmplifier"), false, false));
        }
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent e) {
        ArmorEquipEvent.EquipMethod method = e.getMethod();
        SetMaster.CheckSets(e.getPlayer());
    }

    @EventHandler
    public void onLingeringPotion(LingeringPotionSplashEvent e) {
        if (UmcpItem.DIRTY_BOMB.check(e.getEntity().getItem())) {
            Location loc = e.getEntity().getLocation();
            AreaEffectCloud dirtyCloud = (AreaEffectCloud) e.getEntity().getWorld().spawnEntity(loc, EntityType.AREA_EFFECT_CLOUD);
            dirtyCloud.setColor(Color.fromRGB(
                    uraleninParams.getInt("bomb.Color.R"),
                    uraleninParams.getInt("bomb.Color.G"),
                    uraleninParams.getInt("bomb.Color.B")));
            dirtyCloud.setRadius(uraleninParams.getInt("bomb.Radius"));
            dirtyCloud.setDuration(uraleninParams.getInt("bomb.Duration"));
            dirtyCloud.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, 0, 0), false);
            dirtyCloud.setMetadata("isDirtyCloud", new FixedMetadataValue(plugin, true));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        if (Main.conn.GetInstitute(player.getUniqueId().toString()).equals(InstituteNames.INFO.name)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE,
                    Main.config.getInt("info.params.FireResistAmplifier"), false, false));
        }
    }

    @EventHandler
    public void onEffectChangeOrRemove(EntityPotionEffectEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getAction().equals(EntityPotionEffectEvent.Action.ADDED)) {
                return;
            }
            Player player = (Player) e.getEntity();
            PotionEffectType effect = e.getModifiedType();
            if (effect.equals(PotionEffectType.FIRE_RESISTANCE) && Main.conn.GetInstitute(player.getUniqueId().toString()).equals(InstituteNames.INFO.name) &&
                    e.getOldEffect().getDuration() > 3600000) {
                e.setCancelled(true);
            }
            if (SetMaster.getEffects(player).contains(effect) && e.getOldEffect().getDuration() > 3600000) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onAnvil(PrepareAnvilEvent e) {
        Player player = (Player) e.getView().getPlayer();
        ItemStack item = e.getInventory().getItem(1);
        if (item != null && item.getType().equals(Material.ENCHANTED_BOOK) &&
                Main.conn.GetInstitute(player.getUniqueId().toString()).equals(InstituteNames.UGI.name)) {
            e.getInventory().setRepairCost(1);
        }
    }

    @EventHandler
    public void onAnvilClick(InventoryClickEvent e) {
        if (!e.getInventory().getType().equals(InventoryType.ANVIL)) {
            return;
        }
        if (e.getSlotType().equals(InventoryType.SlotType.RESULT)) {
            ItemStack secondItem = e.getInventory().getItem(1);
            ItemStack result = e.getInventory().getItem(2);
            Player player = (Player) e.getView().getPlayer();
            if (result != null && secondItem != null && secondItem.getType().equals(Material.ENCHANTED_BOOK) &&
                    Main.conn.GetInstitute(player.getUniqueId().toString()).equals(InstituteNames.UGI.name)) {
                player.giveExpLevels(1);
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Player player = (Player) e.getEntity().getShooter();
            if (e.getEntity() instanceof Snowball &&
                    UmcpItem.BOOK.check(player.getInventory().getItemInMainHand())) {
                if (Main.conn.GetInstitute(player.getUniqueId().toString()).equals(InstituteNames.UGI.name)) {
                    e.getEntity().setMetadata("book", new FixedMetadataValue(plugin, true));
                }
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Snowball &&
                e.getEntity().hasMetadata("book") &&
                e.getEntity().getMetadata("book").get(0).asBoolean()) {
            Entity target = e.getHitEntity();
            if (target instanceof Player) {
                Player player = (Player) target;
                player.damage(40);
            }
            if (target instanceof Mob) {
                Mob mob = (Mob) target;
                mob.damage(40);
            }
        }
    }
}
