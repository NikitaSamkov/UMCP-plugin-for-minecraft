package org.umc.umcp.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.umc.umcp.commands.Painter;
import org.umc.umcp.connection.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerChatListener implements Listener {
    private final DBConnection conn;
    private final Map<String, String> painter;

    public PlayerChatListener() {
        conn = new DBConnection("jdbc:mysql://umcraft.scalacubes.org:2163/UMCraft", "root", "4o168PPYSIdyjFU");
        painter = Painter.GetPainter(GetInstitutesList(), null, (String s) -> "["+s+"]");
        painter.put("абитуриент", "§8[абитуриент]§f");
    }

    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String instituteName = conn.GetInstitute(player.getUniqueId().toString());
        if (instituteName == null) {
            instituteName = "абитуриент";
        }
        e.setFormat(String.format("%s %s", painter.get(instituteName), e.getFormat()));
    }



    private List<String> GetInstitutesList() {
        List<String> result = new ArrayList<>();

        try {
            conn.Connect();
            ResultSet institutes = conn.MakeQuery("select name from institutes");

            while (institutes.next()) {
                result.add(institutes.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.Close();

        return result;
    }
}
