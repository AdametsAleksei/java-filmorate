package ru.yandex.practicum.filmorate.service.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

public interface ReviewService {
    public Review create(Review review);

    public Review getById(Long id);

    public Review update(Review review);

    public void deleteById(Long id);

    public Collection<Review> getAllReview(Long filmId, Long count);

    public void addLike(Long reviewId, Long userId);

    public void addDislike(Long reviewId, Long userId);

    public void deleteLike(Long reviewId, Long userId);

    public void deleteDislike(Long reviewId, Long userId);
}
