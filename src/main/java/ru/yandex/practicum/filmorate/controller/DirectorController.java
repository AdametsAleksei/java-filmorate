package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.director.DirectorService;

import java.util.Collection;


@Slf4j
@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;


    @GetMapping
    public Collection<Director> findAllDirectors() {
        log.info("Запрошен список всех режиссеров");
        return directorService.findAllDirectors();
    }

    @GetMapping("/{id}")
    public Director findOneDirector(@PathVariable Long id) {
        log.info("Запрошен режиссер с ID - {}", id);
        return directorService.findOneDirector(id);
    }

    @PostMapping
    public Director create(@RequestBody Director director) {
        log.info("Добавление режиссера: start");
        directorService.createDirector(director);
        log.info("Добавлен режиссер - {}", director.getId());
        return director;
    }


    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("Удаление режиссера c ID - {}", id);
        directorService.deleteDirector(id);
        log.info("Режиссер c ID - {} удален", id);
    }

    @PutMapping
    public Director update(@RequestBody Director director) {
        log.info("Обновление режиссера: start");
        directorService.updateDirector(director);
        log.info("Обновлен режиссер - {}", director.getId());
        return director;
    }
}
