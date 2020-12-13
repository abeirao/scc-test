package scc.data;

import java.util.Collection;
import java.util.Optional;

// Data Access Object
public interface DAO<T, I> {
    Optional<T> get(int id);
    Collection<T> getAll();
    Optional<I> save(T t);
    void update(T t);
    void delete(T t);
}