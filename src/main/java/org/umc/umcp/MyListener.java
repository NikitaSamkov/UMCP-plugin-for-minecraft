package org.umc.umcp;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MyListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        String name = e.getPlayer().getName();
        e.getPlayer().sendMessage(String.format("Здравствуй, %s, добро пожаловать на самый лучший сервер - %s", name, Bukkit.getServer().getName()));
    }
}
