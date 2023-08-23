package com.ibsadjobah.bulksms.bulksms.repository;

import com.ibsadjobah.bulksms.bulksms.model.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String name);
}
