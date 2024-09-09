package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReviewExtractor implements ResultSetExtractor<Review> {
    @Override
    public Review extractData(ResultSet rs) throws SQLException, DataAccessException {
        Review review = null;
        while (rs.next()) {
            if (review == null) {
                review = new Review();
            }
            review.setReviewId(rs.getLong("REVIEW_ID"));
            review.setContent(rs.getString("CONTENT"));
            review.setIsPositive(rs.getBoolean("IS_POSITIVE"));
            review.setUserId(rs.getLong("USER_ID"));
            review.setFilmId(rs.getLong("FILM_ID"));
            review.setUseful(rs.getLong("USEFUL"));
        }
        return review;
    }
}
