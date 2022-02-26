package org.umc.umcp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.umc.umcp.commands.help.Help;
import org.umc.umcp.commands.help.HelpSupport;
import org.umc.umcp.connection.DBConnection;

public class InstituteTabExecutor extends HelpSupport {

    private final DBConnection conn;
    private final List<String> institutes;
    private final Map<String, String> painter;
    private UmcpCommand commandTree;
    private Help helper;

    private int currentArgCount;
    private List<String> currentTabComplete;

    public InstituteTabExecutor() {
        conn = new DBConnection("jdbc:mysql://umcraft.scalacubes.org:2163/UMCraft", "root", "4o168PPYSIdyjFU");
        commandTree = GetTree();
        institutes = commandTree.GetSubcommand("join").GetArguments();
        painter = CreatePainter(institutes);
        helper = new Help(commandTree);

        currentArgCount = 0;
        currentTabComplete = commandTree.GetSubcommands();
        currentTabComplete.add("help");

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1 || args[args.length - 1].equalsIgnoreCase("help"))
            return helper.GetHelp(sender, command, label, args);
        UmcpCommand curr = commandTree;
        for (int i = 0; i < args.length; i++) {
            UmcpCommand next = curr.GetSubcommand(args[i]);
            if (next == null) {
                return curr.Execute(sender, command, label, Arrays.copyOfRange(args, i, args.length));
            }
            curr = next;
        }
        return curr.Execute(sender, command, label, new String[0]);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (currentArgCount == args.length)
            return currentTabComplete;

        currentArgCount = args.length;
        List<String> path = new LinkedList<>(Arrays.asList(args));
        if (path.size() > 0)
            path.remove(path.size() - 1);

        UmcpCommand comm = commandTree.GetSubcommand(path);

        if (comm == null)
            return new ArrayList<>();

        List<String> subs = comm.GetSubcommands();
        subs.addAll(comm.GetArguments());
        subs.add("help");
        currentTabComplete = subs;
        return subs;
    }

    private List<String> GetInstitutes() {
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

    protected UmcpCommand GetTree() {
        List<String> institutes = GetInstitutes();
        return new UmcpCommand("institute", this::NoCommand,
                "База для команд поступления в один из институтов", new LinkedList<>(Arrays.asList(
                new UmcpCommand("join", this::Join, "Поступить в один из институтов", null, institutes),
                new UmcpCommand("info", this::Info, "Узнать, в каком институте сейчас учишься"),
                new UmcpCommand("list", this::InstitutesList, "Посмотреть список всех институтов")
        )));
    }

    private boolean InstitutesList(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(painter.values().stream().sorted().collect(Collectors.joining("\n")));
        return true;
    }

    private boolean Join(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0)
            return false;
        if (!(sender instanceof Player)) return false;

        String instituteName = args[0];
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

    private boolean NoCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
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
}


