package ru.yandex.practicum.filmorate.storage.film_genre;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmGenreStorage implements FilmGenreStorage {
    private final Map<Long, Set<Integer>> filmGenres = new HashMap<>();

    @Override
    public void saveFilmGenres(Long filmId, List<Integer> genreIds) {
        deleteFilmGenres(filmId);

        filmGenres.put(filmId, new HashSet<>(genreIds));
    }

    @Override
    public void deleteFilmGenres(Long filmId) {
        filmGenres.remove(filmId);
    }

    @Override
    public List<FilmGenre> findByFilmId(Long filmId) {
        Set<Integer> genres = filmGenres.getOrDefault(filmId, Collections.emptySet());
        return genres.stream()
                .map(genreId -> new FilmGenre(filmId, genreId))
                .collect(Collectors.toList());
    }

    public List<FilmGenre> findAll() {
        return filmGenres.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(genreId -> new FilmGenre(entry.getKey(), genreId)))
                .collect(Collectors.toList());
    }

}
