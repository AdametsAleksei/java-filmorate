package adamets.filmorate.film.repository;

import adamets.filmorate.film.model.Film;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class FilmRepositoryTest {

    FilmRepository filmRepository;

    @BeforeEach
    void setUp() {
        filmRepository = new InMemoryFilmRepository();
    }

    @Test
    @DisplayName("Получение всех фильмов, когда их 0")
    void givenEmptyDataSource_whenFindAllFilms_thenEmptyIterableRetrieved() {
        assertThat(this.filmRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Получение фильма с неправильным Id")
    void givenInvalidId_whenFindById_thenEmptyRetrieved() {

        //given
        Long invalidId = -1L;

        //when
        Optional<Film> film = this.filmRepository.getById(invalidId);
        //then
        assertThat(film).isEmpty();
    }

    @Test
    @DisplayName("Получение Id")
    void shouldReturnNewId() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        Method method = InMemoryFilmRepository.class.getDeclaredMethod("getNewId");
        method.setAccessible(true);

        //when
        Object result = method.invoke(this.filmRepository);

        //then
        assertThat(result).isEqualTo(1L);
    }

    @Test
    @DisplayName("Создание фильма")
    void givenValidFilm_whenCreateFilm_thenCreatedFilmRetrieved() {

        //given
        Film film = new Film(null, "Терминатор", "Очень крутой фильм",
                LocalDate.of(1984, 10, 26), 150);

        //when

        //then
        assertThat(film)
                .usingRecursiveComparison()
                .isEqualTo(this.filmRepository.createFilm(film).orElse(null));
    }

    @Test
    @DisplayName("Получение фильма по Id")
    void givenValidId_whenGetFilmById_thenFilmShouldBeRetrieved() {

        //given
        Film film = new Film(null, "Терминатор", "Очень крутой фильм",
                LocalDate.of(1984, 10, 26), 150);
        this.filmRepository.createFilm(film);

        //when

        //then

        assertThat(film)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(this.filmRepository.getById(film.getId()).orElse(null));
    }

    @Test
    @DisplayName("Обновление фильма")
    void givenFilmForUpdate_whenUpdateFilm_thenUpdatedFilmRetrieved() {
        //given
        Film filmOld = new Film(null, "Терминатор", "Очень крутой фильм",
                LocalDate.of(1984, 10, 26), 150);
        Film filmUpdate = new Film(null, "Терминатор 2", "Очень очень крутой фильм",
                LocalDate.of(1986, 10, 26), 150);
        this.filmRepository.createFilm(filmOld);
        filmUpdate.setId(filmOld.getId());

        //when

        //then
        assertThat(filmUpdate)
                .usingRecursiveComparison()
                .isEqualTo(this.filmRepository.updateFilm(filmUpdate).orElse(null));
    }

    @Test
    @DisplayName("Некорректный ID при обновлении фильма")
    void givenInvalidIdFilmForUpdate_whenUpdateFilm_thenEmptyRetrieved() {

        //given
        Film filmOld = new Film(100L, "Терминатор", "Очень крутой фильм",
                LocalDate.of(1984, 10, 26), 150);

        //when

        //then
        assertThat(this.filmRepository.updateFilm(filmOld)).isEmpty();

    }

    @Test
    @DisplayName("Получение списка из нескольких фильмов")
    void givenSeveralFilms_whenGetAll_thenReturnIterable() {

        //given
        Film film1 = new Film(null, "Терминатор", "Очень крутой фильм",
                LocalDate.of(1984, 10, 26), 150);
        Film film2 = new Film(null, "Терминатор", "Очень крутой фильм",
                LocalDate.of(1984, 10, 26), 150);
        Film film3 = new Film(null, "Терминатор", "Очень крутой фильм",
                LocalDate.of(1984, 10, 26), 150);

        this.filmRepository.createFilm(film1);
        this.filmRepository.createFilm(film2);
        this.filmRepository.createFilm(film3);

        //when

        //then
        assertThat(this.filmRepository.findAll()).contains(film1, film2, film3);
    }
}