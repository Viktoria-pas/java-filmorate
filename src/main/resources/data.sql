-- Отключаем проверку внешних ключей
SET REFERENTIAL_INTEGRITY FALSE;

-- Очистка зависимых таблиц
DELETE FROM likes;
DELETE FROM friendships;
DELETE FROM film_genres;
DELETE FROM films;
DELETE FROM users;

-- Очистка таблиц friend_status, genres и mpa_ratings
DELETE FROM friend_status;
DELETE FROM genres;
DELETE FROM mpa_ratings;

-- Сброс счетчиков автоинкремента (если используется H2)
ALTER TABLE friend_status ALTER COLUMN id RESTART WITH 1;
ALTER TABLE genres ALTER COLUMN id RESTART WITH 1;
ALTER TABLE mpa_ratings ALTER COLUMN id RESTART WITH 1;

-- Включаем проверку внешних ключей
SET REFERENTIAL_INTEGRITY TRUE;


INSERT INTO mpa_ratings (id, name, description) VALUES
(1, 'G', 'Нет возрастных ограничений'),
(2, 'PG', 'Детям рекомендуется смотреть фильм с родителями'),
(3, 'PG-13', 'Детям до 13 лет просмотр не желателен'),
(4, 'R', 'Лицам до 17 лет просмотр только в присутствии взрослого'),
(5, 'NC-17', 'Лицам до 18 лет просмотр запрещён');


INSERT INTO genres (id, name) VALUES
(1, 'Комедия'),
(2, 'Драма'),
(3, 'Мультфильм'),
(4, 'Триллер'),
(5, 'Документальный'),
(6, 'Боевик');

INSERT INTO friend_status (id, status) VALUES
(1, 'PENDING'),
(2, 'CONFIRMED');
