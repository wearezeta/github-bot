package com.wire.bots.github;

import com.wire.bots.sdk.Configuration;

import java.sql.*;
import java.util.UUID;

public class Database {
    private final UUID botId;
    private final Configuration.DB conf;

    public Database(String botId, Configuration.DB conf) {
        this.botId = UUID.fromString(botId);
        this.conf = conf;
    }

    public boolean insertSecret(String secret) throws Exception {
        try (Connection c = newConnection()) {
            PreparedStatement stmt = c.prepareStatement("INSERT INTO GitHub (botId, secret) VALUES (?, ?) ON CONFLICT (botId) DO NOTHING");
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

    private Connection newConnection() throws SQLException {
        String url = String.format("jdbc:postgresql://%s:%d/%s", conf.host, conf.port, conf.database);
        return DriverManager.getConnection(url, conf.user, conf.password);
    }
}
