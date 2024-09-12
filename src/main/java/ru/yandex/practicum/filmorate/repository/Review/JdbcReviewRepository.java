package ru.yandex.practicum.filmorate.repository.Review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.mapper.ReviewExtractor;
import ru.yandex.practicum.filmorate.repository.mapper.ReviewsExtractor;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcReviewRepository implements ReviewRepository {
    private final NamedParameterJdbcTemplate jdbc;
    private final ReviewExtractor reviewExtractor;
    private final ReviewsExtractor reviewsExtractor;

    @Override
    public Review create(Review review) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = """
                INSERT INTO REVIEW ("CONTENT", "IS_POSITIVE", "USER_ID", "FILM_ID")
                VALUES (:CONTENT, :IS_POSITIVE, :USER_ID, :FILM_ID);
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("CONTENT", review.getContent())
                .addValue("IS_POSITIVE", review.getIsPositive())
                .addValue("USER_ID", review.getUserId())
                .addValue("FILM_ID", review.getFilmId());
        jdbc.update(sql, parameter, keyHolder);
        review.setReviewId(keyHolder.getKeyAs(Long.class));
        review.setUseful(0L);
        return review;
    }

    @Override
    public Optional<Review> getById(Long id) {
        String sql = """
                SELECT R.REVIEW_ID, R.CONTENT, R.IS_POSITIVE, R.USER_ID, R.FILM_ID, SUM(U.USEFUL) AS USEFUL
                FROM REVIEW AS R
                LEFT JOIN USEFUL AS U ON R.REVIEW_ID = U.REVIEW_ID
                WHERE R.REVIEW_ID = :REVIEW_ID
                GROUP BY R.REVIEW_ID;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource().addValue("REVIEW_ID", id);
        return Optional.ofNullable(jdbc.query(sql, parameter, reviewExtractor));
    }

    @Override
    public Review update(Review reviewBeforeUpdate, Review reviewAfterUpdate) {
        String sql = """
                UPDATE REVIEW
                SET CONTENT = :CONTENT,
                    IS_POSITIVE = :IS_POSITIVE
                WHERE REVIEW_ID = :REVIEW_ID;
                """;

        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("CONTENT", reviewAfterUpdate.getContent())
                .addValue("REVIEW_ID", reviewAfterUpdate.getReviewId())
                .addValue("IS_POSITIVE", reviewAfterUpdate.getIsPositive());
        jdbc.update(sql, parameter);
        reviewBeforeUpdate.setContent(reviewAfterUpdate.getContent());
        reviewBeforeUpdate.setIsPositive(reviewAfterUpdate.getIsPositive());
        return reviewBeforeUpdate;
    }

    @Override
    public void deleteById(Long id) {
        String sql = """
                DELETE FROM REVIEW WHERE REVIEW.REVIEW_ID = :REVIEW_ID;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource().addValue("REVIEW_ID", id);
        jdbc.update(sql, parameter);
    }

    @Override
    public Collection<Review> getAllReview(Long filmId, Long count) {
        String sqlWithFilmId = """
                SELECT R.REVIEW_ID, R.CONTENT, R.IS_POSITIVE, R.USER_ID, R.FILM_ID, COALESCE(SUM(U.USEFUL), 0) AS USEFUL
                FROM REVIEW AS R
                LEFT JOIN USEFUL AS U ON R.REVIEW_ID = U.REVIEW_ID
                WHERE R.FILM_ID = :FILM_ID
                GROUP BY R.REVIEW_ID
                ORDER BY USEFUL DESC
                LIMIT :COUNT;
                """;
        String sqlWithoutFilmId = """
                SELECT R.REVIEW_ID, R.CONTENT, R.IS_POSITIVE, R.USER_ID, R.FILM_ID, COALESCE(SUM(U.USEFUL), 0) AS USEFUL
                FROM REVIEW AS R
                LEFT JOIN USEFUL AS U ON R.REVIEW_ID = U.REVIEW_ID
                GROUP BY R.REVIEW_ID
                ORDER BY USEFUL DESC
                LIMIT :COUNT;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("COUNT", count)
                .addValue("FILM_ID", filmId);
        if (filmId == 0) {
            return jdbc.query(sqlWithoutFilmId, parameter, reviewsExtractor);
        } else {
            return jdbc.query(sqlWithFilmId, parameter, reviewsExtractor);
        }
    }

    @Override
    public void addLike(Long reviewId, Long userId) {
        String sql = """
                MERGE INTO USEFUL (REVIEW_ID, USER_ID, USEFUL)
                VALUES (:REVIEW_ID, :USER_ID, :USEFUL);
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("REVIEW_ID", reviewId)
                .addValue("USER_ID", userId)
                .addValue("USEFUL", 1);
        jdbc.update(sql, parameter);
    }

    @Override
    public void addDislike(Long reviewId, Long userId) {
        String sql = """
                MERGE INTO USEFUL (REVIEW_ID, USER_ID, USEFUL)
                VALUES (:REVIEW_ID, :USER_ID, :USEFUL);
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("REVIEW_ID", reviewId)
                .addValue("USER_ID", userId)
                .addValue("USEFUL", -1);
        jdbc.update(sql, parameter);
    }

    @Override
    public void deleteLike(Long reviewId, Long userId) {
        String sql = """
                DELETE FROM USEFUL WHERE USEFUL.REVIEW_ID = :REVIEW_ID AND USEFUL.USER_ID = :USER_ID;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("REVIEW_ID", reviewId)
                .addValue("USER_ID", userId);
        jdbc.update(sql, parameter);
    }

    @Override
    public void deleteDislike(Long reviewId, Long userId) {
        String sql = """
                DELETE FROM USEFUL WHERE USEFUL.REVIEW_ID = :REVIEW_ID AND USEFUL.USER_ID = :USER_ID;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("REVIEW_ID", reviewId)
                .addValue("USER_ID", userId);
        jdbc.update(sql, parameter);
    }
}