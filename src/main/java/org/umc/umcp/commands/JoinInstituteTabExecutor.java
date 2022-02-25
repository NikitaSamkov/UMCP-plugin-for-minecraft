package org.umc.umcp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.umc.umcp.connection.DBConnection;

public class JoinInstituteTabExecutor implements TabExecutor {

    private List<String> institutes;

    public JoinInstituteTabExecutor() {
        institutes = GetInstitutes();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String instituteName = args[0];
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        player.getUniqueId();
        if (JoinInstitute(player.getUniqueId().toString(), instituteName)) {
            sender.sendMessage("Успешно сменен институт на " + instituteName + "!");
            return true;
        } else {
            sender.sendMessage("Произошла ошибка: проверьте правильность написания названия института (даже подсказки есть)");
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return institutes;
        }
        return null;
    }

    private boolean JoinInstitute(String uuid, String instituteName) {
        DBConnection conn = new DBConnection("jdbc:mysql://umcraft.scalacubes.org:2163/UMCraft", "root", "4o168PPYSIdyjFU");
        conn.Connect();
        ResultSet instituteID = conn.MakeQuery(String.format("select id from institutes where name='%s'", instituteName));
        try {
            if (!instituteID.next()) {
                return false;
            }
            int iid = instituteID.getInt("id");
            instituteID.close();
            ResultSet players = conn.MakeQuery(String.format("select * from players where uuid='%s'", uuid));
            SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = fdate.format(new Date());
            if (players.next()) {
                conn.MakeUpdate(String.format("update players set institute=%d, last_change='%s' where uuid='%s'", iid, date, uuid));
            } else {
                conn.MakeUpdate(String.format("insert into players values ('%s', %d, '%s')", uuid, iid, date));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.Close();
        return false;
    }

    private List<String> GetInstitutes() {
        List<String> result = new ArrayList<String>();

        DBConnection conn = new DBConnection("jdbc:mysql://umcraft.scalacubes.org:2163/UMCraft", "root", "4o168PPYSIdyjFU");
        conn.Connect();
        try {
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
