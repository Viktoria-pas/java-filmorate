package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;
import java.util.Optional;

@Component
@Qualifier("mpaRatingDbStorage")
public class MpaRatingDbStorage implements MpaRatingStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaRatingRowMapper mpaRatingRowMapper;
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa_ratings";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa_ratings WHERE id = ?";
    private static final String SAVE_QUERY = "INSERT INTO mpa_ratings (name, description) VALUES (?, ?)";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM mpa_ratings WHERE id = ?";

    public MpaRatingDbStorage(JdbcTemplate jdbcTemplate, MpaRatingRowMapper mpaRatingRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaRatingRowMapper = mpaRatingRowMapper;
    }

    @Override
    public MpaRating save(MpaRating mpaRating) {
        jdbcTemplate.update(SAVE_QUERY, mpaRating.getName(), mpaRating.getDescription());
        String sql = "SELECT MAX(id) FROM mpa_ratings";
        int mpaId = jdbcTemplate.queryForObject(sql, Integer.class);
        mpaRating.setId(mpaId);
        return mpaRating;
    }

    @Override
    public Optional<MpaRating> findById(int id) {
        return jdbcTemplate.query(FIND_BY_ID_QUERY, mpaRatingRowMapper, id).stream().findFirst();
    }

    @Override
    public Collection<MpaRating> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, mpaRatingRowMapper);
    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.update(DELETE_BY_ID_QUERY, id);
    }
}
