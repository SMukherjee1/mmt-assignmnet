package com.mmt.assignment.mmtassignment.iface;

import com.mmt.assignment.mmtassignment.dto.FastestRoutesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * API Definition for Team Onboarding Service.
 *
 * @author s0m01xw
 * @since 18/09/2019
 */

@RequestMapping(path = "/v1/mmt")
public interface ServiceAPI {

    /**
     * Get all flight details
     *
     *
     * @return ResponseEntity
     */
    @GetMapping(value = "/fastestRoutes")
    ResponseEntity<List<FastestRoutesResponse>> getFlightDetails(
            @RequestParam String routesFile);
}
