package com.example.alkemy.disneyworld.repository;

import com.example.alkemy.disneyworld.entity.MovieEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, String> {
    List<MovieEntity> findAll(Specification<MovieEntity> spec);
}
