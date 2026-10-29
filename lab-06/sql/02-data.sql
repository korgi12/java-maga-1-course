INSERT INTO artists(name) VALUES
    ('Radiohead'), ('Muse'), ('Coldplay'), ('Queen'), ('Arctic Monkeys')
ON CONFLICT (name) DO NOTHING;

INSERT INTO albums(artist_id, title, genre)
SELECT a.id, data.title, data.genre
FROM (VALUES
    ('Radiohead', 'OK Computer', 'Alternative Rock'),
    ('Muse', 'Origin of Symmetry', 'Alternative Rock'),
    ('Coldplay', 'Parachutes', 'Rock'),
    ('Queen', 'A Night at the Opera', 'Rock'),
    ('Arctic Monkeys', 'AM', 'Indie Rock')
) AS data(artist_name, title, genre)
JOIN artists a ON a.name = data.artist_name
ON CONFLICT (artist_id, title) DO NOTHING;

INSERT INTO tracks(album_id, title, duration_seconds)
SELECT a.id, 'Track ' || generate_series.i, 140 + generate_series.i * 23 + (a.id % 7)::integer
FROM albums a
CROSS JOIN generate_series(1, 5) AS generate_series(i)
ON CONFLICT (album_id, title) DO NOTHING;
