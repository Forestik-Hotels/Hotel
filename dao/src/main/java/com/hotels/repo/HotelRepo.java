package com.hotels.repo;

import com.hotels.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepo extends JpaRepository<Hotel, Long> {

    /**
     * Find hotel by name.
     *
     * @param name of hotel
     * @return {@link Hotel}
     */
    Optional<Hotel> findByName(String name);
}
