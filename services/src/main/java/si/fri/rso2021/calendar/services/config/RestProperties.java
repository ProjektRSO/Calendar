package si.fri.rso2021.calendar.services.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;
import javax.enterprise.context.ApplicationScoped;


// consul kv put environments/dev/services/calendar-service/1.0.0/config/rest-properties/maintenance-mode false or true
// consul kv put environments/dev/services/calendar-service/1.0.0/config/rest-properties/bookingsurl http://52.149.170.232:8082/v1/bookings -> NO QUOTES!

@ConfigBundle("rest-properties")
@ApplicationScoped
public class RestProperties {

    @ConfigValue(watch = true)
    private Boolean maintenanceMode;

    @ConfigValue(watch = true)
    private Boolean broken;

    @ConfigValue(watch = true)
    private String bookingsurl;

    @ConfigValue(watch = true)
    private String workersurl;

    public Boolean getMaintenanceMode() {
        return this.maintenanceMode;
    }

    public void setMaintenanceMode(final Boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }

    public Boolean getBroken() {
        return broken;
    }

    public void setBroken(final Boolean broken) {
        this.broken = broken;
    }

    public String getBookingsurl() {
        return bookingsurl;
    }

    public void setBookingsurl(String bookingsurl) {
        this.bookingsurl = bookingsurl;
    }

    public String getWorkersurl() {
        return workersurl;
    }

    public void setWorkersurl(String workersurl) {
        this.workersurl = workersurl;
    }
}
