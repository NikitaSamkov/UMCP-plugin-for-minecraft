package org.umc.umcp.misc;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.umc.umcp.Main;
import org.umc.umcp.enums.CooldownType;

import java.io.File;
import java.util.*;

public class Cooldowns {
    private static File cooldownFile;
    private static FileConfiguration cooldownConfig;
    static Map<UUID, Map<CooldownType, List<Date>>> cooldowns = new HashMap<>();

    public static void Update(UUID uuid, CooldownType type, Date moment) {
        if (!cooldowns.containsKey(uuid)) {
            cooldowns.put(uuid, new HashMap<>());
        }
        if (!cooldowns.get(uuid).containsKey(type)) {
            cooldowns.get(uuid).put(type, new ArrayList<>());
        }
        UpdateStamps(uuid, type, moment);
        cooldowns.get(uuid).get(type).add(moment);
    }

    public static void Update(UUID uuid, CooldownType type) {
        Update(uuid, type, new Date());
    }

    public static @NotNull Boolean UpdateWithDiff(UUID uuid, CooldownType type, Date forMoment) {
        Update(uuid, type, forMoment);
        return IsAcceptableCount(uuid, type);
    }

    public static @NotNull Boolean UpdateWithDiff(UUID uuid, CooldownType type) {
        return UpdateWithDiff(uuid, type, new Date());
    }

    public static @NotNull Boolean IsAcceptableCount(UUID uuid, CooldownType type) {
        if (cooldowns.containsKey(uuid) && cooldowns.get(uuid).containsKey(type)) {
            return cooldowns.get(uuid).get(type).size() <= type.acceptableCount;
        }
        return true;
    }

    public static @NotNull Boolean CanUse(UUID uuid, CooldownType type) {
        if (cooldowns.containsKey(uuid) && cooldowns.get(uuid).containsKey(type)) {
            UpdateStamps(uuid, type, new Date());
            return cooldowns.get(uuid).get(type).size() < type.acceptableCount;
        }
        return true;
    }

    public static void UpdateStamps(UUID uuid, CooldownType type, Date forMoment) {
        List<Date> timestamps = cooldowns.get(uuid).get(type);
        long moment = forMoment.getTime();
        while (timestamps.size() != 0 && moment - timestamps.get(0).getTime() > type.time.getTime()) {
            timestamps.remove(0);
        }
    }

    public static @Nullable List<Date> Get(UUID uuid, CooldownType type) {
        if (cooldowns.containsKey(uuid) && cooldowns.get(uuid).containsKey(type)) {
            return cooldowns.get(uuid).get(type);
        }
        return null;
    }

    public static void Clear(UUID uuid) {
        cooldowns.remove(uuid);
    }

    public static void Clear(UUID uuid, CooldownType type) {
        if (!cooldowns.containsKey(uuid) || !cooldowns.get(uuid).containsKey(type)) {
            return;
        }
        cooldowns.get(uuid).remove(type);
    }

    public static FileConfiguration GetCooldowns(JavaPlugin plugin) {
        cooldownFile = new File(plugin.getDataFolder(), "cooldowns.yml");
        if (!cooldownFile.exists()) {
            try {
                cooldownFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cooldownConfig = YamlConfiguration.loadConfiguration(cooldownFile);
        for (String uuids : cooldownConfig.getKeys(false)) {
            UUID uuid = UUID.fromString(uuids);
            if (!cooldowns.containsKey(uuid)) {
                cooldowns.put(uuid, new HashMap<>());
            }
            for (String types : cooldownConfig.getConfigurationSection(uuids).getKeys(false)) {
                CooldownType type = CooldownType.ByName(types);
                if (type != null) {
                    List<Long> datesl = cooldownConfig.getLongList(String.format("%s.%s", uuids, types));
                    List<Date> dates = new ArrayList<>();
                    for (Long time : datesl) {
                        dates.add(new Date(time));
                    }
                    cooldowns.get(uuid).put(type, dates);
                    Main.log.info(String.format("loaded %s.%s", uuids, types));
                }
            }
        }
        return cooldownConfig;
    }

    public static void SaveCooldowns(JavaPlugin plugin) {
        try {
            cooldownFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cooldownConfig = YamlConfiguration.loadConfiguration(cooldownFile);
        for (UUID uuid : cooldowns.keySet()) {
            for (CooldownType type : cooldowns.get(uuid).keySet()) {
                UpdateStamps(uuid, type, new Date());
                if (cooldowns.get(uuid).get(type).size() != 0) {
                    List<Long> dates = new LinkedList<>();
                    for (Date d : cooldowns.get(uuid).get(type)) {
                        dates.add(d.getTime());
                    }
                    cooldownConfig.set(String.format("%s.%s", uuid.toString(), CooldownType.GetName(type)), dates);
                }
            }
        }
        try {
            cooldownConfig.save(cooldownFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
