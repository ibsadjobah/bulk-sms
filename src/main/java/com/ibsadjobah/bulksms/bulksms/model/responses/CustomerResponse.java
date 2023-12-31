package com.ibsadjobah.bulksms.bulksms.model.responses;

import com.ibsadjobah.bulksms.bulksms.model.entities.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {

    private Long id;
    private String name;
    private String phone;
    private String email;
    private Group group_id;
}

