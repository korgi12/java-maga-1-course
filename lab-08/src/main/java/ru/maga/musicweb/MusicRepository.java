package ru.maga.musicweb;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public final class MusicRepository {
    public record Suggestion(long id, String label, String extra) { }

    private MusicRepository() { }

    public static List<Artist> artists() {
        try (Session session = HibernateUtil.factory().openSession()) {
            return session.createQuery("from Artist order by name", Artist.class).list();
        }
    }

    public static List<Album> albums() {
        try (Session session = HibernateUtil.factory().openSession()) {
            return session.createQuery("select a from Album a join fetch a.artist order by a.title", Album.class).list();
        }
    }

    public static List<Track> tracks() {
        try (Session session = HibernateUtil.factory().openSession()) {
            return session.createQuery("select t from Track t join fetch t.album a join fetch a.artist order by t.title", Track.class).list();
        }
    }

    public static List<Object[]> artistAlbumReport() {
        try (Session session = HibernateUtil.factory().openSession()) {
            return session.createQuery("select a.name, count(b.id) from Artist a left join Album b on b.artist = a group by a.id, a.name order by a.name", Object[].class).list();
        }
    }

    public static List<Object[]> shortestTrackReport() {
        try (Session session = HibernateUtil.factory().openSession()) {
            return session.createQuery("select a.title, min(t.durationSeconds) from Album a join Track t on t.album = a group by a.id, a.title having count(t.id) >= 5 order by a.title", Object[].class).list();
        }
    }

    public static void save(String type, String action, long id, String title, String genre, long relationId, int duration) {
        try (Session session = HibernateUtil.factory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                switch (type) {
                    case "artists" -> saveArtist(session, action, id, title);
                    case "albums" -> saveAlbum(session, action, id, title, genre, relationId);
                    case "tracks" -> saveTrack(session, action, id, title, duration, relationId);
                    default -> throw new IllegalArgumentException("Unknown section");
                }
                transaction.commit();
            } catch (RuntimeException ex) {
                if (transaction.isActive()) transaction.rollback();
                throw ex;
            }
        }
    }

    private static void saveArtist(Session session, String action, long id, String name) {
        if (action.equals("delete")) {
            Artist artist = session.get(Artist.class, id);
            if (artist == null) throw new IllegalArgumentException("Unknown artist");
            session.createQuery("from Track t where t.album.artist.id = :id", Track.class)
                .setParameter("id", id).list().forEach(session::remove);
            session.createQuery("from Album a where a.artist.id = :id", Album.class)
                .setParameter("id", id).list().forEach(session::remove);
            session.remove(artist);
            return;
        }
        Artist artist = action.equals("create") ? new Artist() : session.get(Artist.class, id);
        if (artist == null) throw new IllegalArgumentException("Unknown artist");
        artist.setName(required(name));
        if (action.equals("create")) session.persist(artist);
    }

    private static void saveAlbum(Session session, String action, long id, String title, String genre, long artistId) {
        if (action.equals("delete")) {
            Album album = session.get(Album.class, id);
            if (album == null) throw new IllegalArgumentException("Unknown album");
            session.createQuery("from Track t where t.album.id = :id", Track.class)
                .setParameter("id", id).list().forEach(session::remove);
            session.remove(album);
            return;
        }
        Artist artist = session.get(Artist.class, artistId);
        if (artist == null) throw new IllegalArgumentException("Unknown artist");
        Album album = action.equals("create") ? new Album() : session.get(Album.class, id);
        if (album == null) throw new IllegalArgumentException("Unknown album");
        album.setArtist(artist);
        album.setTitle(required(title));
        album.setGenre(required(genre));
        if (action.equals("create")) session.persist(album);
    }

    private static void saveTrack(Session session, String action, long id, String title, int duration, long albumId) {
        if (action.equals("delete")) {
            Track track = session.get(Track.class, id);
            if (track == null) throw new IllegalArgumentException("Unknown track");
            session.remove(track);
            return;
        }
        Album album = session.get(Album.class, albumId);
        if (album == null) throw new IllegalArgumentException("Unknown album");
        if (duration <= 0) throw new IllegalArgumentException("Invalid duration");
        Track track = action.equals("create") ? new Track() : session.get(Track.class, id);
        if (track == null) throw new IllegalArgumentException("Unknown track");
        track.setAlbum(album);
        track.setTitle(required(title));
        track.setDurationSeconds(duration);
        if (action.equals("create")) session.persist(track);
    }

    private static String required(String text) {
        if (text == null || text.isBlank()) throw new IllegalArgumentException("Empty value");
        return text.trim();
    }

    public static List<Suggestion> suggestions(String type, String search) {
        String query = switch (type) {
            case "artists" -> "select a.id, a.name, a.name from Artist a where lower(a.name) like :q order by a.name";
            case "albums" -> "select a.id, a.title, a.genre from Album a where lower(a.title) like :q order by a.title";
            case "tracks" -> "select t.id, t.title, t.durationSeconds from Track t where lower(t.title) like :q order by t.title";
            default -> throw new IllegalArgumentException("Unknown type");
        };
        try (Session session = HibernateUtil.factory().openSession()) {
            return session.createQuery(query, Object[].class)
                .setParameter("q", "%" + search.toLowerCase().replace("%", "\\%").replace("_", "\\_") + "%")
                .setMaxResults(10).list().stream()
                .map(row -> new Suggestion(((Number) row[0]).longValue(), String.valueOf(row[1]), String.valueOf(row[2])))
                .toList();
        }
    }
}
