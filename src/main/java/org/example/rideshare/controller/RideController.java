package org.example.rideshare.controller;

import jakarta.validation.Valid;
import org.example.rideshare.dto.request.CreateRideRequest;
import org.example.rideshare.dto.response.RideResponse;
import org.example.rideshare.service.RideService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    /**
     * Create a new ride request (USER only)
     * POST /api/v1/rides
     */
    @PostMapping("/rides")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<RideResponse> createRide(@Valid @RequestBody CreateRideRequest request) {
        RideResponse response = rideService.createRide(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get current user's rides (USER only)
     * GET /api/v1/user/rides
     */
    @GetMapping("/user/rides")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<RideResponse>> getUserRides() {
        List<RideResponse> rides = rideService.getUserRides();
        return ResponseEntity.ok(rides);
    }

    /**
     * Complete a ride (USER or DRIVER)
     * POST /api/v1/rides/{rideId}/complete
     */
    @PostMapping("/rides/{rideId}/complete")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_DRIVER')")
    public ResponseEntity<RideResponse> completeRide(@PathVariable String rideId) {
        RideResponse response = rideService.completeRide(rideId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get ride by ID
     * GET /api/v1/rides/{rideId}
     */
    @GetMapping("/rides/{rideId}")
    public ResponseEntity<RideResponse> getRideById(@PathVariable String rideId) {
        RideResponse response = rideService.getRideById(rideId);
        return ResponseEntity.ok(response);
    }
}
