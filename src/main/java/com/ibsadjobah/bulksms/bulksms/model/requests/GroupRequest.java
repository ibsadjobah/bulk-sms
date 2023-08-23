package com.ibsadjobah.bulksms.bulksms.model.requests;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class GroupRequest {

    @Length(min = 3, max = 30, message = "Le nom doit obligatoirement etre compris entre 3 et 30 carateres")
    private String name;
}
