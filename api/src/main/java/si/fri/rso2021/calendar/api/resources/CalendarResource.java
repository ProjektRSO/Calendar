package si.fri.rso2021.calendar.api.resources;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;
import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.fri.rso2021.calendar.models.objects.Booking;
import si.fri.rso2021.calendar.models.objects.Worker;

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

import si.fri.rso2021.calendar.services.config.RestProperties;

@Log
@ApplicationScoped
@Path("/calendar")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST")
public class CalendarResource {
    private Logger log = Logger.getLogger(CalendarResource.class.getName());

    //private String url = "http://52.149.170.232:8082/v1/bookings";

    @Inject
    private RestProperties restProperties;

    @Context
    protected UriInfo uriInfo;

    @Inject
    @DiscoverService(value = "bookings-service", environment = "dev", version = "1.0.0")
    private String bookingurl;

    @Inject
    @DiscoverService(value = "workers-service", environment = "dev", version = "1.0.0")
    private String workerurl;

    private List<Booking> makeListRequest(String type, String urlparam) throws IOException {
        String dburl = this.bookingurl;
        log.info("STARTING " + type + " REQUEST " + dburl);
        URL url = new URL(dburl + urlparam);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("accept", "application/json");
        InputStream responseStream = con.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        List<Booking> bookings = mapper.readValue(responseStream, new TypeReference<List<Booking>>(){});
        return bookings;
    }

    private Booking makeObjectRequest(String type, String urlparam) throws IOException {
        String dburl = this.bookingurl;
        log.info("STARTING" + type + "REQUEST " + dburl);
        URL url = new URL(dburl + urlparam);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("accept", "application/json");
        InputStream responseStream = con.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        Booking booking = mapper.readValue(responseStream, Booking.class);
        return booking;
    }

    private List<Worker> makeWorkerListRequest(String type, String urlparam) throws IOException {
        String dburl = this.workerurl;
        log.info("STARTING " + type + " REQUEST " + dburl);
        URL url = new URL(dburl + urlparam);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("accept", "application/json");
        InputStream responseStream = con.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        List<Worker> workers = mapper.readValue(responseStream, new TypeReference<List<Worker>>(){});
        return workers;
    }



//    @Operation(description = "Get all bookings data.", summary = "Get all bookings")
//    @APIResponses({
//            @APIResponse(responseCode = "200",
//                    description = "List of booking data",
//                    content = @Content(schema = @Schema(implementation = Booking.class, type = SchemaType.ARRAY))
//            )})
//    @GET
//    public Response getBookings() throws IOException {
//        List<Booking> bookings = makeListRequest("GET", "");
//        return Response.status(Response.Status.OK).entity(bookings).build();
//    }

    @GET
    public Response getMonth() throws IOException {
        // for each worker get their working days
        // for each worker get their bookings
        // IDEJA -> spremeni booking in worker delovni čas samo v dneve, da se ne rabiš ubadat z urami
        List<Booking> bookings = makeListRequest("GET", "");
        List<Worker> workers = makeWorkerListRequest("GET", "");


        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Operation(description = "Get one booking data by worker id.", summary = "Get one booking")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Booking data",
                    content = @Content(schema = @Schema(implementation = Booking.class, type = SchemaType.ARRAY))
            ),
            @APIResponse( responseCode = "404",
                    description = "Booking with id does not exist"
            )})
    @GET
    @Path("/w/{wid}")
    public Response getBookingsbyWorkerId(@PathParam("wid") Integer wid) throws IOException {

        List<Booking> bs = makeListRequest("GET", String.format("/w/%d", wid));
        if (bs == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(bs).build();
    }


    @Operation(description = "Create one booking.", summary = "Create one booking")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Booking created"
            ),
            @APIResponse( responseCode = "400",
                    description = "Creation not successful"
            )})
    @POST
    public Response createBooking(Booking b) throws IOException {
        if (b.getCustomerid() == null || b.getWorkerid() == null) {
            log.info("BAD POST REQUEST");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        log.info("MAKING POST REQUEST" + b.toString());
        String dburl = restProperties.getBookingsurl();
        URL url = new URL(dburl);
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




}
