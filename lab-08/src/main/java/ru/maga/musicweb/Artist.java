package ru.maga.musicweb;

import jakarta.persistence.*;

@Entity
@Table(name = "artists")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "music_sequence")
    @SequenceGenerator(name = "music_sequence", sequenceName = "music_id_seq", allocationSize = 1)
    private Long id;

    public Long getId() { return id; }

    @Column(nullable = false, unique = true, length = 200)
    private String name;

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }
}
