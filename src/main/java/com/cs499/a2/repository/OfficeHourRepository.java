package com.cs499.a2.repository;

import com.cs499.a2.domain.OfficeHour;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OfficeHour entity.
 */
@SuppressWarnings("unused")
public interface OfficeHourRepository extends JpaRepository<OfficeHour,Long> {

}
