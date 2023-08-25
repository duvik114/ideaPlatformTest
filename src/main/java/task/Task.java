package task;

import entity.Ticket;
import entity.TicketList;
import json.JsonTicketParser;
import manager.TicketManager;
import validation.TicketValidator;
import validation.ValidationResult;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;

public class Task {

//    The time difference between Vladivostok and Tel Aviv in minutes
    private final int fromVVOtoTLVminutes = 7 * 60;

    public Map<String, Long> getMinFlightTime(Map<String, List<Ticket>> carrierTickets) {
        Map<String, Long> res = new HashMap<>();
        for (Map.Entry<String, List<Ticket>> e : carrierTickets.entrySet()) {
            long minDiff = Long.MAX_VALUE;
            for (Ticket t : e.getValue()) {
                DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                        .appendPattern("dd.MM.")
                        .appendValueReduced(ChronoField.YEAR, 2, 4, 2000)
                        .appendPattern(" ")
                        .appendValueReduced(ChronoField.HOUR_OF_DAY, 1, 2, 0)
                        .appendPattern(":mm")
                        .toFormatter();
                String fromDate = t.getDeparture_date() + " " + t.getDeparture_time();
                String toDate = t.getArrival_date() + " " + t.getArrival_time();
                LocalDateTime from = LocalDateTime.parse(fromDate, formatter);
                LocalDateTime to = LocalDateTime.parse(toDate, formatter);
                long minutesDiff = Duration.between(from, to).toMinutes();
                minDiff = Math.min(minutesDiff, minDiff);
            }
            res.put(e.getKey(), minDiff + fromVVOtoTLVminutes);
        }
        return res;
    }

    public void makeTask(String fileName) {
        TicketList tickets;
        try {
            tickets = new JsonTicketParser().parseTickets(fileName);
        } catch (IOException e) {
            System.err.println("Cannot read input json file: " + e.getMessage());
            return;
        }

        TicketValidator ticketValidator = new TicketValidator();
        ValidationResult res = ticketValidator.checkTickets(tickets);
        if (!res.isStatus()) {
            System.err.println("Not valid json objects in file: " + res.getMessage());
            return;
        }

        List<Ticket> ticketList = tickets.getTickets();
        TicketManager ticketManager = new TicketManager();
        ticketList = ticketManager.filterByOrigin(ticketList, "VVO");
        ticketList = ticketManager.filterByDestination(ticketList, "TLV");
        if (ticketList.isEmpty()) {
            System.err.println("No suitable tickets in json file!");
            return;
        }

        DoubleStream sortedPrices = ticketManager.getPrices(ticketList).stream().mapToDouble(Double::doubleValue).sorted();
        double median = ticketList.size() % 2 == 0
                ? sortedPrices.skip(ticketList.size() / 2 - 1).limit(2).average().getAsDouble()
                : sortedPrices.skip(ticketList.size() / 2).findFirst().getAsDouble();
        double avgPrice = ticketManager.getAveragePrice(ticketList);

        Map<String, List<Ticket>> carrierTickets = ticketManager.groupByCarrier(ticketList);
        Map<String, Long> carrierBestFlightTime = getMinFlightTime(carrierTickets);

        System.out.println("Results:");
        System.out.println();
        for (Map.Entry<String, Long> e : carrierBestFlightTime.entrySet()) {
            System.out.println("Min time flight for '" + e.getKey() + "' is " + e.getValue() / 60 + " hours and " + e.getValue() % 60 + " minutes");
            System.out.println();
        }

        System.out.println();
        System.out.println("Difference between Average price and median: " + (avgPrice - median));
    }
}
