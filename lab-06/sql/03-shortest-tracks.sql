SELECT a.title AS album, t.title AS shortest_track, t.duration_seconds
FROM albums a
JOIN LATERAL (
    SELECT t.title, t.duration_seconds
    FROM tracks t
    WHERE t.album_id = a.id
    ORDER BY t.duration_seconds, t.title
    LIMIT 1
) t ON TRUE
WHERE (SELECT COUNT(*) FROM tracks x WHERE x.album_id = a.id) >= 5
ORDER BY a.title;
