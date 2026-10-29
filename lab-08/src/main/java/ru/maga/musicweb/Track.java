package ru.maga.musicweb;

import jakarta.persistence.*;

@Entity
@Table(name = "tracks", uniqueConstraints = @UniqueConstraint(columnNames = {"album_id","title"}))
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "music_sequence")
    @SequenceGenerator(name = "music_sequence", sequenceName = "music_id_seq", allocationSize = 1)
    private Long id;

    public Long getId() { return id; }

    @ManyToOne(optional = false)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "duration_seconds", nullable = false)
    private int durationSeconds;

    public Album getAlbum() { return album; }
    public void setAlbum(Album value) { this.album = value; }
    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }
    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int value) { this.durationSeconds = value; }
}
