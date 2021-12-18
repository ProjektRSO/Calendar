package si.fri.rso2021.calendar.api.resources;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;
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
import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ConfigBundle("bookings-url")
@ApplicationScoped
@Path("/calendar")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CalendarResource {
    private Logger log = Logger.getLogger(CalendarResource.class.getName());

    @ConfigValue(watch = true)
    private String url = "http://20.81.50.151:8082/v1/bookings";

    @Inject
    private BookingBean bookingBean;

    @Context
    protected UriInfo uriInfo;

    private List<Booking> makeListRequest(String type, String urlparam) throws IOException {
        log.info("STARTING" + type + "REQUEST " + this.url);
        URL url = new URL(this.url + urlparam);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("accept", "application/json");
        InputStream responseStream = con.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        List<Booking> bookings = mapper.readValue(responseStream, new TypeReference<List<Booking>>(){});
        return bookings;
    }

    private Booking makeObjectRequest(String type, String urlparam) throws IOException {
        log.info("STARTING" + type + "REQUEST " + this.url);
        URL url = new URL(this.url + urlparam);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("accept", "application/json");
        InputStream responseStream = con.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        Booking booking = mapper.readValue(responseStream, Booking.class);
        return booking;
    }

    @GET
    public Response getBookings() throws IOException {
        List<Booking> bookings = makeListRequest("GET", "");
        return Response.status(Response.Status.OK).entity(bookings).build();
    }

    @GET
    @Path("/{id}")
    public Response getBookingbyId(@PathParam("id") Integer id) throws IOException {
        Booking w = makeObjectRequest("GET", String.format("/%d", id));
        if (w == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(w).build();
    }

    @GET
    @Path("/c/{cid}")
    public Response getBookingsbyCustomerId(@PathParam("cid") Integer cid) throws IOException {
        List<Booking> bs = makeListRequest("GET", String.format("/c/%d", cid));
        if (bs == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(bs).build();
    }

    @GET
    @Path("/w/{wid}")
    public Response getBookingsbyWorkerId(@PathParam("wid") Integer wid) throws IOException {

        List<Booking> bs = makeListRequest("GET", String.format("/w/%d", wid));
        if (bs == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(bs).build();
    }

    @POST
    public Response createBooking(Booking b) throws IOException {
        if (b.getCustomerid() == null || b.getWorkerid() == null) {
            log.info("BAD POST REQUEST");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        log.info("MAKING POST REQUEST" + b.toString());
        URL url = new URL(this.url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setDoOutput(true);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonstring = ow.writeValueAsString(b);
        log.info("JSON " + jsonstring);
        try(DataOutputStream wr = new DataOutputStream( con.getOutputStream())) {
            wr.write(jsonstring.getBytes(StandardCharsets.UTF_8));
        }
        log.info("STATUS " + con.getResponseMessage());
        return Response.status(Response.Status.CREATED).entity(b).build();
    }
//
//    @PUT
//    @Path("/{id}")
//    public Response putBooking(@PathParam("id") Integer id, Booking b){
//        b = bookingBean.putBooking(id, b);
//        if (b == null) {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//        return Response.status(Response.Status.NOT_MODIFIED).build();
//    }
//
//    @DELETE
//    @Path("{id}")
//    public Response deleteBooking(@PathParam("id") Integer id){
//        boolean deleted = bookingBean.deleteBooking(id);
//        if (deleted) {
//            return Response.status(Response.Status.NO_CONTENT).build();
//        }
//        else {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//    }





}
