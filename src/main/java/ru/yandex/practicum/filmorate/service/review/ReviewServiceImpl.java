package ru.yandex.practicum.filmorate.service.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.Event.EventRepository;
import ru.yandex.practicum.filmorate.repository.Film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.Review.ReviewRepository;
import ru.yandex.practicum.filmorate.repository.User.UserRepository;

import java.time.Instant;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final FilmRepository films;
    private final UserRepository users;
    private final EventRepository events;

    public Review create(Review review) {
        films.isFilmNotExists(review.getFilmId());
        users.isUserNotExists(review.getUserId());
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
        events.addEvent(event);
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
        events.addEvent(event);
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
        events.addEvent(event);
    }

    @Override
    public Collection<Review> getAllReview(Long filmId, Long count) {
        return reviewRepository.getAllReview(filmId, count);
    }

    @Override
    public void addLike(Long reviewId, Long userId) {
        getById(reviewId);
        users.isUserNotExists(userId);
        reviewRepository.addLike(reviewId, userId);
    }

    @Override
    public void addDislike(Long reviewId, Long userId) {
        getById(reviewId);
        users.getById(userId);
        reviewRepository.addDislike(reviewId, userId);
    }

    @Override
    public void deleteLike(Long reviewId, Long userId) {
        getById(reviewId);
        users.getById(userId);
        reviewRepository.deleteLike(reviewId, userId);
    }

    @Override
    public void deleteDislike(Long reviewId, Long userId) {
        getById(reviewId);
        users.getById(userId);
        reviewRepository.deleteLike(reviewId, userId);
    }
}