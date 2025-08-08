package adamets.filmorate.genre;

import java.util.Set;

public interface GenreService {

    Genre findById(Integer id);

    Set<Genre> findAllById(Iterable<Integer> ids);
}
