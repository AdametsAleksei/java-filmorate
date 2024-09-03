MERGE INTO FILMS (FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)
VALUES (101,'TestName', 'Test', '1999-02-27', 100, 1),
        (102,'TestName2', 'Test2', '1999-03-28', 120, 3),
        (103,'TestName3', 'Test3', '1999-01-27', 110, 4);

MERGE INTO RATING_MPA (MPA_ID, MPA_NAME)
VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');

MERGE INTO USERS (USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY)
    VALUES (101,'test@yandex.ru', 'Test','Test','1995-08-22'),
           (102,'TestName2', 'Test2', 'Test2','1999-03-28'),
           (103,'TestName3', 'Test3', 'Test3', '1999-01-27');