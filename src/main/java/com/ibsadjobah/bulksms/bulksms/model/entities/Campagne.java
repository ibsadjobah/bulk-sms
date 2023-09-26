package com.ibsadjobah.bulksms.bulksms.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "campagnes")
public class Campagne {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ref", nullable = false, unique = true)
    private String ref;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String type;

    private LocalDateTime schedule_at;

    private Timestamp created_at;

    private Timestamp updated_at;
}
