CREATE TABLE IF NOT exists rating (
  rating_id integer generated BY DEFAULT AS IDENTITY NOT null,
  name varchar not null,
  primary key(rating_id)
);
CREATE TABLE IF NOT exists genre_name (
  genre_id integer generated BY DEFAULT AS IDENTITY NOT null,
  genre_name varchar not null,
  PRIMARY key(genre_id)
);

CREATE TABLE  IF NOT exists films(
	id integer generated BY DEFAULT AS IDENTITY NOT null,
	name varchar,
	description varchar(200),
	release_date timestamp NOT NULL,
	duration integer NOT NULL,
	rating_id integer NOT NULL REFERENCES rating(rating_id) ON DELETE cascade,
	PRIMARY key(id)
);

CREATE TABLE IF NOT exists genre (
  film_id integer NOT NULL REFERENCES films(id) ON DELETE cascade,
  genre_id integer NOT NULL REFERENCES genre_name(genre_id) ON DELETE cascade
);

CREATE TABLE IF NOT exists users (
  id integer  generated BY DEFAULT AS IDENTITY NOT NULL,
  email varchar NOT NULL unique,
  login varchar NOT NULL unique,
  name varchar,
  birthday timestamp NOT null,
  primary key(id)
);

CREATE TABLE IF NOT exists ls_likes (
	user_id integer NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	film_id integer NOT NULL REFERENCES films(id) ON DELETE CASCADE
);



CREATE TABLE IF NOT exists friends (
  user_id integer NOT NULL REFERENCES users(id) ON DELETE cascade,
  friend_id integer NOT NULL REFERENCES users(id) ON DELETE cascade
);