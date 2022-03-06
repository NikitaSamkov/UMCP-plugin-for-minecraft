package org.umc.umcp.connection;

import java.sql.*;

public class DBConnection {
    private final String url;
    private final String login;
    private final String pass;

    private Connection conn;
    private Statement stmt;

    public DBConnection(String url, String login, String password){
        this.url = url == null ? "" : url;
        this.login = login == null ? "" : login;
        this.pass = password == null ? "" : password;
    }

    public void Connect() {
        try {
            conn = DriverManager.getConnection(url, login, pass);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Close() {
        try {
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet MakeQuery(String query) {
        try {
            ResultSet result = stmt.executeQuery(query);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void MakeUpdate(String query) {
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String GetInstitute(String uuid) {
        try {
            this.Connect();
            ResultSet result = this.MakeQuery(String.format("select name from institutes " +
                    "inner join players p on institutes.id = p.institute where p.uuid='%s'", uuid));
            String name = null;
            if (result.next()) {
                name = result.getString("name");
            }
            result.close();
            this.Close();
            return name;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
