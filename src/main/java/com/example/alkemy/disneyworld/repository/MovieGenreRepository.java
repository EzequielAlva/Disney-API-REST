package com.example.alkemy.disneyworld.repository;

import com.example.alkemy.disneyworld.entity.MovieGenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieGenreRepository extends JpaRepository<MovieGenreEntity, String> {
    Optional<MovieGenreEntity> findByName(String name);
}
