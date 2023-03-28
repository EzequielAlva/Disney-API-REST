package com.example.alkemy.disneyworld.repository.specification;

import com.example.alkemy.disneyworld.dto.MovieDTOFilterRequest;
import com.example.alkemy.disneyworld.entity.MovieEntity;
import com.example.alkemy.disneyworld.entity.MovieGenreEntity;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class MovieSpecification {

    public Specification<MovieEntity> getByFilter(MovieDTOFilterRequest movieFilter){
        return (movie, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(StringUtils.hasLength(movieFilter.getName())){
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(movie.get("title")),
                                "%" + movieFilter.getName().toLowerCase() + "%"
                        )
                );
            }

            if(StringUtils.hasLength(movieFilter.getIdGenre())){
                Join<MovieGenreEntity, MovieEntity> join = movie.join("genres", JoinType.INNER);
                Expression<MovieGenreEntity> genreId = join.get("id");
                predicates.add(genreId.in(movieFilter.getIdGenre()));
            }

            query.distinct(true);

            String orderByTitle = "title";
            query.orderBy(
                    movieFilter.isASC() ?
                            criteriaBuilder.asc(movie.get(orderByTitle)) :
                            criteriaBuilder.desc(movie.get(orderByTitle))
            );

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
