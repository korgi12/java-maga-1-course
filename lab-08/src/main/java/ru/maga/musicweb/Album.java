package ru.maga.musicweb;

import jakarta.persistence.*;

@Entity
@Table(name = "albums", uniqueConstraints = @UniqueConstraint(columnNames = {"artist_id","title"}))
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "music_sequence")
    @SequenceGenerator(name = "music_sequence", sequenceName = "music_id_seq", allocationSize = 1)
    private Long id;

    public Long getId() { return id; }

    @ManyToOne(optional = false)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 100)
    private String genre;

    public Artist getArtist() { return artist; }
    public void setArtist(Artist value) { this.artist = value; }
    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }
    public String getGenre() { return genre; }
    public void setGenre(String value) { this.genre = value; }
}
