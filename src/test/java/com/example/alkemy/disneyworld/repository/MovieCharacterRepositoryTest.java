package com.example.alkemy.disneyworld.repository;

import com.example.alkemy.disneyworld.entity.MovieCharacterEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MovieCharacterRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MovieCharacterRepository movieCharacterRepository;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(entityManager);
    }

    @Test
    @DisplayName("Save Character")
    void saveCharacter() {
        MovieCharacterEntity character = new MovieCharacterEntity();
        character.setImage("./images/Billy.jpg");
        character.setName("Billy");
        character.setAge(28);
        character.setWeight(80);

        Assertions.assertNull(character.getId());
        movieCharacterRepository.save(character);
        Assertions.assertNotNull(character.getId());
    }

    @Test
    @DisplayName("Find All Characters")
    void findAll() {
        MovieCharacterEntity character1 = new MovieCharacterEntity();
        character1.setImage("./images/Billy.jpg");
        character1.setName("Billy");
        character1.setAge(28);
        character1.setWeight(80);
        movieCharacterRepository.save(character1);

        MovieCharacterEntity character2 = new MovieCharacterEntity();
        character2.setImage("./images/Gery.jpg");
        character2.setName("Gery");
        character2.setAge(23);
        character2.setWeight(58);
        movieCharacterRepository.save(character2);

        List<MovieCharacterEntity> entityList = movieCharacterRepository.findAll();

        Assertions.assertNotNull(entityList);
        Assertions.assertEquals(entityList.size(), 10);
        //both characters plus 8 in database
    }

}
