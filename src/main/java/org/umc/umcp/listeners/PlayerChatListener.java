package org.umc.umcp.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.umc.umcp.connection.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerChatListener implements Listener {
    private DBConnection conn;

    public PlayerChatListener() {
        conn = new DBConnection("jdbc:mysql://umcraft.scalacubes.org:2163/UMCraft", "root", "4o168PPYSIdyjFU");
    }

    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String instituteName = GetInstitute(player.getUniqueId().toString());
        if (instituteName == null) {
            instituteName = "не поступил";
        }
        e.setFormat(String.format("[%s] %s", instituteName, e.getFormat()));
    }

    private String GetInstitute(String uuid) {
        try {
            conn.Connect();
            ResultSet result = conn.MakeQuery(String.format("select name from institutes " +
                    "inner join players p on institutes.id = p.institute where p.uuid='%s'", uuid));
            String name = null;
            if (result.next()) {
                name = result.getString("name");
            }
            result.close();
            conn.Close();
            return name;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
