package ru.yandex.practicum.filmorate.service.director;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;

@Component
public interface DirectorService {

    void createDirector(Director director);

    Collection<Director> findAllDirectors();

    Director findOneDirector(Long id);

    void deleteDirector(Long id);

    void updateDirector(Director director);
}
