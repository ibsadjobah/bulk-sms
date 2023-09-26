package com.ibsadjobah.bulksms.bulksms.controller;

import com.ibsadjobah.bulksms.bulksms.model.HttpResponse;
import com.ibsadjobah.bulksms.bulksms.model.entities.Campagne;
import com.ibsadjobah.bulksms.bulksms.model.requests.CampagneRequest;
import com.ibsadjobah.bulksms.bulksms.model.responses.CampagneResponse;
import com.ibsadjobah.bulksms.bulksms.service.CampagneService;
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
@RequestMapping("api/v1/campagnes")
@RequiredArgsConstructor
@ApiOperation("api/v1/campagnes")
@Slf4j
public class CampagneController {

    private final CampagneService campagneService;

    private final ModelMapper modelMapper;


    @GetMapping
    @ApiOperation(value = "Liste des campagnes", notes = "Affiche la liste de toutes les campagnes", response = HttpResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Liste des campagnes")
    })
   public ResponseEntity<HttpResponse> list(){

        log.info("La liste des campagnes");
       List<Campagne> campagnes = campagneService.all();

       List<CampagneResponse> data = campagnes.stream()
               .map(campagne -> CampagneResponse.builder()
                       .id(campagne.getId())
                       .ref(campagne.getRef())
                       .message(campagne.getMessage())
                       .type(campagne.getType())
                       .schedule_at(campagne.getSchedule_at())
                       .build())
               .toList();

       HttpResponse httpResponse = HttpResponse.builder()
               .code(HttpStatus.OK.value())
               .message("La liste de campagnes")
               .data(Map.of("campagnes", data))
               .build();

       return ResponseEntity.ok()
               .body(httpResponse);
   }

   @GetMapping("{campagneId}")
   @ApiOperation(value = "Affichage d'une campagne à partir de son ID", response = HttpResponse.class)
   @ApiResponses(value = {
           @ApiResponse(code = 200, message = "Affichage d'une campagnes"),
           @ApiResponse(code = 404, message = "ID de la campagne n'a pas été retrouvée"),
   })
   public ResponseEntity<HttpResponse> show(@PathVariable("campagneId") Long campagneId){

       log.info("Affichage d'une campagne");
        CampagneResponse data = modelMapper.map(campagneService.show(campagneId), CampagneResponse.class);

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Affichage d'une campagne")
                .data(Map.of("campagne", data))
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);
   }


   @PostMapping
   @ApiOperation(value = "Creation d'une campagnes", response = HttpResponse.class)
   @ApiResponses(value = {
           @ApiResponse(code = 200, message = "Creation d'une nouvelle campagne"),
           @ApiResponse(code = 400, message = "Cette reference existe déjà"),
   })
   public ResponseEntity<HttpResponse> create(@RequestBody CampagneRequest campagneRequest){

       log.info("Creation d'une campagne");
        Campagne campagne = new Campagne();

        campagne.setRef(campagneRequest.getRef());
        campagne.setMessage(campagneRequest.getMessage());
        campagne.setType(campagneRequest.getType());
        campagne.setSchedule_at(campagneRequest.getSchedule_at());

        CampagneResponse data = modelMapper.map(campagneService.create(campagne), CampagneResponse.class);

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Creation d'une campagne")
                .data(Map.of(" campagne", data))
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);

   }

   @PutMapping("{campagneId}")
   @ApiOperation(value = "Mise à jour d'une campagne", response = HttpResponse.class)
   @ApiResponses(value = {
           @ApiResponse(code = 200, message = "Mise à jour d'une campagne"),
           @ApiResponse(code = 400, message = "Cette reference existe déjà"),
   })
   public ResponseEntity<HttpResponse> update(@PathVariable("campagneId") Long campagneId, @RequestBody CampagneRequest campagneRequest){

       log.info("Mise à jour d'une campagne");
      Campagne updateCampagne = campagneService.update(campagneId, modelMapper.map(campagneRequest, Campagne.class));

      CampagneResponse data = modelMapper.map(updateCampagne, CampagneResponse.class);

      HttpResponse httpResponse = HttpResponse.builder()
              .code(HttpStatus.OK.value())
              .message("Mise à jour d'une campagne")
              .data(Map.of("Campagne", data))
              .build();

      return ResponseEntity.ok()
              .body(httpResponse);
   }


   @DeleteMapping("{campagneId}")
   @ApiOperation(value = "Suprression d'une campagne", response = HttpResponse.class)
   @ApiResponses(value = {
           @ApiResponse(code = 200, message = "Suppression d'une campagne"),
           @ApiResponse(code = 404, message = "Id de cette campagne n'a pas été retrouvée")
   })
   public ResponseEntity<HttpResponse> delete(@PathVariable("campagneId") Long campagneId){

       log.info("Suppression d'une campagne");
        campagneService.delete(campagneId);

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Suppression d'une campagne" + campagneId )
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);
   }


}
