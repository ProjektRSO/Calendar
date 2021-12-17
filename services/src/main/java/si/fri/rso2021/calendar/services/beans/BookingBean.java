package si.fri.rso2021.calendar.services.beans;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso2021.calendar.models.converters.BookingConverter;
import si.fri.rso2021.calendar.models.entities.BookingEntity;
import si.fri.rso2021.calendar.models.objects.Booking;

@RequestScoped
public class BookingBean {
    private Logger log = Logger.getLogger(BookingBean.class.getName());

    //@PersistenceContext(unitName = "bookings-jpa")
    //@Inject
    private EntityManager em;

    public List<Booking> getBookings() {
        TypedQuery<BookingEntity> query = em.createNamedQuery(
                "BookingEntity.getAll", BookingEntity.class);
        List<BookingEntity> resultList =  query.getResultList();
        return resultList.stream().map(BookingConverter::toDto).collect(Collectors.toList());
    }

    public Booking getBooking_byId(Integer id) {
        BookingEntity bookingEntity = em.find(BookingEntity.class, id);
        if (bookingEntity == null) {
            throw new NotFoundException();
        }
        Booking b = BookingConverter.toDto(bookingEntity);
        return b;
    }

    public List<Booking> getBookings_byCustomerId(Integer cid) {
        List<BookingEntity> query = em.createNamedQuery("BookingEntity.getByCustomerId", BookingEntity.class).setParameter("customerid", cid).getResultList();
        return query.stream().map(BookingConverter::toDto).collect(Collectors.toList());
    }

    public List<Booking> getBookings_byWorkerId(Integer wid) {
        List<BookingEntity> query = em.createNamedQuery("BookingEntity.getByWorkerId", BookingEntity.class).setParameter("workerid", wid).getResultList();
        return query.stream().map(BookingConverter::toDto).collect(Collectors.toList());
    }

    public Booking createBooking(Booking b) {
        BookingEntity bookingEntity = BookingConverter.toEntity(b);
        try {
            beginTx();
            em.persist(bookingEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }
        if (bookingEntity.getId() == null) {
            throw new RuntimeException("Worker entity was not persisted.");
        }
        return BookingConverter.toDto(bookingEntity);
    }

    public Booking putBooking(Integer id, Booking b) {
        BookingEntity bookingEntity = em.find(BookingEntity.class, id);
        if (bookingEntity == null) return null;
        BookingEntity updatedbookingEntity = BookingConverter.toEntity(b);

        try {
            beginTx();
            updatedbookingEntity.setId(id);
            updatedbookingEntity = em.merge(updatedbookingEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }
        return BookingConverter.toDto(updatedbookingEntity);
    }

    public boolean deleteBooking(Integer id) {
        BookingEntity bookingEntity = em.find(BookingEntity.class, id);
        if (bookingEntity != null) {
            try {
                beginTx();
                em.remove(bookingEntity);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {return false;}
        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

}
