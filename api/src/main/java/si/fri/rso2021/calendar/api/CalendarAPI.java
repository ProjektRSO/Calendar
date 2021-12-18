package si.fri.rso2021.calendar.api;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(info = @Info(title = "Calendar API", version = "v1",
        license = @License(name = "David"), description = "API for displaying availability data."),
        servers = @Server(url = "http://localhost:8081/"))
@ApplicationPath("/v1")
public class CalendarAPI extends Application {
}
