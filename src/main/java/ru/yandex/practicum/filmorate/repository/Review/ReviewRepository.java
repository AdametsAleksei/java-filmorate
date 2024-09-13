package ru.yandex.practicum.filmorate.repository.Review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

public interface ReviewRepository {
    Review create(Review review);

    Optional<Review> getById(Long id);

    Review update(Review reviewBeforeUpdate, Review reviewAfterUpdate);

    void deleteById(Long id);

    Collection<Review> getAllReview(Long filmId, Long count);

    void addLike(Long reviewId, Long userId);

    void addDislike(Long reviewId, Long userId);

    void deleteLike(Long reviewId, Long userId);

    void deleteDislike(Long reviewId, Long userId);
}
