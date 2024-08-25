package ru.yandex.practicum.filmorate.service.mpa;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.Mpa.MpaRepository;

import java.util.Collection;

@Service
@AllArgsConstructor
public class MpaServiceImpl implements MpaService {
    private final MpaRepository mpaRepository;

    @Override
    public Collection<Mpa> getAll() {
        return mpaRepository.getAll();
    }

    @Override
    public Mpa getById(int id) {
        return mpaRepository.getById(id).orElseThrow(() -> new NotFoundException(
                "MPA c ID - " + id + " не найден"));
    }
}
