package org.umc.umcp.misc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.umc.umcp.enums.CooldownType;

import java.util.*;

public class Cooldowns {
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
}
