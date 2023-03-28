package com.example.alkemy.disneyworld.repository;

import com.example.alkemy.disneyworld.entity.MovieCharacterEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieCharacterRepository extends JpaRepository<MovieCharacterEntity, String> {
    Optional<MovieCharacterEntity> findByName(String name);
    List<MovieCharacterEntity> findAll(Specification<MovieCharacterEntity> spec);
}
