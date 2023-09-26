package com.ibsadjobah.bulksms.bulksms.controller;

import com.ibsadjobah.bulksms.bulksms.model.HttpResponse;
import com.ibsadjobah.bulksms.bulksms.model.entities.Destinataire;
import com.ibsadjobah.bulksms.bulksms.model.requests.DestinataireRequest;
import com.ibsadjobah.bulksms.bulksms.model.responses.DestinataireResponse;
import com.ibsadjobah.bulksms.bulksms.service.DestinataireService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/destinataires")
public class DestinataireController {

    private final DestinataireService destinataireService;

    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<HttpResponse> list(){

        List<Destinataire> destinataires = destinataireService.all();

        List<DestinataireResponse> data = destinataires.stream()
                .map(destinataire -> DestinataireResponse.builder()
                        .id(destinataire.getId())
                        .status(destinataire.getStatus())
                        .build())
                .toList();

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("La liste de destinataire")
                .data(Map.of("Destinataire", data))
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);
    }

    @GetMapping("{destinataireId}")
    public ResponseEntity<HttpResponse> show(@PathVariable("destinataireId") Long destinataireId)
    {
        DestinataireResponse data = modelMapper.map(destinataireService.show(destinataireId), DestinataireResponse.class);


        HttpResponse httpResponse =  HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Affichage d'un destinaire")
                .data(Map.of("Destinataire", data))
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);
    }

    @PostMapping
    public ResponseEntity<HttpResponse> create(@RequestBody DestinataireRequest destinataireRequest){
        Destinataire destinataire = new Destinataire();

        destinataire.setStatus(destinataireRequest.getStatus());

        DestinataireResponse data = modelMapper.map(destinataireService.create(destinataire), DestinataireResponse.class);

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Ajouter un destinataire")
                .data(Map.of("Destinataire", data))
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);

    }

    @PutMapping("{destinataireId}")
    public ResponseEntity<HttpResponse> update(@PathVariable("destinataireId") Long destinataireId, @RequestBody DestinataireRequest destinataireRequest){

        Destinataire update = destinataireService.update(destinataireId, modelMapper.map(destinataireRequest, Destinataire.class));

        DestinataireResponse data = modelMapper.map(update, DestinataireResponse.class);

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Mise à jour d'un destinataire")
                .data(Map.of("Destinataire", data))
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);

    }

    @DeleteMapping("{destinataireId}")
    public ResponseEntity<HttpResponse> delete(@PathVariable("destinataireId") Long destinataireId)
    {
        destinataireService.delete(destinataireId);

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Suppression d'un destinataire")
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);
    }
}
