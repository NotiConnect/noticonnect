package io.github.drspeedy.noticonnectandroid.repositories;

import java.util.List;
import java.util.Map;

/**
 * Created by doc on 10/23/16.
 */

public interface StandardRepository<IdT, T> {
    T get(IdT id);
    Map<IdT, T> getAllMapped();
    List<T> getAll();

    void put(IdT id, T value);
    void putAll(List<T> values);

    void delete(IdT id);
}
