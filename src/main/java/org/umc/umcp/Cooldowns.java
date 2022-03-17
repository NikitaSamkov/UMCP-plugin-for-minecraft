package org.umc.umcp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.umc.umcp.enums.CooldownType;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cooldowns {
    static Map<UUID, Map<CooldownType, Date>> cooldowns = new HashMap<>();

    public static void Update(UUID uuid, CooldownType type, Date moment) {
        if (!cooldowns.containsKey(uuid)) {
            cooldowns.put(uuid, new HashMap<>());
        }
        cooldowns.get(uuid).put(type, moment);
    }

    public static void Update(UUID uuid, CooldownType type) {
        Update(uuid, type, new Date());
    }

    public static Boolean UpdateWithDiff(UUID uuid, CooldownType type, Date cooldownLength, Date forMoment) {
        Boolean ended = IsCooldownEnded(uuid, type, cooldownLength, forMoment);
        Update(uuid, type, new Date());
        return ended;
    }

    public static Boolean UpdateWithDiff(UUID uuid, CooldownType type, Date cooldownLength) {
        return UpdateWithDiff(uuid, type, cooldownLength, new Date());
    }

    public static @Nullable Date GetDifference(UUID uuid, CooldownType type, Date forMoment) {
        if (cooldowns.containsKey(uuid) && cooldowns.get(uuid).containsKey(type)) {
            return new Date(forMoment.getTime() - cooldowns.get(uuid).get(type).getTime());
        }
        return null;
    }

    public static @NotNull Boolean IsCooldownEnded(UUID uuid, CooldownType type, Date cooldownLength, Date forMoment) {
        Date diff = GetDifference(uuid, type, forMoment);
        if (diff != null) {
            return diff.after(cooldownLength);
        }
        return true;
    }

    public static Boolean IsCooldownEnded(UUID uuid, CooldownType type, Date cooldownLength) {
        return IsCooldownEnded(uuid, type, cooldownLength, new Date());
    }

    public static @Nullable Date Get(UUID uuid, CooldownType type) {
        if (cooldowns.containsKey(uuid) && cooldowns.get(uuid).containsKey(type)) {
            return cooldowns.get(uuid).get(type);
        }
        return null;
    }
}
