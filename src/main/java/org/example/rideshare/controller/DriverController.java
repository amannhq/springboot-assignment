package org.example.rideshare.controller;

import org.example.rideshare.dto.response.RideResponse;
import org.example.rideshare.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/driver")
@PreAuthorize("hasAuthority('ROLE_DRIVER')")
public class DriverController {

    private final RideService rideService;

    public DriverController(RideService rideService) {
        this.rideService = rideService;
    }

    /**
     * Get all pending ride requests (DRIVER only)
     * GET /api/v1/driver/rides/requests
     */
    @GetMapping("/rides/requests")
    public ResponseEntity<List<RideResponse>> getPendingRides() {
        List<RideResponse> rides = rideService.getPendingRides();
        return ResponseEntity.ok(rides);
    }

    /**
     * Accept a ride (DRIVER only)
     * POST /api/v1/driver/rides/{rideId}/accept
     */
    @PostMapping("/rides/{rideId}/accept")
    public ResponseEntity<RideResponse> acceptRide(@PathVariable String rideId) {
        RideResponse response = rideService.acceptRide(rideId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get driver's accepted/completed rides
     * GET /api/v1/driver/rides
     */
    @GetMapping("/rides")
    public ResponseEntity<List<RideResponse>> getDriverRides() {
        List<RideResponse> rides = rideService.getDriverRides();
        return ResponseEntity.ok(rides);
    }
}
