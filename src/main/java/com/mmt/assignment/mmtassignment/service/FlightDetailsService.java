package com.mmt.assignment.mmtassignment.service;

import com.mmt.assignment.mmtassignment.dto.FlightDetails;
import com.mmt.assignment.mmtassignment.dto.FastestRoutesResponse;
import com.mmt.assignment.mmtassignment.util.FlightTimeDurationUtils;
import com.mmt.assignment.mmtassignment.util.FlightUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

/**
 * Service to compute the fastest flight Routes
 */
@Service
@Slf4j
public class FlightDetailsService {
    private Map<String, List<FlightDetails>> flightAdjacencyMap = new HashMap<>();

    /**
     * Method to compute fastest routes of every source to dest present in file
     *
     * @param file Input csv provided
     * @return Response
     * @throws IOException
     */
    public Set<FastestRoutesResponse> getFlightDetails(String file) throws IOException {
        List<FlightDetails> list = fetchFileDetailsList(file);
        convertToAdjacencyMap(list);
        return new HashSet<>(computefastestRoutes());
    }

    /**
     * Method to compute fastest routes of every source to dest present in file
     *
     * @return
     */
    private List<FastestRoutesResponse> computefastestRoutes() {
        List<FastestRoutesResponse> results = new ArrayList<>();
        for (Map.Entry entry : flightAdjacencyMap.entrySet()) {
            List<FlightDetails> destinationList = (List<FlightDetails>) entry.getValue();
            for (FlightDetails source : destinationList) {
                Map<String, List<String>> responseMap = new HashMap<>();
                computeRouteMap(source, source.getDestinationAirportCode(),
                        responseMap,
                        new HashSet<>());
                Map<String, String> filteredMinTravelDuration = FlightUtils.filterFastestRoutes(responseMap);
                FastestRoutesResponse fastestRoutesResponse = new FastestRoutesResponse();
                fastestRoutesResponse.setFastestRouteMap(filteredMinTravelDuration);
                results.add(fastestRoutesResponse);
            }
        }
        return results;
    }

    /**
     * Method to compute the Response Map
     * <p>
     * Add the onestop flights to the map
     * 1. get the adjcency list of the source, the direct flights.
     * 2. Then for each direct flights (which will be now source and dest as same)
     * get the adjacency list
     * with destination equal to dest
     * <p>
     * 3. Eg: source-> A, Destination->J
     * Adjacency list of A {A->{B,D,J}}
     * Adjacency list of B {B->{E,J}}
     * We need to calculate time travel from A->B->J
     */
    private void computeRouteMap(FlightDetails source, String dest,
                                 Map<String, List<String>> result, Set<String> visited) {

        if (source.getDestinationAirportCode().equals(dest)) {//add direct flights to map
            String route = source.getSourceAirportCode() + "_" + dest;
            List<String> travelDetails = new ArrayList<>();
            travelDetails.add(source.getFlightNo() + ":" + Integer.parseInt(source.getTimeDuration()));
            result.put(route, travelDetails);
            visited.add(source.getFlightNo());//Add flight number to visted set
        }
        for (FlightDetails sourceAdjFlights : flightAdjacencyMap.get(source.getSourceAirportCode())) {
            if (!visited.contains(sourceAdjFlights.getFlightNo())
                    && !sourceAdjFlights.getSourceAirportCode()
                    .equals(sourceAdjFlights.getDestinationAirportCode())) {// we get B-J, and not B-E
                //get adjacency list of B and you print A->B->C, where A is the source and C is the destination
                for (FlightDetails connectingFlight : flightAdjacencyMap
                        .get(sourceAdjFlights.getDestinationAirportCode())) {
                    /**Validate if a person can take the next connecting flight
                     * departure and arrival time gap should be greater than or equal to 120 mins**/
                    if (FlightUtils.validateConnectingFlights(connectingFlight, visited, dest)) {
                        if (!FlightTimeDurationUtils.isValidConnectingFlight(sourceAdjFlights.getEnd(),
                                connectingFlight.getStart())) {
                            continue;
                        }
                        FlightUtils.addConnectingFlightsToMap(sourceAdjFlights,
                                connectingFlight, result, visited, source);
                    }
                }
            }
        }
    }

    /**
     * Convert the List of flight details retrieved from csv into
     * flight Adjacency Map where,
     * <p>
     * Key-> Source Airport Code
     * Value-> list of all direct flights from the source airport
     *
     * @param list List<FlightDetails></FlightDetails>
     */
    private void convertToAdjacencyMap(List<FlightDetails> list) {
        for (FlightDetails flight : list) {
            if (flightAdjacencyMap.containsKey(flight.getSourceAirportCode())) {
                List<FlightDetails> lists = flightAdjacencyMap
                        .get(flight.getSourceAirportCode());
                lists.add(flight);
            } else {
                List<FlightDetails> lists = new ArrayList<>();
                lists.add(flight);
                flightAdjacencyMap.put(flight.getSourceAirportCode(), lists);
            }
        }
    }

    /**
     * Map the flight details read from csv into Model class
     *
     * @param file input file
     * @return List<FlightDetails>
     * @throws IOException
     */
    private List<FlightDetails> fetchFileDetailsList(String file) throws IOException {
        List<FlightDetails> detailList = new ArrayList<>();
        List<String> productLines = Files.readAllLines(java.nio.file.Paths.get(file),
                StandardCharsets.UTF_8);

        for (String line : productLines) {
            FlightDetails details = new FlightDetails();
            String[] tokens = line.split(",");

            details.setFlightNo(tokens[0]);
            details.setSourceAirportCode(tokens[1]);
            details.setDestinationAirportCode(tokens[2]);
            details.setStart(tokens[3]);
            details.setEnd(tokens[4]);
            details.setTimeDuration(details.calculateFlightDuration());
            detailList.add(details);
        }
        return detailList;
    }

   /* public static void main(String[] args) throws IOException {
        getFlightDetails("/Users/s0m01xw/Downloads/ivtest-sched.csv");
    }*/

}
