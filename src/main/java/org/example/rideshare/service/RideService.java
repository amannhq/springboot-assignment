package org.example.rideshare.service;

import org.example.rideshare.dto.request.CreateRideRequest;
import org.example.rideshare.dto.response.RideResponse;
import org.example.rideshare.exception.BadRequestException;
import org.example.rideshare.exception.NotFoundException;
import org.example.rideshare.model.Ride;
import org.example.rideshare.repository.RideRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final UserService userService;

    public RideService(RideRepository rideRepository, UserService userService) {
        this.rideRepository = rideRepository;
        this.userService = userService;
    }

    // Create a new ride (Passenger only) - evicts pending rides cache
    @CacheEvict(value = "pendingRides", allEntries = true)
    public RideResponse createRide(CreateRideRequest request) {
        String userId = userService.getCurrentUserId();

        Ride ride = Ride.builder()
                .userId(userId)
                .pickupLocation(request.getPickupLocation())
                .dropLocation(request.getDropLocation())
                .status(Ride.STATUS_REQUESTED)
                .createdAt(new Date())
                .build();

        Ride savedRide = rideRepository.save(ride);
        return mapToResponse(savedRide);
    }

    // Get all rides for the current user (Passenger)
    public List<RideResponse> getUserRides() {
        String userId = userService.getCurrentUserId();
        return rideRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get all pending ride requests (Driver) - Cached for 30 seconds
    @Cacheable(value = "pendingRides")
    public List<RideResponse> getPendingRides() {
        return rideRepository.findByStatus(Ride.STATUS_REQUESTED).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Accept a ride (Driver only) - evicts pending rides cache
    @CacheEvict(value = "pendingRides", allEntries = true)
    public RideResponse acceptRide(String rideId) {
        String driverId = userService.getCurrentUserId();

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found with id: " + rideId));

        // Check if ride is in REQUESTED status
        if (!Ride.STATUS_REQUESTED.equals(ride.getStatus())) {
            throw new BadRequestException("Ride is not in REQUESTED status. Current status: " + ride.getStatus());
        }

        // Assign driver and update status
        ride.setDriverId(driverId);
        ride.setStatus(Ride.STATUS_ACCEPTED);

        Ride updatedRide = rideRepository.save(ride);
        return mapToResponse(updatedRide);
    }

    // Complete a ride (User or Driver)
    public RideResponse completeRide(String rideId) {
        String currentUserId = userService.getCurrentUserId();

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found with id: " + rideId));

        // Check if ride is in ACCEPTED status
        if (!Ride.STATUS_ACCEPTED.equals(ride.getStatus())) {
            throw new BadRequestException("Ride must be in ACCEPTED status to complete. Current status: " + ride.getStatus());
        }

        // Check if the current user is either the passenger or the driver
        if (!currentUserId.equals(ride.getUserId()) && !currentUserId.equals(ride.getDriverId())) {
            throw new BadRequestException("You are not authorized to complete this ride");
        }

        // Update status to completed
        ride.setStatus(Ride.STATUS_COMPLETED);

        Ride updatedRide = rideRepository.save(ride);
        return mapToResponse(updatedRide);
    }

    // Get ride by ID
    public RideResponse getRideById(String rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found with id: " + rideId));
        return mapToResponse(ride);
    }

    // Get all rides for current driver
    public List<RideResponse> getDriverRides() {
        String driverId = userService.getCurrentUserId();
        return rideRepository.findByDriverId(driverId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Map Ride entity to RideResponse DTO
    private RideResponse mapToResponse(Ride ride) {
        return RideResponse.builder()
                .id(ride.getId())
                .userId(ride.getUserId())
                .driverId(ride.getDriverId())
                .pickupLocation(ride.getPickupLocation())
                .dropLocation(ride.getDropLocation())
                .status(ride.getStatus())
                .createdAt(ride.getCreatedAt())
                .build();
    }
}
