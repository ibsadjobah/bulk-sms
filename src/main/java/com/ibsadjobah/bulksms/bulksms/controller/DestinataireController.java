package com.ibsadjobah.bulksms.bulksms.controller;

import com.ibsadjobah.bulksms.bulksms.model.HttpResponse;
import com.ibsadjobah.bulksms.bulksms.model.entities.Destinataire;
import com.ibsadjobah.bulksms.bulksms.model.requests.DestinataireRequest;
import com.ibsadjobah.bulksms.bulksms.model.responses.DestinataireResponse;
import com.ibsadjobah.bulksms.bulksms.service.DestinataireService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/destinataires")
@Slf4j
@ApiOperation("api/v1/destinataires")
public class DestinataireController {

    private final DestinataireService destinataireService;

    private final ModelMapper modelMapper;

    @GetMapping
    @ApiOperation(value = "Liste des destinataires", notes = "Affiche la liste de tous les destinataires", response = HttpResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Liste des destinataires")
    })
    public ResponseEntity<HttpResponse> list(){

        log.info("La liste de destinataire");
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
    @ApiOperation(value = "Affichage d'un destinataire à partir de son ID", response = HttpResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Affichage d'un destinataire"),
            @ApiResponse(code = 404, message = "ID du destinaire n'a pas été retrouvé"),
    })
    public ResponseEntity<HttpResponse> show(@PathVariable("destinataireId") Long destinataireId)
    {
        log.info("Affichage d'un destinataire");
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
    @ApiOperation(value = "Ajout d'un destinataire", response = HttpResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ajout d'un nouveau destinataire"),
    })
    public ResponseEntity<HttpResponse> create(@RequestBody DestinataireRequest destinataireRequest){

        log.info("Ajout d'un nouveau destinataire");
        Destinataire destinataire = new Destinataire();

        destinataire.setStatus(destinataireRequest.getStatus());

        DestinataireResponse data = modelMapper.map(destinataireService.create(destinataire), DestinataireResponse.class);

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Ajout d'un destinataire")
                .data(Map.of("Destinataire", data))
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);

    }

    @PutMapping("{destinataireId}")
    @ApiOperation(value = "Mise à jour d'un destinataire", response = HttpResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Mise à jour du destinataire"),
    })
    public ResponseEntity<HttpResponse> update(@PathVariable("destinataireId") Long destinataireId, @RequestBody DestinataireRequest destinataireRequest){

        log.info("Mise à jour du destinataire");
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
    @ApiOperation(value = "Suprression d'un destinataire", response = HttpResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suppression du destinataire"),
            @ApiResponse(code = 404, message = "Id du destinataire n'a pas été retrouvé")
    })
    public ResponseEntity<HttpResponse> delete(@PathVariable("destinataireId") Long destinataireId)
    {
        log.info("Suppresion d'un destinataire");
        destinataireService.delete(destinataireId);

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Suppression d'un destinataire")
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);
    }
}
