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

    @PersistenceContext(unitName = "bookings-jpa")
    //@Inject
    private EntityManager em;

    public List<Booking> getBookings() {
        TypedQuery<BookingEntity> query = em.createNamedQuery(
                "BookingEntity.getAll", BookingEntity.class);
        List<BookingEntity> resultList =  query.getResultList();
        return resultList.stream().map(BookingConverter::toDto).collect(Collectors.toList());
    }

}
