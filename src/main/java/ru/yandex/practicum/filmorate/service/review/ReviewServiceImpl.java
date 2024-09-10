package ru.yandex.practicum.filmorate.service.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.Review.ReviewRepository;
import ru.yandex.practicum.filmorate.service.event.EventService;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.Instant;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final FilmService filmService;
    private final UserService userService;
    private final EventService eventService;

    public Review create(Review review) {
        filmService.getById(review.getFilmId());
        userService.get(review.getUserId());
        Review reviewCreate = reviewRepository.create(review);
        if (reviewCreate.getReviewId() == null) {
            throw new InternalServerException("Не удалось сохранить данные");
        }
        Event event = Event.builder()
                .userId(review.getUserId())
                .entityId(reviewCreate.getReviewId())
                .timestamp(Instant.now().toEpochMilli())
                .eventType(Event.EventType.REVIEW)
                .operation(Event.Operation.ADD)
                .build();
        eventService.addEvent(event);
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
        Event event = Event.builder()
                .userId(reviewAfterUpdate.getUserId())
                .entityId(reviewBeforeUpdate.getReviewId())
                .timestamp(Instant.now().toEpochMilli())
                .eventType(Event.EventType.REVIEW)
                .operation(Event.Operation.UPDATE)
                .build();
        eventService.addEvent(event);
        log.info("Отзыв с id = {} обновлен", reviewAfterUpdate.getReviewId());
        return reviewAfterUpdate;
    }

    @Override
    public void deleteById(Long id) {
        Review review = getById(id);
        reviewRepository.deleteById(id);
        Event event = Event.builder()
                .userId(review.getUserId())
                .entityId(review.getReviewId())
                .timestamp(Instant.now().toEpochMilli())
                .eventType(Event.EventType.REVIEW)
                .operation(Event.Operation.REMOVE)
                .build();
        eventService.addEvent(event);
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