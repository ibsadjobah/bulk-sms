package com.ibsadjobah.bulksms.bulksms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibsadjobah.bulksms.bulksms.model.entities.Destinataire;
import com.ibsadjobah.bulksms.bulksms.model.entities.Group;
import com.ibsadjobah.bulksms.bulksms.repository.DestinataireRepository;
import com.ibsadjobah.bulksms.bulksms.service.DestinataireService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class DestinataireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private final String BASE_URL = "http://127.0.0.1:8080/api/v1";
    private final String contentType = "application/json";

    @Autowired
    private  DestinataireRepository destinataireRepository;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();

        destinataireRepository.deleteAll();

    }

    @AfterAll
    void clearDatabase() {
        destinataireRepository.deleteAll();

    }

    @Test
    @Order(1)
    void itShouldListEmptyDestinataire() throws Exception{
        // Given
        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/destinataires")
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("La liste de destinataire")))
                .andExpect(jsonPath("$.data.Destinataire", hasSize(0)));

    }

    @Test
    @Order(2)
    void itShouldListDestinataire()  throws Exception{

        Destinataire destinataire1 = Destinataire.builder()
                .status("delivre")
                .build();

        Destinataire destinataire2 = new Destinataire();
        destinataire2.setStatus("ouvert");


        destinataireRepository.saveAll(Arrays.asList(destinataire1, destinataire2));

        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/destinataires")
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("La liste de destinataire")))
                .andExpect(jsonPath("$.data.Destinataire", hasSize(2)))
                .andExpect(jsonPath("$.data.Destinataire[0].status", is(destinataire1.getStatus())))
                .andExpect(jsonPath("$.data.Destinataire[1].status", is(destinataire2.getStatus())));

    }

    @Test
    @Order(3)
    void itShouldShowDestinataireById() throws Exception{
        // Given


        Destinataire destinataire = destinataireRepository.save(Destinataire.builder()
                .status("en attente")
                .build());
        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/destinataires/" + destinataire.getId())
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("Affichage d'un destinaire")))
                .andExpect(jsonPath("$.data.Destinataire.status", is(destinataire.getStatus())));

    }

    @Test
    @Order(4)
    void itShouldCreateDestinataire() throws Exception{
        // Given
        Destinataire destinataire = Destinataire.builder()
                .status("envoye")
                .build();

        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL + "/destinataires")
                                .content(objectMapper.writeValueAsString(destinataire))
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("Ajout d'un destinataire")))
                .andExpect(jsonPath("$.data.Destinataire.status", is(destinataire.getStatus())));

    }

    @Test
    @Order(5)
    void itShouldUpdateDestinataire() throws Exception{
        // Given
        Destinataire destinataire = destinataireRepository.save(Destinataire.builder()
                        .status("delivre")
                        .build());

        Destinataire destinataireUpdate = Destinataire.builder()
                .status("update")
                .build();
        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.put(BASE_URL + "/destinataires/" +destinataire.getId())
                                .content(objectMapper.writeValueAsString(destinataireUpdate))
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("Mise à jour d'un destinataire" )))
                .andExpect(jsonPath("$.data.Destinataire.status", is(destinataireUpdate.getStatus())));

    }

    @Test
    @Order(6)
    void itShouldNotUpdateWhenDestinataireDoesntExist() throws Exception {
        // Given
        Destinataire windows = Destinataire.builder()
                .status("notUpdate")
                .build();

        int destinataireId = 67;
        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.put(BASE_URL + "/destinataires/" +destinataireId)
                                .content(objectMapper.writeValueAsString(windows))
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("Le destinataire avec cet ID " + destinataireId + " n'existe pas ")));

    }
}
