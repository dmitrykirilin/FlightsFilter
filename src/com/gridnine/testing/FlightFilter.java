package com.gridnine.testing;

import java.util.List;

public interface FlightFilter {
    FlightFilter checkActualDeparture();

    FlightFilter checkCorrectDate();

    FlightFilter excludeLongTransfer();

    List<Flight> getFlights();
}
