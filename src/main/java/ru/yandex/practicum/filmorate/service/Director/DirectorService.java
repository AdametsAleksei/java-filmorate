package ru.yandex.practicum.filmorate.service.Director;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;

@Component
public interface DirectorService {

    Director createDirector(Director director);

    Collection<Director> findAllDirectors();

    Director findOneDirector(Long id);

    void deleteDirector(Long id);

    Director updateDirector(Director director);
}
