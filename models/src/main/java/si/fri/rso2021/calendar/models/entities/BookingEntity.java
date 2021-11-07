package si.fri.rso2021.calendar.models.entities;
import javax.persistence.*;

@Entity
@Table(name = "bookings")
@NamedQueries(value =
        {
                @NamedQuery(name = "BookingEntity.getAll", query = "SELECT b FROM BookingEntity b"),
                @NamedQuery(name = "BookingEntity.getByCustomerId", query = "SELECT b FROM BookingEntity b WHERE b.customerid = :customerid"),
                @NamedQuery(name = "BookingEntity.getByWorkerId", query = "SELECT b FROM BookingEntity b WHERE b.workerid = :workerid"),
        })
public class BookingEntity implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "t_from")
    private long from;

    @Column(name = "t_to")
    private long to;

    @Column(name = "workerid")
    private Integer workerid;

    @Column(name = "customerid")
    private Integer customerid;

    public Integer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
    }

    public Integer getWorkerid() {
        return workerid;
    }

    public void setWorkerid(Integer workerid) {
        this.workerid = workerid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }
}
