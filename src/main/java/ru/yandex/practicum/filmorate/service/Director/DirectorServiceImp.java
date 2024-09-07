package ru.yandex.practicum.filmorate.service.Director;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.repository.Director.DirectorRepository;

import java.util.Collection;


@Slf4j
@Service
@AllArgsConstructor
public class DirectorServiceImp implements DirectorService {
    DirectorRepository directorRepository;


    @Override
    public Director createDirector(Director director) {
        directorRepository.createDirector(director);
        if (director.getId() == null || director.getName().isBlank()) {
            throw new InternalServerException("Не удалось сохранить данные");
        }
        return director;
    }

    @Override
    public Collection<Director> findAllDirectors() {
        return directorRepository.findAllDirectors();
    }

    @Override
    public Director findOneDirector(Long id) {
        directorRepository.isDirectorNotExists(id);
        return directorRepository.getDirectorById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Режиссер c ID - " + id + " не найден"));
    }

    @Override
    public void deleteDirector(Long id) {
        directorRepository.isDirectorNotExists(id);
        directorRepository.deleteDirector(id);
    }

    @Override
    public Director updateDirector(Director director) {
        if (director.getId() == null || director.getName() == null)
            throw new ResponseStatusException(HttpStatus.NOT_EXTENDED, "Режиссер не найден");
        directorRepository.isDirectorNotExists(director.getId());
        return directorRepository.updateDirector(director);
    }

}