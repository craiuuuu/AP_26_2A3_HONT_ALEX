
CREATE TABLE genres (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL UNIQUE
);


CREATE TABLE movies (
                        id SERIAL PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        release_date DATE,
                        duration INTEGER,
                        score DECIMAL(3, 1),
                        genre_id INTEGER REFERENCES genres(id)
);
CREATE TABLE actors (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL
);
CREATE TABLE movie_actors (
                              movie_id INTEGER REFERENCES movies(id),
                              actor_id INTEGER REFERENCES actors(id),
                              PRIMARY KEY (movie_id, actor_id)
);