package si.fri.rso2021.calendar.api.resources;


import si.fri.rso2021.calendar.models.objects.Booking;
import si.fri.rso2021.calendar.services.beans.BookingBean;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/calendar")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CalendarResource {
    private Logger log = Logger.getLogger(CalendarResource.class.getName());

    @Inject
    private BookingBean bookingBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getBookings() {
        List<Booking> bookings = bookingBean.getBookings();
        return Response.status(Response.Status.OK).entity(bookings).build();
    }

}
