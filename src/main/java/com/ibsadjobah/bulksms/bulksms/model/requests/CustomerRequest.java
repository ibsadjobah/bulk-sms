package com.ibsadjobah.bulksms.bulksms.model.requests;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class CustomerRequest {

    @Length(min = 5, max = 50, message = "le nom doit être compris entre 5 et 50 caractère")
    private String name;

    @Length(min = 9, max=9, message = "le numero de telephone doit obligatoirement avoir que 9 chiffres")
    private String phone;

    @Email(message = "veuiller entrer une addresse email valide")
    @NotBlank(message = "l'addresse email ne peut pas être vide")
    @Pattern(regexp = ".*@gmail\\.com$", message = "L'adresse e-mail doit se terminer par @gmail.com")
    private String email;
}
