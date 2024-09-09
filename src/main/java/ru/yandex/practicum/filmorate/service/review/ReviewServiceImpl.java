package ru.yandex.practicum.filmorate.service.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.Review.ReviewRepository;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final FilmService filmService;
    private final UserService userService;

    @Override
    public Review create(Review review) {
        filmService.getById(review.getFilmId());
        userService.get(review.getUserId());
        Review reviewCreate = reviewRepository.create(review);
        if (reviewCreate.getReviewId() == null) {
            throw new InternalServerException("Не удалось сохранить данные");
        }
        log.info("Отзыв создан с id = {}", reviewCreate.getReviewId());
        return reviewCreate;
    }

    @Override
    public Review getById(Long id) {
        log.info("Получение отзыва с id={}", id);
        return reviewRepository.getById(id).orElseThrow(() -> new NotFoundException(
                "Отзыв c ID - " + id + " не найден"));
    }

    @Override
    public Review update(Review reviewAfterUpdate) {
        Review reviewBeforeUpdate = getById(reviewAfterUpdate.getReviewId());
        reviewAfterUpdate = reviewRepository.update(reviewBeforeUpdate, reviewAfterUpdate);
        log.info("Отзыв с id = {} обновлен", reviewAfterUpdate.getReviewId());
        return reviewAfterUpdate;
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        reviewRepository.deleteById(id);
    }

    @Override
    public Collection<Review> getAllReview(Long filmId, Long count) {
        return reviewRepository.getAllReview(filmId, count);
    }

    @Override
    public void addLike(Long reviewId, Long userId) {
        getById(reviewId);
        userService.get(userId);
        reviewRepository.addLike(reviewId, userId);
    }

    @Override
    public void addDislike(Long reviewId, Long userId) {
        getById(reviewId);
        userService.get(userId);
        reviewRepository.addDislike(reviewId, userId);
    }

    @Override
    public void deleteLike(Long reviewId, Long userId) {
        getById(reviewId);
        userService.get(userId);
        reviewRepository.deleteLike(reviewId, userId);
    }

    @Override
    public void deleteDislike(Long reviewId, Long userId) {
        getById(reviewId);
        userService.get(userId);
        reviewRepository.deleteLike(reviewId, userId);
    }
}