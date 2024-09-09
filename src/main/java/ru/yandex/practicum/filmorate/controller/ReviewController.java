package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.review.ReviewService;
import ru.yandex.practicum.filmorate.validation.Marker;

import java.util.Collection;

@RestController
@RequestMapping("/reviews")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Marker.OnCreate.class)
    public Review create(@Valid @RequestBody Review review) {
        log.info("Создание отзыва: start");
        System.out.println(review);
        Review create = reviewService.create(review);
        log.info("Создан пользователь - {}", create);
        return review;
    }

    @GetMapping("/{id}")
    public Review getById(@PathVariable("id") long id) {
        log.info("Запрошен отзыв с ID - {}", id);
        return reviewService.getById(id);
    }

    @PutMapping
    @Validated(Marker.OnUpdate.class)
    public Review update(@Valid @RequestBody Review review) {
        log.info("Обновление тзыва: start");
        Review userUpdate = reviewService.update(review);
        log.info("Обновлен пользователь - {}", userUpdate);
        return userUpdate;
    }

    @DeleteMapping("/{id}")
    public void deleteByID(@PathVariable("id") long id) {
        log.info("Удаление отзыва с id = {}", id);
        reviewService.deleteById(id);
        log.info("Отзыв с id = {} удален", id);
    }

    @GetMapping
    public Collection<Review> getAllReview(@RequestParam(defaultValue = "10", name = "count") Long count, @RequestParam(defaultValue = "0", name = "filmId") Long filmId) {
        log.info("Запрошен список отзывов");
        return reviewService.getAllReview(filmId, count);
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Добавление Like : start");
        reviewService.addLike(id, userId);
        log.info("Like добавлен");
    }

    @PutMapping("{id}/dislike/{userId}")
    public void addDislike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Добавление Dislike : start");
        reviewService.addDislike(id, userId);
        log.info("Dislike добавлен");
    }

    @DeleteMapping("{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Удаление Like : start");
        reviewService.deleteLike(id, userId);
        log.info("Like удален");
    }

    @DeleteMapping("{id}/dislike/{userId}")
    public void deleteDislike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Удаление Dislike : start");
        reviewService.deleteDislike(id, userId);
        log.info("Dislike удален");
    }
}

