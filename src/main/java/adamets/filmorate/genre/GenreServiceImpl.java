package adamets.filmorate.genre;

import adamets.filmorate.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre findById(Integer id) {
        return genreRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Genre с таким id - %d не найден", id)));
    }

    @Override
    public Set<Genre> findAllById(Iterable<Integer> ids) {
        return new HashSet<>(genreRepository.findAllById(ids));
    }
}
