package ru.maga.musicweb;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public final class ResetDatabase {
    public static void main(String[] args) throws Exception {
        String url = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/music_store");
        String user = System.getenv().getOrDefault("DB_USER", "postgres");
        String password = System.getenv().getOrDefault("DB_PASSWORD", "postgres");
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS tracks CASCADE");
            statement.execute("DROP TABLE IF EXISTS albums CASCADE");
            statement.execute("DROP TABLE IF EXISTS artists CASCADE");
            statement.execute("DROP SEQUENCE IF EXISTS music_id_seq CASCADE");
        }

        Configuration configuration = new Configuration().configure("hibernate.cfg.xml")
            .addAnnotatedClass(Artist.class)
            .addAnnotatedClass(Album.class)
            .addAnnotatedClass(Track.class);
        configuration.setProperty("hibernate.connection.url", url);
        configuration.setProperty("hibernate.connection.username", user);
        configuration.setProperty("hibernate.connection.password", password);
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        try (SessionFactory factory = configuration.buildSessionFactory()) {
            seed(factory);
        }
    }

    private static void seed(SessionFactory factory) {
        String[] artists = {"Radiohead", "Muse", "Coldplay", "Queen", "Arctic Monkeys"};
        String[] titles = {"OK Computer", "Origin of Symmetry", "Parachutes", "A Night at the Opera", "AM"};
        String[] genres = {"Alternative Rock", "Alternative Rock", "Rock", "Rock", "Indie Rock"};
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                for (int i = 0; i < artists.length; i++) {
                    Artist artist = new Artist();
                    artist.setName(artists[i]);
                    session.persist(artist);
                    Album album = new Album();
                    album.setArtist(artist);
                    album.setTitle(titles[i]);
                    album.setGenre(genres[i]);
                    session.persist(album);
                    for (int j = 1; j <= 5; j++) {
                        Track track = new Track();
                        track.setAlbum(album);
                        track.setTitle("Track " + j);
                        track.setDurationSeconds(140 + 23 * j + i);
                        session.persist(track);
                    }
                }
                transaction.commit();
            } catch (RuntimeException ex) {
                if (transaction.isActive()) transaction.rollback();
                throw ex;
            }
        }
    }
}
