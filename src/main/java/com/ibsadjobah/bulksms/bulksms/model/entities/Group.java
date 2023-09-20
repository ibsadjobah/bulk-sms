package com.ibsadjobah.bulksms.bulksms.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    @Column(name = "group_id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "group")
    private List<Customer> customer;

  // @OneToMany(mappedBy = "group")
   //@OneToMany(cascade = CascadeType.ALL)
   //@JoinColumn(name = "fk_group_id", referencedColumnName = "group_id")
    //private List<Customer> customer ;


}
