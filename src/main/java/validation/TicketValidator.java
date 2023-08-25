package validation;

import entity.Ticket;
import entity.TicketList;

public class TicketValidator {
    public ValidationResult checkTickets(TicketList ticketList) {
        if (ticketList == null || ticketList.getTickets() == null || ticketList.getTickets().isEmpty()) {
            return new ValidationResult(false, "no tickets");
        }
        int counter = 1;
        for (Ticket t : ticketList.getTickets()) {
            if (t.getOrigin() == null || t.getOrigin().isBlank()) {
                return new ValidationResult(false, "no origin in the ticket number " + counter);
            }
            if (t.getDestination() == null || t.getDestination().isBlank()) {
                return new ValidationResult(false, "no destination in the ticket number " + counter);
            }
            if (t.getDeparture_date() == null || t.getDeparture_date().isBlank()) {
                return new ValidationResult(false, "no departure_date in the ticket number " + counter);
            }
            if (t.getDeparture_time() == null || t.getDeparture_time().isBlank()) {
                return new ValidationResult(false, "no departure_time in the ticket number " + counter);
            }
            if (t.getArrival_date() == null || t.getArrival_date().isBlank()) {
                return new ValidationResult(false, "no arrival_date in the ticket number " + counter);
            }
            if (t.getArrival_time() == null || t.getArrival_time().isBlank()) {
                return new ValidationResult(false, "no arrival_time in the ticket number " + counter);
            }
            if (t.getCarrier() == null || t.getCarrier().isBlank()) {
                return new ValidationResult(false, "no carrier in the ticket number " + counter);
            }

            if (t.getPrice() <= 0) {
                return new ValidationResult(false, "no price in the ticket number " + counter);
            }

            counter++;
        }
        return new ValidationResult(true, "");
    }
}
