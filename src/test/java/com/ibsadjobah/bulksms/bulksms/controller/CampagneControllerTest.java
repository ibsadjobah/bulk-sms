package com.ibsadjobah.bulksms.bulksms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibsadjobah.bulksms.bulksms.model.entities.Campagne;
import com.ibsadjobah.bulksms.bulksms.model.entities.Customer;
import com.ibsadjobah.bulksms.bulksms.repository.CampagneRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class CampagneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private final String BASE_URL = "http://127.0.0.1:8080/api/v1";

    private final String contentType ="application/json";


    @Autowired
    private  CampagneRepository campagneRepository;

    @BeforeEach
    void setUp(){
        objectMapper = new ObjectMapper();
    }

    @AfterAll
    void clearDatabase(){
        campagneRepository.deleteAll();
    }

    @Test
    @Order(1)
    void itShouldNotListCampagne() throws Exception {

        // Given
        // When
        // Then
        this.mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + "/campagnes")
                        .contentType(contentType)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("La liste de campganes")))
                .andExpect(jsonPath("$.data.campagnes", hasSize(0)));


    }

    @Test
    @Order(2)
    void itShouldListCampagne() throws Exception{

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        LocalDateTime scheduleAt = LocalDateTime.parse("2023-04-28T12:00:01.545", formatter);

        Campagne campagne = Campagne.builder()
                .ref("AZZE112")
                .message("merci de bien developper ")
                .type("positif")
                .schedule_at(scheduleAt)
                .build();

        campagneRepository.saveAll(Arrays.asList(campagne));

        //then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/campagnes")
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("La liste de campagnes")))
                .andExpect(jsonPath("$.data.campagnes", hasSize(1)))
                .andExpect(jsonPath("$.data.campagnes[0].ref", is(campagne.getRef())));

    }

    @Test
    @Order(3)
    void itShouldNotShowCampagneById() throws Exception {
        int campagneId = 65;

        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/campagnes/" +campagneId)
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("La campagne " + campagneId + "n'existe pas")));

    }

    @Test
    @Order(4)
    void itShouldShowCampagneById() throws  Exception{

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        LocalDateTime scheduleAt = LocalDateTime.parse("2023-04-28T12:00:01.545", formatter);

        Campagne campagne = campagneRepository.save(Campagne.builder()
                .ref("AZZE112")
                .message("merci de bien developper ")
                .type("positif")
                .schedule_at(scheduleAt)
                .build());

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/campagnes/" + campagne.getId())
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("Affichage d'une campagne")))
                .andExpect(jsonPath("$.data.campagne.ref", is(campagne.getRef())));

    }

    /*
    @Test
    void itSouldCreateCampagne() throws Exception{

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        LocalDateTime scheduleAt = LocalDateTime.parse("2023-04-28T12:00:01.545", formatter);

        Campagne campagne = Campagne.builder()
                .ref("AZZE112")
                .message("merci de bien developper ")
                .type("positif")
                .schedule_at(scheduleAt)
                .build();

        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL + "/campagnes")
                                .content(objectMapper.writeValueAsString(campagne))
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("Creation d'une campagne")))
                .andExpect(jsonPath("$.data.campagne.ref", is(campagne.getRef())));

    }*/

    @Test
    @Order(5)
    void itSouldNotDeleteCampagneById() throws Exception {

        int campagneId = 90;
        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete(BASE_URL + "/campagnes/" + campagneId)
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("La campagne " + campagneId + "n'existe pas")));

    }

    @Test
    @Order(6)
    void itShouldDeleteCampagneById() throws Exception {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        LocalDateTime scheduleAt = LocalDateTime.parse("2023-04-28T12:00:01.545", formatter);


        Campagne campagne = campagneRepository.save(Campagne.builder()
                .ref("AZZE112")
                .message("merci de bien developper ")
                .type("positif")
                .schedule_at(scheduleAt)
                .build());

        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete(BASE_URL + "/campagnes/" + campagne.getId())
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("Suppression d'une campagne" +campagne.getId())));

    }


}
