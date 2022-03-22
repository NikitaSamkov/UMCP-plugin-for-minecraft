package org.umc.umcp.listeners;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.umc.umcp.misc.Cooldowns;
import org.umc.umcp.Main;
import org.umc.umcp.armorset.ArmorEquipEvent.ArmorEquipEvent;
import org.umc.umcp.armorset.SetMaster;
import org.umc.umcp.enums.CooldownType;
import org.umc.umcp.enums.InstitutesNames;
import org.umc.umcp.enums.UmcpItem;

import java.util.*;

public class GlobalListener implements Listener {
    private Plugin plugin;
    private final ConfigurationSection rtfParams;

    public GlobalListener(Plugin plugin) {
        this.plugin = plugin;
        rtfParams = Main.config.getConfigurationSection("rtf.params");
    }


    @EventHandler
    public void onPotionMake(BrewEvent e) {
        for (int i = 0; i < 3; i++) {
            ItemStack item = e.getContents().getItem(i);
            if (item == null) {
                continue;
            }
            if (UmcpItem.VAPE.check(item)) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onItemUse(PlayerItemConsumeEvent e) {
        ItemStack item = e.getItem();
        if (UmcpItem.VAPE.check(item)) {
            Player player = e.getPlayer();

            if (Main.conn.GetInstitute(player.getUniqueId().toString()).equals(InstitutesNames.RTF.name)) {
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
            steam.setParticle(Particle.CLOUD);
            steam.setMetadata("isVapeSteam", new FixedMetadataValue(plugin, true));
            steam.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1, 0), false);
            //</editor-fold>
            if (!Cooldowns.UpdateWithDiff(player.getUniqueId(), CooldownType.VAPE)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
                        rtfParams.getInt("VapePenaltyDuration"), rtfParams.getInt("VapePenaltyAmplifier")));
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
                    if (!Objects.equals(institute, InstitutesNames.RTF.name)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
                                rtfParams.getInt("SteamBlindnessDuration"),
                                rtfParams.getInt("SteamBlindnessAmplifier")));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        SetMaster.CheckSets(e.getPlayer());
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent e) {
        ArmorEquipEvent.EquipMethod method = e.getMethod();
        SetMaster.CheckSets(e.getPlayer());
    }
}
