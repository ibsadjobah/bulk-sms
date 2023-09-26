package com.ibsadjobah.bulksms.bulksms.repository;

import com.ibsadjobah.bulksms.bulksms.model.entities.Destinataire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinataireRepository extends JpaRepository<Destinataire, Long> {
}
