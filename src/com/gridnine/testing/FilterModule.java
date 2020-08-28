package com.gridnine.testing;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class FilterModule implements FlightFilter{

    private static List<Flight> unhandledFlights;

    private List<Flight> flights;

    public FilterModule(List<Flight> flights) {
        unhandledFlights = flights;
        this.flights = flights;
    }

    @Override
    public FlightFilter checkActualDeparture() {
        this.flights = flights.parallelStream().filter(f -> f.getSegments().get(0).getDepartureDate().isAfter(LocalDateTime.now())).collect(Collectors.toList());
        return this;
    }

    @Override
    public FlightFilter checkCorrectDate() {
        this.flights = flights.parallelStream().filter(f -> f.getSegments().stream().allMatch(s -> s.getDepartureDate().isBefore(s.getArrivalDate()))).collect(Collectors.toList());
        return this;
    }

    @Override
    public FlightFilter excludeLongTransfer() {
        CopyOnWriteArrayList<Flight> flights1 = new CopyOnWriteArrayList<>(this.flights);
        flights1.parallelStream().forEach(flight -> {
            if(flight.getSegments().size() > 1){
                for (int i = 0; i < flight.getSegments().size() - 1; i++) {
                    Segment curSegment = flight.getSegments().get(i);
                    Segment nextSegment = flight.getSegments().get(i + 1);
                    if(curSegment.getArrivalDate().until(nextSegment.getDepartureDate(), ChronoUnit.HOURS) > 2){
                        flights1.remove(flight);
                    }
                }
            }
        });
        this.flights = new ArrayList<>(flights1);
        return this;
    }

    @Override
    public List<Flight> getFlights() {
        List<Flight> filteredFlights = flights;
        flights = unhandledFlights;
        return filteredFlights;
    }
}
