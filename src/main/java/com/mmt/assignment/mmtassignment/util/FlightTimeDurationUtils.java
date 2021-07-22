package com.mmt.assignment.mmtassignment.util;

/**
 * Utility class for flight time duration calculation
 */
public class FlightTimeDurationUtils {

    public static boolean isValidConnectingFlight(String arrival, String departure) {
        int arrivalMinutes = calculateMinutes(Integer.parseInt(arrival));
        int depMinutes = calculateMinutes(Integer.parseInt(departure));
        if (depMinutes - arrivalMinutes < 0) {
            depMinutes = depMinutes + 24 * 60;
        }
        return (depMinutes - arrivalMinutes < 120) ? false : true;
    }

    /**
     * Method to calculate the time interval between arrival of one
     * flight and departure of the next connecting flight
     *
     * @param arrival   Arrival time of first flight (end of source flight)
     * @param departure Departure of second flight(start of next connecting flight)
     * @return
     */
    public static int flightTimingDifference(String arrival, String departure) {
        int arrivalMinutes = calculateMinutes(Integer.parseInt(arrival));
        int depMinutes = calculateMinutes(Integer.parseInt(departure));
        if (depMinutes - arrivalMinutes < 0) {
            depMinutes = depMinutes + 24 * 60; // means departure is next day
        }
        return depMinutes - arrivalMinutes;
    }

    public static int calculateMinutes(int milTime) {
        double time = milTime / 100d;
        int hours = (int) Math.floor(time);
        int minutes = milTime % 100;
        return (hours * 60) + minutes;
    }
}
