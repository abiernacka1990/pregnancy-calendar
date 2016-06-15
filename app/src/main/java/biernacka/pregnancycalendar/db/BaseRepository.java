package biernacka.pregnancycalendar.db;

import java.util.List;

/**
 * Created by agnieszka on 12.04.16.
 */
public interface BaseRepository<T> {

    int create(T item);

    int update(T item);

    int delete(T item);

    List<T> getAll();
}
