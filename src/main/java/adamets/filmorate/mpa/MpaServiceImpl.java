package adamets.filmorate.mpa;

import adamets.filmorate.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MpaServiceImpl implements MpaService {

    private final MpaRepository mpaRepository;

    public MpaServiceImpl(MpaRepository mpaRepository) {
        this.mpaRepository = mpaRepository;
    }

    @Override
    public Mpa findById(Integer id) {
        return mpaRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Mpa с таким id - %d не найден", id)));
    }
}
