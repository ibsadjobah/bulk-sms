package com.ibsadjobah.bulksms.bulksms.repository;

import com.ibsadjobah.bulksms.bulksms.model.entities.Campagne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampagneRepository extends JpaRepository<Campagne, Long> {
    Optional<Campagne> findByRef(String ref);
}
