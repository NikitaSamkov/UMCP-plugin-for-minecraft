package org.umc.umcp.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.umc.umcp.Main;
import org.umc.umcp.commands.Painter;
import org.umc.umcp.connection.DBConnection;
import org.umc.umcp.enums.InstituteNames;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerChatListener implements Listener {
    private final Map<String, String> painter;

    public PlayerChatListener() {
        painter = Painter.GetPainter(new ArrayList<>(Main.conn.GetInstitutes().keySet()), (String s) -> "["+s+"]");
        painter.put("абитуриент", "§8[абитуриент]§f");
    }

    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String instituteName = Main.conn.GetInstitute(player.getUniqueId().toString());
        if (instituteName.equals(InstituteNames.NONE.name)) {
            instituteName = "абитуриент";
        }
        e.setFormat(String.format("%s %s", painter.get(instituteName), e.getFormat()));
    }
}
