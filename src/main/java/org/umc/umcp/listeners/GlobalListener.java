package org.umc.umcp.listeners;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.umc.umcp.Cooldowns;
import org.umc.umcp.Crafter;
import org.umc.umcp.Main;
import org.umc.umcp.armorset.ArmorEquipEvent.ArmorEquipEvent;
import org.umc.umcp.armorset.SetMaster;
import org.umc.umcp.enums.CooldownType;
import org.umc.umcp.enums.UmcpItem;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class GlobalListener implements Listener {
    private Plugin plugin;

    public GlobalListener(Plugin plugin) {
        this.plugin = plugin;
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
            Location loc = player.getLocation();
            loc.setY(loc.getY() + 1);
            //<editor-fold desc="Cloud creation">
            AreaEffectCloud steam = (AreaEffectCloud) player.getWorld().spawnEntity(loc, EntityType.AREA_EFFECT_CLOUD);
            steam.setColor(Color.fromRGB(255, 255, 255));
            steam.setRadius(10);
            steam.setSource(player);
            steam.setDuration(1);
            steam.setParticle(Particle.CLOUD);
            steam.setMetadata("isVapeSteam", new FixedMetadataValue(plugin, true));
            steam.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1, 0), false);
            //</editor-fold>
            if (!Cooldowns.UpdateWithDiff(player.getUniqueId(), CooldownType.VAPE)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 1200, 1));
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
                    if (!Objects.equals(institute, "ИРИТ-РТФ")) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 24));
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
