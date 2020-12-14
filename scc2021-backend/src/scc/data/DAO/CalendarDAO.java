package scc.data.DAO;

import java.util.Collection;
import java.util.Optional;

import scc.data.Calendar;

public class CalendarDAO implements DAO<Calendar, Long> {

    public Optional<Calendar> get(Long id) {
        return null;
    }

    public Collection<Calendar> getAll() {
        return null;
    }

    public Optional<Long> save(Calendar calendar) {
        return null;
    }

    public void update(Calendar calendar){}

    public void delete(Calendar calendar){}
    
}
