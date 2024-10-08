create TABLE IF NOT EXISTS USERS
(
    USER_ID LONG NOT NULL PRIMARY KEY AUTO_INCREMENT,
    EMAIL VARCHAR(50) NOT NULL,
    LOGIN VARCHAR(50) NOT NULL,
    NAME VARCHAR(50),
    BIRTHDAY DATE
);

create TABLE IF NOT EXISTS FRIENDS
(
    USER_ID LONG NOT NULL REFERENCES USERS (USER_ID),
    FRIEND_ID LONG NOT NULL
);

create TABLE IF NOT EXISTS FILMS
(
    FILM_ID LONG NOT NULL PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(50) NOT NULL,
    DESCRIPTION VARCHAR(201) NOT NULL,
    RELEASE_DATE DATE,
    DURATION INT,
    MPA_ID SMALLINT NOT NULL
);

CREATE TABLE IF NOT EXISTS GENRE
(
    GENRE_ID INT PRIMARY KEY AUTO_INCREMENT,
    GENRE_NAME VARCHAR(50) NOT NULL
);

create TABLE IF NOT EXISTS FILM_GENRE
(
    FILM_ID LONG NOT NULL,
    GENRE_ID INT NOT NULL,
    FOREIGN KEY (FILM_ID) REFERENCES FILMS (FILM_ID),
    FOREIGN KEY (GENRE_ID) REFERENCES GENRE (GENRE_ID)
);

CREATE TABLE IF NOT EXISTS POPULAR
(
    FILM_ID LONG NOT NULL,
    USER_ID LONG NOT NULL,
    CONSTRAINT "POPULAR_PK" PRIMARY KEY (FILM_ID, USER_ID)
);

CREATE TABLE IF NOT EXISTS RATING_MPA
(
    MPA_ID INT PRIMARY KEY AUTO_INCREMENT,
    MPA_NAME VARCHAR(50) NOT NULL
);