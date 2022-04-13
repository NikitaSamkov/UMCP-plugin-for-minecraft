package org.umc.umcp.armorset;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.umc.umcp.Main;

import java.util.*;

public class SetMaster {
    static List<UmcpArmorSet> sets = new ArrayList<>();
    static Map<UUID, List<UmcpArmorSet>> activeSets = new HashMap<>();
    static Plugin plugin;

    public static void SetPlugin(Plugin plug) {
        plugin = plug;
    }

    public static void AddSet(UmcpArmorSet set) {
        sets.add(set);
    }

    static void AddEffects(Player player, List<PotionEffect> effects) {
        if (effects != null) {
            for (PotionEffect effect : effects) {
                player.addPotionEffect(effect);
            }
        }
    }

    static void RemoveEffects(Player player, List<PotionEffect> effects) {
        if (effects != null) {
            for (PotionEffect effect : effects) {
                player.removePotionEffect(effect.getType());
            }
        }
    }

    static void AddSetEffect(@NotNull Player player, @NotNull UmcpArmorSet set) {
        if (activeSets.containsKey(player.getUniqueId()) && activeSets.get(player.getUniqueId()).contains(set)) {
            return;
        }
        if (set.getInstitute() != null && !set.getInstitute().name.equals(Main.conn.GetInstitute(player.getUniqueId().toString()))) {
            return;
        }
        if (!activeSets.containsKey(player.getUniqueId())) {
            activeSets.put(player.getUniqueId(), new ArrayList<>());
        }
        AddEffects(player, set.GetEffects());
        activeSets.get(player.getUniqueId()).add(set);
    }

    static void RemoveSetEffect(@NotNull Player player, @NotNull UmcpArmorSet set) {
        if (!activeSets.containsKey(player.getUniqueId()) || !activeSets.get(player.getUniqueId()).contains(set)) {
            return;
        }
        RemoveEffects(player, set.GetEffects());
    }

    static void RefreshEffects(@NotNull Player player) {
        if (!activeSets.containsKey(player.getUniqueId())) {
            return;
        }
        for (UmcpArmorSet set: activeSets.get(player.getUniqueId())) {
            AddEffects(player, set.GetEffects());
        }
    }

    public static void RemoveAllSets(Player player) {
        if (!activeSets.containsKey(player.getUniqueId())) {
            return;
        }
        for (UmcpArmorSet set: activeSets.get(player.getUniqueId())) {
            RemoveSetEffect(player, set);
        }
        activeSets.remove(player.getUniqueId());
    }

    public static void CheckSets(Player player) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                List<UmcpArmorSet> added = getSets(player);

                if (added.size() == 0) {
                    RemoveAllSets(player);
                }
                for (UmcpArmorSet set: added) {
                    AddSetEffect(player, set);
                }
            }
        }, 1);
    }

    public static List<UmcpArmorSet> getSets(Player player) {
        List<UmcpArmorSet> result = new ArrayList<>();
        for (UmcpArmorSet set: sets) {
            if (set.CheckSet(player)) {
                result.add(set);
            }
        }
        return result;
    }

    public static List<PotionEffectType> getEffects(Player player) {
        List<UmcpArmorSet> playerSets = getSets(player);
        List<PotionEffectType> result = new ArrayList<>();
        for (UmcpArmorSet armorSet: playerSets) {
            List<PotionEffect> potionEffects = armorSet.GetEffects();
            for (PotionEffect potionEffect: potionEffects) {
                result.add(potionEffect.getType());
            }
        }
        return result;
    }
}
