package ru.maga.music;

import java.sql.*;

public final class MusicStoreApp {
    private static final String URL = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/music_store");
    private static final String USER = System.getenv().getOrDefault("DB_USER", "postgres");
    private static final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "postgres");

    public static void main(String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            printShortestTracks(connection);
            long inserted = insertTrack(connection, firstAlbumId(connection), "Demo Track", 120);
            updateTrack(connection, inserted, "Updated Demo Track", 135);
            printTrack(connection, inserted);
            deleteTrack(connection, inserted);
            System.out.println("Deleted: " + inserted);
        }
    }

    private static long firstAlbumId(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT id FROM albums ORDER BY id LIMIT 1");
             ResultSet result = statement.executeQuery()) {
            if (!result.next()) throw new SQLException("No albums");
            return result.getLong(1);
        }
    }

    public static void printShortestTracks(Connection connection) throws SQLException {
        String sql = """
            SELECT a.title AS album, t.title AS shortest_track, t.duration_seconds
            FROM albums a
            JOIN LATERAL (
                SELECT title, duration_seconds
                FROM tracks
                WHERE album_id = a.id
                ORDER BY duration_seconds, title
                LIMIT 1
            ) t ON TRUE
            WHERE (SELECT COUNT(*) FROM tracks x WHERE x.album_id = a.id) >= 5
            ORDER BY a.title
            """;
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                System.out.printf("%s | %s | %d%n", result.getString("album"), result.getString("shortest_track"), result.getInt("duration_seconds"));
            }
        }
    }

    public static long insertTrack(Connection connection, long albumId, String title, int duration) throws SQLException {
        String sql = "INSERT INTO tracks(album_id, title, duration_seconds) VALUES (?, ?, ?) RETURNING id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, albumId);
            statement.setString(2, title);
            statement.setInt(3, duration);
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                return result.getLong(1);
            }
        }
    }

    public static void updateTrack(Connection connection, long id, String title, int duration) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE tracks SET title = ?, duration_seconds = ? WHERE id = ?")) {
            statement.setString(1, title);
            statement.setInt(2, duration);
            statement.setLong(3, id);
            if (statement.executeUpdate() != 1) throw new SQLException("Track not found: " + id);
        }
    }

    public static void printTrack(Connection connection, long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT title, duration_seconds FROM tracks WHERE id = ?")) {
            statement.setLong(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) System.out.println(result.getString(1) + " | " + result.getInt(2));
            }
        }
    }

    public static void deleteTrack(Connection connection, long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM tracks WHERE id = ?")) {
            statement.setLong(1, id);
            if (statement.executeUpdate() != 1) throw new SQLException("Track not found: " + id);
        }
    }
}
