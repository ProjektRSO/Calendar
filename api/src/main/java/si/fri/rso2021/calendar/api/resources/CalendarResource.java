package si.fri.rso2021.calendar.api.resources;


import si.fri.rso2021.calendar.models.entities.BookingEntity;
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

    @GET
    @Path("/{id}")
    public Response getBookingbyId(@PathParam("id") Integer id) {
        Booking w = bookingBean.getBooking_byId(id);
        if (w == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(w).build();
    }

    @GET
    @Path("/c/{cid}")
    public Response getBookingsbyCustomerId(@PathParam("cid") Integer cid) {

        List<Booking> bs = bookingBean.getBookings_byCustomerId(cid);
        if (bs == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(bs).build();
    }

    @GET
    @Path("/w/{wid}")
    public Response getBookingsbyWorkerId(@PathParam("wid") Integer wid) {

        List<Booking> bs = bookingBean.getBookings_byWorkerId(wid);
        if (bs == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(bs).build();
    }

    @POST
    public Response createBooking(Booking b) {
        if (b.getCustomerid() == null || b.getWorkerid() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            b = bookingBean.createBooking(b);
        }
        return Response.status(Response.Status.CONFLICT).entity(b).build();
    }

    @PUT
    @Path("/{id}")
    public Response putBooking(@PathParam("id") Integer id, Booking b){
        b = bookingBean.putBooking(id, b);
        if (b == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.NOT_MODIFIED).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteBooking(@PathParam("id") Integer id){
        boolean deleted = bookingBean.deleteBooking(id);
        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }





}
