package com.gridnine.testing;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FlightFilterTest {

    private FilterModule filterModule;
    private final static List<Flight> expectedList;
    private final static List<Flight> testFlights;
    private final static Flight flightWithExpiredDateTime;
    private final static Flight flightWithIncorrectDate;
    private final static Flight flightWithTransferMoreThenTwoHours;

    static {
        expectedList = new ArrayList<Flight>(){{
            add(new Flight(new ArrayList<Segment>(){{
                add(new Segment(LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(4)));
            }}));
            add(new Flight(new ArrayList<Segment>(){{
                add(new Segment(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2)));
                add(new Segment(LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(4)));
                add(new Segment(LocalDateTime.now().plusHours(5), LocalDateTime.now().plusHours(6)));
            }}));
        }};
        testFlights = new ArrayList<>(expectedList);
        flightWithExpiredDateTime = new Flight(new ArrayList<Segment>(){{
            add(new Segment(LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(4)));
        }});
        flightWithIncorrectDate = new Flight(new ArrayList<Segment>(){{
            add(new Segment(LocalDateTime.now().plusHours(2), LocalDateTime.now().minusDays(1)));
        }});
        flightWithTransferMoreThenTwoHours = new Flight(new ArrayList<Segment>(){{
            add(new Segment(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2)));
            add(new Segment(LocalDateTime.now().plusHours(5), LocalDateTime.now().plusHours(6)));
        }});
    }

    @Test
    void shouldFilterFlightsWithExpiredDepartureDate() {
        testFlights.add(flightWithExpiredDateTime);
        filterModule = new FilterModule(testFlights);

        assertEquals(expectedList, filterModule.checkActualDeparture().getFlights());

        testFlights.remove(flightWithExpiredDateTime);
    }

    @Test
    void shouldFilterFlightsWithIncorrectDate() {
        testFlights.add(flightWithIncorrectDate);
        filterModule = new FilterModule(testFlights);

        assertEquals(expectedList, filterModule.checkCorrectDate().getFlights());

        testFlights.remove(flightWithIncorrectDate);
    }

    @Test
    void shouldFilterFlightsWithTransferMoreThenTwoHours() {
        testFlights.add(flightWithTransferMoreThenTwoHours);
        filterModule = new FilterModule(testFlights);

        assertEquals(expectedList, filterModule.excludeLongTransfer().getFlights());

        testFlights.remove(flightWithTransferMoreThenTwoHours);
    }
}
