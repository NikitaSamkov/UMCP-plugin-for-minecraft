package org.umc.umcp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

import org.bukkit.entity.Player;
import org.umc.umcp.connection.DBConnection;

@FunctionalInterface
interface InstituteSubcommand{
    public Boolean subcommand(CommandSender cs, Command c, String s, String[] sa);
}

public class InstituteTabExecutor implements TabExecutor {

    private final DBConnection conn;
    private final List<String> institutes;
    private final Map<String, InstituteSubcommand> subcommands;

    public InstituteTabExecutor() {
        conn = new DBConnection("jdbc:mysql://umcraft.scalacubes.org:2163/UMCraft", "root", "4o168PPYSIdyjFU");
        institutes = GetInstitutes();
        subcommands = new HashMap<String, InstituteSubcommand>();
        subcommands.put("join", this::Join);
        subcommands.put("info", this::Info);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1)
            return false;
        return subcommands.get(args[0].toLowerCase(Locale.ROOT)).subcommand(sender, command, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(subcommands.keySet());
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("join")) {
            return institutes;
        }
        return null;
    }

    private boolean Join(CommandSender sender, Command command, String label, String[] args) {
        String instituteName = args[1];
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

    private boolean Info(CommandSender sender, Command command, String label, String[] args) {
        try {
            Player player = (Player) sender;
            conn.Connect();
            ResultSet result = conn.MakeQuery(String.format("select id, name from institutes " +
                    "inner join players p on institutes.id = p.institute where p.uuid='%s'", player.getUniqueId()));
            if (result.next()) {
                sender.sendMessage(String.format("Вы сейчас состоите в институте %s!", result.getString("name")));
            } else {
                sender.sendMessage("Вы сейчас не состоите ни в каком институте, поступите в него командой \"/institute join <название института>\"!");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean JoinInstitute(String uuid, String instituteName) {
        try {
            conn.Connect();
            ResultSet instituteID = conn.MakeQuery(String.format("select id from institutes where name='%s'", instituteName));
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


