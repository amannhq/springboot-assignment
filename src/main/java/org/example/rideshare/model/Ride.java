package org.example.rideshare.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rides")
@CompoundIndexes({
    @CompoundIndex(name = "status_createdAt_idx", def = "{'status': 1, 'createdAt': -1}"),
    @CompoundIndex(name = "userId_status_idx", def = "{'userId': 1, 'status': 1}"),
    @CompoundIndex(name = "driverId_status_idx", def = "{'driverId': 1, 'status': 1}")
})
public class Ride {

    @Id
    private String id;

    @Indexed
    private String userId;      // Passenger (FK to User)

    @Indexed
    private String driverId;    // Driver (FK to User, nullable until accepted)

    private String pickupLocation;

    private String dropLocation;

    @Indexed
    private String status;      // REQUESTED, ACCEPTED, COMPLETED

    private Date createdAt;

    // Status constants
    public static final String STATUS_REQUESTED = "REQUESTED";
    public static final String STATUS_ACCEPTED = "ACCEPTED";
    public static final String STATUS_COMPLETED = "COMPLETED";
}
