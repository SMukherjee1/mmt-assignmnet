package com.mmt.assignment.mmtassignment.controller;

import com.mmt.assignment.mmtassignment.dto.FastestRoutesResponse;
import com.mmt.assignment.mmtassignment.iface.ServiceAPI;
import com.mmt.assignment.mmtassignment.service.FlightDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author s0m01xw
 * <p>
 * Controller for onboarding and authorization of users
 */
@RestController
public class RouteComputeController implements ServiceAPI {
    @Autowired
    FlightDetailsService flightDetailsService;
    @Override
    public ResponseEntity<List<FastestRoutesResponse>> getFlightDetails(@RequestParam String routesFile) {
        try {
           Set<FastestRoutesResponse> setResponse = flightDetailsService.getFlightDetails(routesFile);
            return new ResponseEntity<>(new ArrayList<>(setResponse), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
