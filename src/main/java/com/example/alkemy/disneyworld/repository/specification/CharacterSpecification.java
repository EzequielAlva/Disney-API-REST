package com.example.alkemy.disneyworld.repository.specification;

import com.example.alkemy.disneyworld.dto.CharacterDTOFilterRequest;
import com.example.alkemy.disneyworld.entity.MovieCharacterEntity;
import com.example.alkemy.disneyworld.entity.MovieEntity;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

@Component
public class CharacterSpecification {

    public Specification<MovieCharacterEntity> getByFilters(CharacterDTOFilterRequest characterDTO){
        return (character, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(StringUtils.hasLength(characterDTO.getName())){
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(character.get("name")),
                                "%" + characterDTO.getName().toLowerCase() + "%"
                        )
                );
            }

            if(characterDTO.getAge() != null && characterDTO.getAge() > -1){
                predicates.add(
                        criteriaBuilder.equal(
                                character.get("age"),
                                characterDTO.getAge()
                        )
                );
            }

            if(characterDTO.getWeight() != null && characterDTO.getWeight() > -1){
                predicates.add(
                        criteriaBuilder.equal(
                                character.get("weight"),
                                characterDTO.getWeight()
                        )
                );
            }

            if(StringUtils.hasLength(characterDTO.getIdMovie())){
                Join<MovieEntity, MovieCharacterEntity> join = character.join("movies", JoinType.INNER);
                Expression<String> movieId = join.get("id");
                predicates.add(movieId.in(characterDTO.getIdMovie()));
            }

            query.distinct(true);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
