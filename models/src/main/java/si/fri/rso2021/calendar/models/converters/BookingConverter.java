package si.fri.rso2021.calendar.models.converters;

import si.fri.rso2021.calendar.models.entities.BookingEntity;
import si.fri.rso2021.calendar.models.objects.Booking;

public class BookingConverter {
    public static Booking toDto(BookingEntity entity) {
        Booking dto = new Booking();
        dto.setFrom(entity.getFrom());
        dto.setTo(entity.getTo());
        dto.setId(entity.getId());
        dto.setCustomerid(entity.getCustomerid());
        dto.setWorkerid(entity.getWorkerid());
        return dto;
    }
    public static BookingEntity toEntity(Booking dto) {

        BookingEntity entity = new BookingEntity();
        entity.setFrom(dto.getFrom());
        entity.setTo(dto.getTo());
        entity.setId(dto.getId());
        entity.setCustomerid(dto.getCustomerid());
        entity.setWorkerid(dto.getWorkerid());
        return entity;

    }
}
