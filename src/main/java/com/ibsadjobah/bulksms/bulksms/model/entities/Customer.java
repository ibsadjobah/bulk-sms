package com.ibsadjobah.bulksms.bulksms.model.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "group_id")
    private Long groupId;


    @ManyToOne(targetEntity = Group.class, fetch = FetchType.EAGER, cascade  = CascadeType.PERSIST)
    @JoinColumn(name = "group_id", referencedColumnName = "group_id", nullable = false, insertable = false, updatable = false)
    private Group group;

   /* @ManyToOne()
    private Group group;*/


    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
