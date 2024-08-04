# Educational project - Filmorate
### Author:
- Adamets Aleksei
## Database schema:
![Database schema](https://github.com/AdametsAleksei/java-filmorate/blob/main/DatabaseSchemaIteration5.png)

### Top 10 movies by popylarity:
```
SELECT films.name,
       SUM(popular.user_id) AS sum_likes
FROM films
LEFT OUTER JOIN popular ON films.film_id=popular.film_id
GROUP BY films.name
ORDER BY SUM(popular.user_id) DESC
LIMIT 10;
```

### Get genres for films:
```
SELECT gen.name AS genre_name,
       film.name AS film_name
FROM genre AS gen
LEFT OUTER JOIN film_genre AS filter ON gen.genre_id=filgen.genre_id
LEFT OUTER JOIN films ON filgen.film_id=films.dilm_id
GROUP BY gen.name,
         film.name;
```
