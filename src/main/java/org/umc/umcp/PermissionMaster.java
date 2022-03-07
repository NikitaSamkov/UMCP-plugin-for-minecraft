package org.umc.umcp;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class PermissionMaster {
    public static HashMap<UUID, PermissionAttachment> perms = new HashMap<>();
    static Plugin plugin;

    public static PermissionAttachment GetPerms(Player player) {
        if (!perms.containsKey(player.getUniqueId())) {
            PermissionAttachment attachment = player.addAttachment(plugin);
            perms.put(player.getUniqueId(), attachment);
        }
        return perms.get(player.getUniqueId());
    }

    public static void SetPermission(Player player, String permission, Boolean value) {
        PermissionAttachment perm = GetPerms(player);
        perm.setPermission(permission, value);
    }

    public static void RemovePermission(Player player, String permission) {
        PermissionAttachment perm = GetPerms(player);
        perm.unsetPermission(permission);
    }

    public static Boolean HavePermissions(UUID uuid) {
        return perms.containsKey(uuid);
    }
}
