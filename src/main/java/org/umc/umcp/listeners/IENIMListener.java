package org.umc.umcp.listeners;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.umc.umcp.Main;
import org.umc.umcp.enums.InstitutesNames;

import java.util.List;
import java.util.Map;

public class IENIMListener implements Listener {
    private @NotNull Boolean EnchantmentOverload(@NotNull ItemStack item) {
        if (item.hasItemMeta() &&
                item.getItemMeta() != null &&
                item.getItemMeta().hasEnchants()) {
            Map<Enchantment, Integer> enchants = item.getItemMeta().getEnchants();
            for (Enchantment ench: enchants.keySet()) {
                if (enchants.get(ench) > ench.getMaxLevel()) {
                    return true;
                }
            }

        }
        return false;
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if (EnchantmentOverload(e.getItemDrop().getItemStack()) &&
                Main.conn.GetInstitute(e.getPlayer().getUniqueId().toString()).equals(InstitutesNames.IENIM.name)) {
            e.getPlayer().sendMessage("Вы не можете выбросить свой инструмент!");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (Main.conn.GetInstitute(e.getView().getPlayer().getUniqueId().toString()).equals(InstitutesNames.IENIM.name)) {
            Player player = (Player) e.getView().getPlayer();
            if (e.getCursor() != null &&
                    EnchantmentOverload(e.getCursor()) &&
                    !(e.getClickedInventory().getType().equals(InventoryType.PLAYER))) {
                player.sendMessage("Вы безуспешно пытаетесь избавиться от своего инструмента");
                e.setCancelled(true);
                return;
            }
            if (!(e.getInventory().getType().equals(InventoryType.CRAFTING)) &&
                    e.getClick().isShiftClick() &&
                    e.getCurrentItem() != null &&
                    EnchantmentOverload(e.getCurrentItem())) {
                player.sendMessage("Вы безуспешно пытаетесь избавиться от своего инструмента");
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (Main.conn.GetInstitute(e.getView().getPlayer().getUniqueId().toString()).equals(InstitutesNames.IENIM.name)) {
            Player player = (Player) e.getView().getPlayer();

            if (e.getNewItems().size() > 0 &&
                    EnchantmentOverload((e.getNewItems().values().iterator().next()))) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPlacingItem(PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();
        if (entity instanceof ItemFrame &&
                Main.conn.GetInstitute(e.getPlayer().getUniqueId().toString()).equals(InstitutesNames.IENIM.name)
                && EnchantmentOverload(e.getPlayer().getInventory().getItemInMainHand())) {
            e.getPlayer().sendMessage("Вы безуспешно пытаетесь избавиться от своего инструмента");
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onStandInteract(PlayerArmorStandManipulateEvent e) {
        if (EnchantmentOverload(e.getPlayerItem()) &&
                Main.conn.GetInstitute(e.getPlayer().getUniqueId().toString()).equals(InstitutesNames.IENIM.name)) {
            e.getPlayer().sendMessage("Вы безуспешно пытаетесь избавиться от своего инструмента");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity().getPlayer();
        if (Main.conn.GetInstitute(player.getUniqueId().toString()).equals(InstitutesNames.IENIM.name)) {
            List<ItemStack> items = e.getDrops();
            items.removeIf(this::EnchantmentOverload);
        }
    }
}
