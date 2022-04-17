package org.umc.umcp.listeners;

import org.bukkit.configuration.ConfigurationSection;
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
import org.umc.umcp.enums.InstituteNames;
import org.umc.umcp.enums.UmcpItem;

import java.util.List;
import java.util.Map;

public class IENIMListener implements Listener {
    ConfigurationSection ienimMessages = Main.config.getConfigurationSection("ienim.messages");
    ConfigurationSection isaMessages = Main.config.getConfigurationSection("isa.messages");


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

    private Boolean CheckFor(ItemStack item) {
        return (EnchantmentOverload(item) ||
                UmcpItem.LEVIATHAN.check(item));
    }

    private ConfigurationSection GetMessages(Player player) {
        String institute = Main.conn.GetInstitute(player.getUniqueId().toString());
        if (institute.equals(InstituteNames.IENIM.name)) {
            return ienimMessages;
        }
        if (institute.equals(InstituteNames.ISA.name)) {
            return isaMessages;
        }
        return null;
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        ItemStack drop = e.getItemDrop().getItemStack();
        Player player = e.getPlayer();
        if (CheckFor(drop)) {
            ConfigurationSection messages = GetMessages(player);
            if (messages == null) { return; }
            e.getPlayer().sendMessage(messages.getString("DropMessage"));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getView().getPlayer();

        if (e.getCursor() != null &&
                CheckFor(e.getCursor()) &&
                !(e.getClickedInventory().getType().equals(InventoryType.PLAYER)) ||
                (!(e.getInventory().getType().equals(InventoryType.CRAFTING)) &&
                        e.getClick().isShiftClick() &&
                        e.getCurrentItem() != null &&
                        CheckFor(e.getCurrentItem()))) {

            ConfigurationSection messages = GetMessages(player);
            if (messages == null) { return; }
            player.sendMessage(messages.getString("StoreMessage"));
            e.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.getNewItems().size() > 0 &&
                CheckFor(e.getNewItems().values().iterator().next())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlacingItem(PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();
        if (entity instanceof ItemFrame
                && CheckFor(e.getPlayer().getInventory().getItemInMainHand())) {
            ConfigurationSection messages = GetMessages(e.getPlayer());
            if (messages == null) { return; }
            e.getPlayer().sendMessage(messages.getString("FrameMessage"));
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onStandInteract(PlayerArmorStandManipulateEvent e) {
        if (CheckFor(e.getPlayerItem())) {
            ConfigurationSection messages = GetMessages(e.getPlayer());
            if (messages == null) { return; }
            e.getPlayer().sendMessage(messages.getString("StandMessage"));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        List<ItemStack> items = e.getDrops();
        items.removeIf(this::CheckFor);
    }
}
