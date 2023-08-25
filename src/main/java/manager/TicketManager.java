package manager;

import entity.Ticket;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TicketManager {
    public List<Ticket> filterByDestination(List<Ticket> tickets, String... destinations) {
        List<String> destinationsList = Arrays.asList(destinations);
        return tickets.stream()
                .filter(t -> (destinationsList.contains(t.getDestination())))
                .collect(Collectors.toList());
    }

    public List<Ticket> filterByOrigin(List<Ticket> tickets, String... origins) {
        List<String> originsList = Arrays.asList(origins);
        return tickets.stream()
                .filter(t -> (originsList.contains(t.getOrigin())))
                .collect(Collectors.toList());
    }

    public List<Double> getPrices(List<Ticket> tickets) {
        return tickets.stream()
                .map(Ticket::getPrice)
                .collect(Collectors.toList());
    }

    public double getAveragePrice(List<Ticket> tickets) {
        return tickets.stream()
                .mapToDouble(Ticket::getPrice)
                .average()
                .orElse(0.0);
    }

    public Map<String, List<Ticket>> groupByCarrier(List<Ticket> tickets) {
        return tickets.stream().collect(Collectors.groupingBy(Ticket::getCarrier));
    }
}
