package com.gridnine.testing;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Flight> unhandledFlights = FlightBuilder.createFlights();

        FilterModule filterModule = new FilterModule(unhandledFlights);

        System.out.println("Excluded flights with departure until the current time:");
        filterModule.checkActualDeparture().getFlights().forEach(System.out::println);

        System.out.println("\n" + "Excluded flights with expired date of the departure:");
        filterModule.checkCorrectDate().getFlights().forEach(System.out::println);

        System.out.println("\n" + "Excluded flights with too long transfers:");
        filterModule.excludeLongTransfer().getFlights().forEach(System.out::println);

        System.out.println("\n" + "All filters are enabled:");
        filterModule.checkActualDeparture().checkCorrectDate().excludeLongTransfer().getFlights().forEach(System.out::println);

    }
}
