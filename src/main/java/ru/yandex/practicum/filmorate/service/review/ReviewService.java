package ru.yandex.practicum.filmorate.service.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

public interface ReviewService {
    Review create(Review review);

    Review getById(Long id);

    Review update(Review review);

    void deleteById(Long id);

    Collection<Review> getAllReview(Long filmId, Long count);

    void addLike(Long reviewId, Long userId);

    void addDislike(Long reviewId, Long userId);

    void deleteLike(Long reviewId, Long userId);

    void deleteDislike(Long reviewId, Long userId);
}
