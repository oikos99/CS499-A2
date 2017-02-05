package com.cs499.a2.repository;

import com.cs499.a2.domain.Instructor;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Instructor entity.
 */
@SuppressWarnings("unused")
public interface InstructorRepository extends JpaRepository<Instructor,Long> {

}
