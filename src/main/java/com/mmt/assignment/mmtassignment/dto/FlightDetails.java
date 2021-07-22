package com.mmt.assignment.mmtassignment.dto;

import com.mmt.assignment.mmtassignment.util.FlightTimeDurationUtils;
import lombok.*;

/**
 * Flight Model class
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class FlightDetails {

    private String flightNo;
    private String sourceAirportCode;
    private String destinationAirportCode;
    private String start;
    private String end;
    private String timeDuration;


    /**
     * Calculate the time duration in minutes
     * @return
     */
    public String calculateFlightDuration() {
        int startMinutes = FlightTimeDurationUtils
                .calculateMinutes(Integer.parseInt(start));
        int endMinutes = FlightTimeDurationUtils
                .calculateMinutes(Integer.parseInt(end));
        if (endMinutes - startMinutes < 0) {
            endMinutes = endMinutes + 24 * 60;
        }
        return Integer.toString(endMinutes - startMinutes);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final FlightDetails other = (FlightDetails) obj;
        if (!this.sourceAirportCode.equals(other.sourceAirportCode)) {
            return false;
        }
        if (!this.destinationAirportCode.equals(other.destinationAirportCode)) {
            return false;
        }
        return true;
    }


    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.sourceAirportCode != null
                ? this.sourceAirportCode.hashCode() : 0);
        hash = 53 * hash + (this.destinationAirportCode != null
                ? this.destinationAirportCode.hashCode() : 0);
        return hash;
    }

}
