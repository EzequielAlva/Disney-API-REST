package com.example.alkemy.disneyworld.entity;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="movie")
@SQLDelete(sql = "UPDATE movie SET deleted=true WHERE id=?")
@Where(clause="deleted=false")
public class MovieEntity {
    @Id
    @GenericGenerator(name="uuid-gen", strategy="org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "uuid-gen")
    @Column(name = "id")
    private String id;
    @Column(name = "image")
    private String image;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "creation_date")
    private LocalDate creationDate;
    @Column(name = "score")
    private Integer score;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "has",
            joinColumns = @JoinColumn(
                    name = "movie_character_id",
                    foreignKey = @ForeignKey(name = "character_movie_id_fk")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "character_movie_id",
                    foreignKey = @ForeignKey(name = "movie_character_id_fk")
            )
    )
    private Set<MovieCharacterEntity> characters = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "belongs",
            joinColumns = @JoinColumn(
                    name = "movie_genre_id",
                    foreignKey = @ForeignKey(name = "genre_movie_id_fk")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "genre_movie_id",
                    foreignKey = @ForeignKey(name = "movie_genre_id_fk")
            )
    )
    private Set<MovieGenreEntity> genres = new HashSet<>();

    @CreationTimestamp
    @Column(name = "record_creation_date", updatable = false)
    private LocalDate recordCreationDate;
    @UpdateTimestamp
    @Column(name = "record_update_date")
    private LocalDate recordUpdateDate;
    private boolean deleted = Boolean.FALSE;

    public void addCharacter(MovieCharacterEntity character){
        if(!getCharacters().contains(character)){
            getCharacters().add(character);
            character.getMovies().add(this);
        }
    }

    public void removeCharacter(MovieCharacterEntity character){
        if (getCharacters().contains(character)){
            character.getMovies().remove(this);
            getCharacters().remove(character);
        }
    }
}
