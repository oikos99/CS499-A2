package com.cs499.a2.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cs499.a2.domain.OfficeHour;

import com.cs499.a2.repository.OfficeHourRepository;
import com.cs499.a2.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing OfficeHour.
 */
@RestController
@RequestMapping("/api")
public class OfficeHourResource {

    private final Logger log = LoggerFactory.getLogger(OfficeHourResource.class);
        
    @Inject
    private OfficeHourRepository officeHourRepository;

    /**
     * POST  /office-hours : Create a new officeHour.
     *
     * @param officeHour the officeHour to create
     * @return the ResponseEntity with status 201 (Created) and with body the new officeHour, or with status 400 (Bad Request) if the officeHour has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/office-hours")
    @Timed
    public ResponseEntity<OfficeHour> createOfficeHour(@RequestBody OfficeHour officeHour) throws URISyntaxException {
        log.debug("REST request to save OfficeHour : {}", officeHour);
        if (officeHour.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("officeHour", "idexists", "A new officeHour cannot already have an ID")).body(null);
        }
        OfficeHour result = officeHourRepository.save(officeHour);
        return ResponseEntity.created(new URI("/api/office-hours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("officeHour", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /office-hours : Updates an existing officeHour.
     *
     * @param officeHour the officeHour to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated officeHour,
     * or with status 400 (Bad Request) if the officeHour is not valid,
     * or with status 500 (Internal Server Error) if the officeHour couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/office-hours")
    @Timed
    public ResponseEntity<OfficeHour> updateOfficeHour(@RequestBody OfficeHour officeHour) throws URISyntaxException {
        log.debug("REST request to update OfficeHour : {}", officeHour);
        if (officeHour.getId() == null) {
            return createOfficeHour(officeHour);
        }
        OfficeHour result = officeHourRepository.save(officeHour);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("officeHour", officeHour.getId().toString()))
            .body(result);
    }

    /**
     * GET  /office-hours : get all the officeHours.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of officeHours in body
     */
    @GetMapping("/office-hours")
    @Timed
    public List<OfficeHour> getAllOfficeHours() {
        log.debug("REST request to get all OfficeHours");
        List<OfficeHour> officeHours = officeHourRepository.findAll();
        return officeHours;
    }

    /**
     * GET  /office-hours/:id : get the "id" officeHour.
     *
     * @param id the id of the officeHour to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the officeHour, or with status 404 (Not Found)
     */
    @GetMapping("/office-hours/{id}")
    @Timed
    public ResponseEntity<OfficeHour> getOfficeHour(@PathVariable Long id) {
        log.debug("REST request to get OfficeHour : {}", id);
        OfficeHour officeHour = officeHourRepository.findOne(id);
        return Optional.ofNullable(officeHour)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /office-hours/:id : delete the "id" officeHour.
     *
     * @param id the id of the officeHour to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/office-hours/{id}")
    @Timed
    public ResponseEntity<Void> deleteOfficeHour(@PathVariable Long id) {
        log.debug("REST request to delete OfficeHour : {}", id);
        officeHourRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("officeHour", id.toString())).build();
    }

}
