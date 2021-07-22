package com.mmt.assignment.mmtassignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Response Model Class
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FastestRoutesResponse {

    private Map<String,String> fastestRouteMap;
}
