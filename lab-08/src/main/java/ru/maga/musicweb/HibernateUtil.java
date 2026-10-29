package ru.maga.musicweb;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateUtil {
    private static final SessionFactory FACTORY = configuration().buildSessionFactory();

    private HibernateUtil() { }

    public static Configuration configuration() {
        Configuration config = new Configuration().configure("hibernate.cfg.xml")
            .addAnnotatedClass(Artist.class)
            .addAnnotatedClass(Album.class)
            .addAnnotatedClass(Track.class);
        config.setProperty("hibernate.connection.url", System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/music_store"));
        config.setProperty("hibernate.connection.username", System.getenv().getOrDefault("DB_USER", "postgres"));
        config.setProperty("hibernate.connection.password", System.getenv().getOrDefault("DB_PASSWORD", "postgres"));
        return config;
    }

    public static SessionFactory factory() { return FACTORY; }
}
