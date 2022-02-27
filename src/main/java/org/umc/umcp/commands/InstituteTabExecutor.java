package org.umc.umcp.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.umc.umcp.commands.help.Help;
import org.umc.umcp.commands.help.HelpSupport;
import org.umc.umcp.connection.DBConnection;

import net.md_5.bungee.api.chat.TextComponent;

public class InstituteTabExecutor extends HelpSupport {

    private final DBConnection conn;
    private final Map<String, String> institutesDescription;
    private final Map<String, String> painter;
    private UmcpCommand commandTree;
    private Help helper;

    private int currentArgCount;
    private List<String> currentTabComplete;

    public InstituteTabExecutor() {
        conn = new DBConnection("jdbc:mysql://umcraft.scalacubes.org:2163/UMCraft", "root", "4o168PPYSIdyjFU");
        institutesDescription = GetInstitutes();
        commandTree = GetTree();
        painter = Painter.GetPainter(new ArrayList<>(institutesDescription.keySet()));
        helper = new Help(commandTree);

        currentArgCount = 0;
        currentTabComplete = commandTree.GetSubcommands();
        currentTabComplete.add("help");

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1)
            return false;
        if (args[args.length - 1].equalsIgnoreCase("help"))
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

//    private List<String> GetInstitutes() {
//        List<String> result = new ArrayList<>();
//
//        try {
//            conn.Connect();
//            ResultSet institutes = conn.MakeQuery("select name from institutes");
//
//            while (institutes.next()) {
//                result.add(institutes.getString("name"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        conn.Close();
//
//        return result;
//    }

    private Map<String, String> GetInstitutes() {
        Map<String, String> desc = new HashMap<>();
        try {
            conn.Connect();
            ResultSet rs = conn.MakeQuery("select name, description from institutes");
            while (rs.next()) {
                desc.put(rs.getString("name"), rs.getString("description"));
            }
            conn.Close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return desc;
    }

    protected UmcpCommand GetTree() {
        List<String> institutesList = new ArrayList<>(institutesDescription.keySet());
        UmcpCommand tree = new UmcpCommand("institute", this::NoCommand,
                "База для команд поступления в один из институтов", new LinkedList<>(Arrays.asList(
                new UmcpCommand("join", this::Join, "Поступить в один из институтов", null, institutesList),
                new UmcpCommand("info", this::Info, "Узнать, об институте", null, institutesList),
                new UmcpCommand("list", this::InstitutesList, "Посмотреть список всех институтов")
        )));
        tree.GetSubcommand("info").arguments.add("me");
        return tree;
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
        if (args.length == 0)
            return false;

        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[0]);

        if (args[0].equalsIgnoreCase("me") || Objects.equals(target, player)) {
            return InfoMe(player);
        }

        if (target != null) {
            return InfoPlayer(player, target);
        }
        //TODO institute info
        return false;
    }

    private String GetPlayerInstitute(UUID uuid) {
        try {
            conn.Connect();
            ResultSet result = conn.MakeQuery(String.format("select id, name from institutes " +
                    "inner join players p on institutes.id = p.institute where p.uuid='%s'", uuid));
            if (result.next()) {
                return result.getString("name");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean InfoMe(Player player) {
        String institute = GetPlayerInstitute(player.getUniqueId());
        TextComponent msg;
        if (institute != null) {
            msg = new TextComponent("Вы сейчас состоите в институте ");
            msg.addExtra(GetInteractiveInstitute(institute));
            msg.addExtra("!");
        } else {
            msg = new TextComponent("Вы сейчас не состоите ни в каком институте, поступите в него командой ");
            TextComponent extra = new TextComponent("\"/institute join <название института>\"");
            extra.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/institute join "));
            extra.setColor(ChatColor.GREEN);
            msg.addExtra(extra);
            msg.addExtra("!");
        }
        player.spigot().sendMessage(msg);
        return true;
    }

    private boolean InfoPlayer(Player player, Player target) {
        String institute = GetPlayerInstitute(target.getUniqueId());

        TextComponent msg = new TextComponent("Игрок ");

        TextComponent targetName = new TextComponent(target.getName());
        targetName.setColor(ChatColor.BLUE);

        msg.addExtra(targetName);
        msg.addExtra(" учится в институте ");
        msg.addExtra(GetInteractiveInstitute(GetPlayerInstitute(target.getUniqueId())));
        msg.addExtra("!");
        player.spigot().sendMessage(msg);
        return true;

    }

    private TextComponent GetInteractiveInstitute(String name) {
        TextComponent institute = new TextComponent(painter.get(name));
        institute.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(institutesDescription.get(name)).create()));
        institute.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/institute info " + name));
        return institute;
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
}


