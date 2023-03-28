package com.example.alkemy.disneyworld.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="movie_character")
@SQLDelete(sql = "UPDATE movie_character SET deleted=true WHERE id=?")
@Where(clause="deleted=false")
public class MovieCharacterEntity {
    @Id
    @GenericGenerator(name="uuid-gen", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "uuid-gen")
    @Column(name = "id")
    private String id;
    @Column(name = "image")
    private String image;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "age")
    private Integer age;
    @Column(name = "weight")
    private Integer weight;
    @Column(name = "story", columnDefinition = "TEXT")
    private String story;

    @ManyToMany(mappedBy = "characters")
    private Set<MovieEntity> movies = new HashSet<>();

    @CreationTimestamp
    @Column(name = "record_creation_date", updatable = false)
    private LocalDate recordCreationDate;
    @UpdateTimestamp
    @Column(name = "record_update_date")
    private LocalDate recordUpdateDate;
    private boolean deleted = Boolean.FALSE;
}
