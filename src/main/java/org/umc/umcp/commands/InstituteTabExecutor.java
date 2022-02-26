package org.umc.umcp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.umc.umcp.commands.help.Help;
import org.umc.umcp.commands.help.HelpSupport;
import org.umc.umcp.commands.help.UmcpCommand;
import org.umc.umcp.connection.DBConnection;

@FunctionalInterface
interface InstituteSubcommand{
    public Boolean subcommand(CommandSender cs, Command c, String s, String[] sa);
}

public class InstituteTabExecutor extends HelpSupport {

    private final DBConnection conn;
    private final List<String> institutes;
    private final Map<String, InstituteSubcommand> subcommands;
    private final Map<String, String> painter;
    private UmcpCommand commandTree;
    private Help helper;

    public InstituteTabExecutor() {
        conn = new DBConnection("jdbc:mysql://umcraft.scalacubes.org:2163/UMCraft", "root", "4o168PPYSIdyjFU");
        commandTree = GetTree();
        institutes = commandTree.GetSubcommand("join").GetSubcommands();
        painter = CreatePainter(institutes);
        helper = new Help(commandTree);

        subcommands = new HashMap<String, InstituteSubcommand>();
        subcommands.put("join", this::Join);
        subcommands.put("info", this::Info);
        subcommands.put("list", this::InstitutesList);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1)
            return false;
        if (args[args.length - 1].equalsIgnoreCase("help"))
            return helper.GetHelp(sender, command, label, args);
        return subcommands.get(args[0].toLowerCase(Locale.ROOT)).subcommand(sender, command, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> path = new LinkedList<>(Arrays.asList(args));
        if (path.size() > 0)
            path.remove(path.size() - 1);
        List<String> subs = commandTree.GetSubcommands(path);
        subs.add("help");
        return subs;
    }

    private boolean InstitutesList(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(painter.values().stream().sorted().collect(Collectors.joining("\n")));
        return true;
    }

    private boolean Join(CommandSender sender, Command command, String label, String[] args) {
        String instituteName = args[1];
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        player.getUniqueId();
        if (JoinInstitute(player.getUniqueId().toString(), instituteName)) {
            sender.sendMessage("Успешно сменен институт на " + painter.get(instituteName) + "!");
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
                sender.sendMessage(String.format("Вы сейчас состоите в институте %s!", painter.get(result.getString("name"))));
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

    private List<String> GetColorfulElements(List<String> list) {
        List<String> paints = Arrays.asList("2", "3", "4", "5", "6", "7", "9", "a", "b", "c", "d", "e", "f");
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            result.add("§" + paints.get(i % paints.size()) + list.get(i) + "§f");
        }
        return result;
    }

    private Map<String, String> CreatePainter(List<String> list) {
        List<String> colored = GetColorfulElements(list);
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            result.put(list.get(i), colored.get(i));
        }
        return result;
    }

    protected UmcpCommand GetTree() {
        List<String> institutes = GetInstitutes();
        UmcpCommand tree = new UmcpCommand("institute", "База для команд поступления в один из институтов", Arrays.asList(
                new UmcpCommand("join", "Поступить в один из институтов", null),
                new UmcpCommand("info", "Узнать, в каком институте сейчас учишься", null),
                new UmcpCommand("list", "Посмотреть список всех институтов", null)
        ));
        for (String i: institutes)
            tree.GetSubcommand("join").subcommands.add(new UmcpCommand(i, "", null));
        return tree;
    }
}


