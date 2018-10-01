package com.wire.bots.github;

import java.sql.*;
import java.util.UUID;

public class Database {
    private final UUID botId;

    public Database(String botId) {
        this.botId = UUID.fromString(botId);
    }

    public boolean insertSecret(String secret) throws Exception {
        try (Connection c = newConnection()) {
            PreparedStatement stmt = c.prepareStatement("INSERT INTO GitHub (botId, secret) VALUES (?, ?)");
            stmt.setObject(1, botId);
            stmt.setString(2, secret);
            return stmt.executeUpdate() == 1;
        }
    }

    public String getSecret() throws Exception {
        try (Connection c = newConnection()) {
            PreparedStatement stmt = c.prepareStatement("SELECT secret FROM GitHub WHERE botId = ?");
            stmt.setObject(1, botId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("secret");
            }
        }
        return null;
    }

    boolean unsubscribe() throws SQLException {
        try (Connection c = newConnection()) {
            PreparedStatement stmt = c.prepareStatement("DELETE FROM GitHub WHERE botId = ?");
            stmt.setObject(1, botId);
            return stmt.executeUpdate() == 1;
        }
    }

    private Connection newConnection() throws SQLException {
        Config.DB conf = Service.config.postgres;
        String url = String.format("jdbc:%s://%s:%d/%s", conf.driver, conf.host, conf.port, conf.database);
        return DriverManager.getConnection(url, conf.user, conf.password);
    }
}
