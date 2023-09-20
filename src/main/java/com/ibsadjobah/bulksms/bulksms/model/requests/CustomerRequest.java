package com.ibsadjobah.bulksms.bulksms.model.requests;

import com.ibsadjobah.bulksms.bulksms.model.entities.Group;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class CustomerRequest {

    @Length(min = 5, max = 50, message = "le nom doit être compris entre 5 et 50 caractère")
    private String name;

    @Length(min = 9, max=9, message = "le numero de telephone doit obligatoirement avoir que 9 chiffres")
    private String phone;

    @Email(message = "veuiller entrer une addresse email valide")
    private String email;

    @NotNull(message = "Le group du client est obligatoire")
    private Integer groupId;
}
