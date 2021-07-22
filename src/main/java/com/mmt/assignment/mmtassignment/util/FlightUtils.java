package com.mmt.assignment.mmtassignment.util;

import com.mmt.assignment.mmtassignment.dto.FlightDetails;

import java.util.*;

public class FlightUtils {

    /**
     * Utility method to filter the flights with the fastest route amongst list of flights
     *
     * @param responseMap key-> ATQ_DEL_BLR, Value->{123_156:207, 205_109:100}   //just an example
     * @return Map<String, String> key-> ATQ_DEL_BLR, Value-> 05_109:100
     */
    public static Map<String, String> filterFastestRoutes(Map<String, List<String>> responseMap) {

        Map<String, String> fastestRouteMap = new HashMap<>();
        for (Map.Entry entry : responseMap.entrySet()) {
            List<String> travelRoutes = (List<String>) entry.getValue();
            int minTravelTime = Integer.MAX_VALUE;
            String fastestRoute = "";
            for (String travelRoute : travelRoutes) {
                String travelTime = travelRoute.split(":")[1];
                if (minTravelTime > Integer.valueOf(travelTime)) {
                    fastestRoute = travelRoute;
                    minTravelTime = Integer.valueOf(travelTime);
                }
            }
            fastestRouteMap.put((String) entry.getKey(), fastestRoute);
        }
        return fastestRouteMap;
    }

    /**
     * Utility to add all the connecting flights to a map of lists
     * <p>
     * ex: key-> ATQ_DEL_BLR, Value->{123_156:207, 205_109:100}
     *
     * @param sourceAdjFlights ATQ_DEL
     * @param connectingFlight DEL_BLR
     * @param result           Map
     * @param visited          set of Flight numbers that contains all visited flights
     * @param source           source flight i.e : ATQ
     */
    public static void addConnectingFlightsToMap(FlightDetails sourceAdjFlights,
                                                 FlightDetails connectingFlight,
                                                 Map<String, List<String>> result,
                                                 Set<String> visited,
                                                 FlightDetails source) {

        String route = sourceAdjFlights.getSourceAirportCode() //ATQ
                + "_" + connectingFlight.getSourceAirportCode() //DEL
                + "_" + connectingFlight.getDestinationAirportCode();//BLR

        String flightNumber = sourceAdjFlights.getFlightNo()
                + "_" + connectingFlight.getFlightNo();

        int timeDuration = Integer.parseInt(sourceAdjFlights.getTimeDuration())
                + Integer.parseInt(connectingFlight.getTimeDuration())
                + FlightTimeDurationUtils.flightTimingDifference(sourceAdjFlights.getEnd()
                , connectingFlight.getStart());
        //add to map
        if (result.containsKey(route)) {
            List<String> travelDetails = result.get(route);
            travelDetails.add(flightNumber + ":" + timeDuration);
            visited.add(source.getFlightNo());
        } else {
            List<String> travelDetails = new ArrayList<>();
            travelDetails.add(flightNumber + ":" + timeDuration);
            result.put(route, travelDetails);
            visited.add(source.getFlightNo());
        }

    }

    /**
     * Validate the connecting flights
     *
     * 1. Flight should not be already visited
     * 2. source airport and destination airport should not be same(DEL->DEL)
     * 3. destination airport of the flight should be same as destination passed as input
     *
     * @param connectingFlight Connecting flight
     * @param visited          Visted Set
     * @param destination      Destination flight numvre
     * @return
     */
    public static boolean validateConnectingFlights(FlightDetails connectingFlight,
                                                    Set<String> visited, String destination) {
        return !visited.contains(connectingFlight.getFlightNo())
                && !connectingFlight.getSourceAirportCode()
                .equals(connectingFlight.getDestinationAirportCode())
                && connectingFlight.getDestinationAirportCode().equals(destination);
    }
}
