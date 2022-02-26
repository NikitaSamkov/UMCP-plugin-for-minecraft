package org.umc.umcp.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.umc.umcp.commands.InstituteTabExecutor;
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
        painter.put("не поступил", "§8[не поступил]§f");
    }

    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String instituteName = GetInstitute(player.getUniqueId().toString());
        if (instituteName == null) {
            instituteName = "не поступил";
        }
        e.setFormat(String.format("%s %s", painter.get(instituteName), e.getFormat()));
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
