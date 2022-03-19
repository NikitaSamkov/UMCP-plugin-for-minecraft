package org.umc.umcp.armorset;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
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
                List<UmcpArmorSet> added = new ArrayList<>();
                for (UmcpArmorSet set: sets) {
                    if (set.CheckSet(player)) {
                        added.add(set);
                    }
                }
                if (added.size() == 0) {
                    RemoveAllSets(player);
                }
                for (UmcpArmorSet set: added) {
                    AddSetEffect(player, set);
                }
            }
        }, 1);
    }
}
