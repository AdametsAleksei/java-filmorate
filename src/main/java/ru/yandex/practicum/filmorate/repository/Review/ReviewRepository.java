package ru.yandex.practicum.filmorate.repository.Review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

public interface ReviewRepository {
    public Review create(Review review);

    public Optional<Review> getById(Long id);

    public Review update(Review reviewBeforeUpdate, Review reviewAfterUpdate);

    public void deleteById(Long id);

    public Collection<Review> getAllReview(Long filmId, Long count);

    public void addLike(Long reviewId, Long userId);

    public void addDislike(Long reviewId, Long userId);

    public void deleteLike(Long reviewId, Long userId);

    public void deleteDislike(Long reviewId, Long userId);
}
