CREATE SEQUENCE IF NOT EXISTS music_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS artists (
    id BIGINT PRIMARY KEY DEFAULT nextval('music_id_seq'),
    name VARCHAR(200) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS albums (
    id BIGINT PRIMARY KEY DEFAULT nextval('music_id_seq'),
    artist_id BIGINT NOT NULL REFERENCES artists(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    genre VARCHAR(100) NOT NULL,
    CONSTRAINT unique_album_per_artist UNIQUE (artist_id, title)
);

CREATE TABLE IF NOT EXISTS tracks (
    id BIGINT PRIMARY KEY DEFAULT nextval('music_id_seq'),
    album_id BIGINT NOT NULL REFERENCES albums(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    duration_seconds INTEGER NOT NULL CHECK (duration_seconds > 0),
    CONSTRAINT unique_track_per_album UNIQUE (album_id, title)
);

CREATE INDEX IF NOT EXISTS idx_albums_artist ON albums(artist_id);
CREATE INDEX IF NOT EXISTS idx_tracks_album ON tracks(album_id);
