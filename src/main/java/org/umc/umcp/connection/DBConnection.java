package org.umc.umcp.connection;

import org.bukkit.entity.Player;
import org.umc.umcp.Main;
import org.umc.umcp.enums.InstituteNames;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.sql.Driver;

public class DBConnection {
    private final String url;
    private final String login;
    private final String pass;

    private Connection conn;
    private Statement stmt;
    private Map<String, Map<String, String>> institutes;

    public DBConnection(String url, String login, String password){
        this.url = url == null ? "" : url;
        this.login = login == null ? "" : login;
        this.pass = password == null ? "" : password;
        SetInstitutes();
    }

    public void SetInstitutes() {
        institutes = new HashMap<>();
        institutes.put("ИЕНиМ", new HashMap<>());
        institutes.get("ИЕНиМ").put("description", "1. В них умещается так много профессий, что они способны на каждом инструменте и оружии преодолевать максимальный уровень зачарования одной(подчеркнуто) чары на +1.\n" +
                "Например, эффективность V -> эффективность VI\n" +
                "Но нельзя, например, получить на одной кирке и Эффективность VI и удачу IV. Только что-то одно.\n" +
                "2. Из-за этой способности их тело и их инструмент связываются. Такой инструмент нельзя выбросить или положить в сундук, также такой инструмент не выпадает после смерти, он исчезает.");
        institutes.get("ИЕНиМ").put("permission", "ienim");

        institutes.put("ИНМиТ", new HashMap<>());
        institutes.get("ИНМиТ").put("description", "1. Возле здания ХТИ могут купить волшебное пиво, которое добавляет им 5 дополнительных сердец. Действует столько же, сколько и зачарованное золотое яблоко. Другие институты тоже могут купить это пиво, но они ничего, кроме отравления, не получат.\n" +
                "2. Арийские предки оставили ИНМиТу свои технологии по изготовлению оружия. Они могут создавать оружия уже с готовыми чарами.");
        institutes.get("ИНМиТ").put("permission", "inmit");

        institutes.put("ИРИТ-РТФ", new HashMap<>());
        institutes.get("ИРИТ-РТФ").put("description", "1. Имеют доступ к крафту электронных сигарет. При курении ослепляют окружающих не-радистов на 10 секунд. Также на радиста накладывается эффект \"Сила 1\" на 30 секунд. При слишком частом использовании могут получить эффект отравления.\n" +
                "2. Имеют доступ к крафту кошачьих гольф с лапками. Защищают как золотые поножи, ломаются так же быстро. При использовании радисту накладывается эффект \"Скорость II\"\n" +
                "3. Имеют доступ к крафту кошачьих ушек. Эффектов никаких. Защищает как золотой шлем, ломается так же быстро.");
        institutes.get("ИРИТ-РТФ").put("permission", "rtf");

        institutes.put("ИСА", new HashMap<>());
        institutes.get("ИСА").put("description", "1. Имеют договоренности с производителями стройматериалов и могут вызывать раз в два неигровых дня /kit stroika, в котором будут содержаться полезные для строительства ресурсы.\n" +
                "2. В своем институте могут купить Левиафан - топор с уникальными чарами. Предмет исчезает при смерти, нельзя убрать из инвентаря.");
        institutes.get("ИСА").put("permission", "isa");

        institutes.put("ИФКСиМП", new HashMap<>());
        institutes.get("ИФКСиМП").put("description", "1. Сразу после попадания в институт, игрок получает два дополнительных сердца.\n" +
                "2. Есть уникальный для института крафт спортивок. Это сет брони, который обладает такими уровнями защиты и прочности, что и алмазная броня. Выглядит как спортивная форма. При ношении полного сета, игрок получает эффекты \"Сила I\", \"Скорость I\".");
        institutes.get("ИФКСиМП").put("permission", "ifksimp");

        institutes.put("ИНФО", new HashMap<>());
        institutes.get("ИНФО").put("description", "1. Сразу после попадания в институт, игрок получает эффект огнестойкости.\n" +
                "2. Так как своего здания не имеет, располагается в подвале бара KillFish, в котором имеет доступ к покупке уникальных зелий.");
        institutes.get("ИНФО").put("permission", "info");

        institutes.put("ХТИ", new HashMap<>());
        institutes.get("ХТИ").put("description", "1. Могут сами создавать уникальные зелья, у которых эффект на +1 лучше, чем те зелья, которые доступны другим институтам (Кроме ИНФО). Например, могут сделать \"Силу III\", \"Мгновенный урон III\".\n" +
                "2. Могут делать пиво.\n" +
                "3. Их золотые яблоки действуют дольше обычного, а золотая морковь дает больше насыщенности.");
        institutes.get("ХТИ").put("permission", "hti");

        institutes.put("УГИ", new HashMap<>());
        institutes.get("УГИ").put("description", "1. На зачарование предметов в кузнице с использованием книг не тратится опыт.\n" +
                "2. Могут создавать зачарованные книги.\n" +
                "3. Могут создавать книги, которые можно кидать как снежки и они будут наносить урон.");
        institutes.get("УГИ").put("permission", "ugi");

        institutes.put("ИНЭУ", new HashMap<>());
        institutes.get("ИНЭУ").put("description", "1. В своем институте могут продавать алмазы вдвое дороже, чем на спавне.\n" +
                "2. При обмене с жителями получают очень выгодные условия обмена. (Скидки)");
        institutes.get("ИНЭУ").put("permission", "ineu");

        institutes.put("УралЭНИН", new HashMap<>());
        institutes.get("УралЭНИН").put("description", "1. Имеют доступ к крафту энергетиков - напитков, которые на минуту дают скорость II и восстановление II. При употреблении напитка чаще, чем 3 раза в 10 минут наступает смерть.\n" +
                "2. После инцидента в лифте, получили возможность делать уникальные бомбочки едкого газа, которые отравляют окружающих игроков.");
        institutes.get("УралЭНИН").put("permission", "uralenin");

        institutes.put("ФТИ", new HashMap<>());
        institutes.get("ФТИ").put("description", "1. Из-за отсутствия женщин они в злости много били по стене и их руки стали настолько сильными, что сразу после вступления в институт они получают эффект \"Силы I\".\n" +
                "2. Научились управлять погодой. Могут создать такой лук, который при попадании может бить молнией игрока. Ломается за три использования.");
        institutes.get("ФТИ").put("permission", "fti");
    }

//    public void Connect() {
//        try {
//            Class.forName("java.sql.Driver");
//            conn = DriverManager.getConnection(url, login, pass);
//            stmt = conn.createStatement();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void Close() {
//        try {
//            stmt.close();
//            conn.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public ResultSet MakeQuery(String query) {
//        try {
//            ResultSet result = stmt.executeQuery(query);
//            return result;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public void MakeUpdate(String query) {
//        try {
//            stmt.executeUpdate(query);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    public String GetInstitute(String uuid) {
//        try {
//            this.Connect();
//            ResultSet result = this.MakeQuery(String.format("select name from institutes " +
//                    "inner join players p on institutes.id = p.institute where p.uuid='%s'", uuid));
//            String name = InstituteNames.NONE.name;
//            if (result.next()) {
//                name = result.getString("name");
//            }
//            result.close();
//            this.Close();
//            return name;
//        } catch (SQLException exception) {
//            exception.printStackTrace();
//        }
//        return InstituteNames.NONE.name;
//    }

    public String GetInstitute(Player player) {
        for (String iname : institutes.keySet()) {
            if (player.hasPermission(String.format("group.%s", institutes.get(iname).get("permission")))) {
                return iname;
            }
        }
        return null;
    }

    public Map<String, Map<String, String>> GetInstitutes() {
        return institutes;
//        Map<String, Map<String, String>> ins = new HashMap<>();
//        try {
//            this.Connect();
//            ResultSet rs = this.MakeQuery("select * from institutes");
//            while (rs.next()) {
//                String name = rs.getString("name");
//                ins.put(name, new HashMap<>());
//                ins.get(name).put("description", rs.getString("description"));
//            }
//            this.Close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return ins;
    }
}
