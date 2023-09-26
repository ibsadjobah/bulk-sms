package com.ibsadjobah.bulksms.bulksms.model.requests;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Getter
@Setter
public class CampagneRequest {

    @Length(min = 6, max = 10, message = "La reference doit obligatoirement etre compris entre 6 et 10 carateres")
    private String ref;

    private String message;

    private String type;

    private LocalDateTime schedule_at;
}
