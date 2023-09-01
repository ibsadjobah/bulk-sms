package com.ibsadjobah.bulksms.bulksms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibsadjobah.bulksms.bulksms.model.entities.Group;
import com.ibsadjobah.bulksms.bulksms.repository.GroupRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
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
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;

    private final String BASE_URL = "http://127.0.0.1:8080/api/v1";
    private final String contentType = "application/json";

    @Autowired
    private GroupRepository groupRepository;

    @BeforeEach
    void setUp() {

        mapper = new ObjectMapper();

        groupRepository.deleteAll();

    }

    @AfterAll
    void clearDatabase() {
        groupRepository.deleteAll();

    }

    @Test
    @Order(1)
    void itShouldListEmptyGroup() throws Exception {
        // Given
        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/groups")
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("Liste des groupes")))
                .andExpect(jsonPath("$.data.groupes", hasSize(0)));

    }

    @Test
    @Order(2)
    void itShouldListGroups() throws Exception {
        // Given

        Group group1 = Group.builder()
                .name("Frontend")
                .build();

        Group group2 = new Group();
        group2.setName("Backend");

        Group group3 = new Group();
        group3.setName("DevOps");

        groupRepository.saveAll(Arrays.asList(group1, group2, group3));

        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/groups")
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("Liste des groupes")))
                .andExpect(jsonPath("$.data.groupes", hasSize(3)))
                .andExpect(jsonPath("$.data.groupes[0].name", is(group1.getName())))
                .andExpect(jsonPath("$.data.groupes[1].name", is(group2.getName())))
                .andExpect(jsonPath("$.data.groupes[2].name", is(group3.getName())));

    }

    @Test
    @Order(3)
    void itShouldnotShowGrouById() throws Exception {
        // Given
        int groupId = 30;
        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/groups/" +groupId)
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("Le groupe avec l'ID "+groupId+" n'existe pas")));

    }

    @Test
    @Order(4)
    void itShouldShowGrouById() throws Exception {
        // Given


        Group react = groupRepository.save(Group.builder()
                .name("React")
                .build());
        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/groups/" +react.getId())
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("Affichage du groupe " +react.getName())))
                .andExpect(jsonPath("$.data.groupe.name", is(react.getName())));

    }

    @Test
    @Order(5)
    void itShouldCreateNewGroup() throws Exception {
        // Given
        Group angular = Group.builder()
                .name("Angular")
                .build();

        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL + "/groups")
                                .content(mapper.writeValueAsString(angular))
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("Ajouut d'un nouveau groupe")))
                .andExpect(jsonPath("$.data.groupe.name", is(angular.getName())));

    }


    @Test
    @Order(6)
    void itShouldNotCreateGroupWhenNameAlreadyExist() throws Exception {
        // Given
        Group vue = groupRepository.save(Group.builder()
                .name("Vue")
                .build());

        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL + "/groups")
                                .content(mapper.writeValueAsString(vue))
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(409)))
                .andExpect(jsonPath("$.message", is("Ce nom existe deja")));

    }



    @Test
    @Order(7)
    void itShouldUpdateGroup() throws Exception {
        // Given
        Group spring = groupRepository.save(Group.builder()
                .name("Spring")
                .build());

        Group springBoot = Group.builder()
                .name("Spring boot")
                .build();
        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.put(BASE_URL + "/groups/" +spring.getId())
                                .content(mapper.writeValueAsString(springBoot))
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("Mise à jour du groupe " +spring.getId())))
                .andExpect(jsonPath("$.data.groupe.name", is(springBoot.getName())));

    }

    @Test
    @Order(8)
    void itShouldNotUpdateGroupWhenNameAlreadyExist() throws Exception {
        // Given
        Group android = groupRepository.save(Group.builder()
                .name("Android")
                .build());

        Group iphone = groupRepository.save(Group.builder()
                .name("Iphone")
                .build());

        Group updateIphone = Group.builder()
                .name(android.getName())
                .build();

        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.put(BASE_URL + "/groups/" +iphone.getId())
                                .content(mapper.writeValueAsString(updateIphone))
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(409)))
                .andExpect(jsonPath("$.message", is("Le groupe avec pour nom " +updateIphone.getName()+ " existe déja")));

    }

    @Test
    @Order(9)
    void itShouldnotUpdateWhenGroupDoesntExist() throws Exception {
        // Given
        Group windows = Group.builder()
                .name("Windows")
                .build();

        int groupId = 67;
        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.put(BASE_URL + "/groups/" +groupId)
                                .content(mapper.writeValueAsString(windows))
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("Le groupe avec l'ID "+groupId+" n'existe pas")));

    }

    @Test
    @Order(10)
    void itShouldnotUpdateWhenGroupNameIsIncorrect() throws Exception {
        Group spring = groupRepository.save(Group.builder()
                .name("Spring")
                .build());

        Group springBoot = Group.builder()
                .name("Sp")
                .build();
        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.put(BASE_URL + "/groups/" +spring.getId())
                                .content(mapper.writeValueAsString(springBoot))
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("Il y a des erreurs")))
                .andExpect(jsonPath("$.errors.name", is("Le nom doit obligatoirement etre compris entre 3 et 30 carateres")));

    }



    @Test
    @Order(11)
    void itShouldnotDeleteGrouById() throws Exception {
        // Given
        int groupId = 30;
        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete(BASE_URL + "/groups/" +groupId)
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("Le groupe avec l'ID "+groupId+" n'existe pas")));

    }

    @Test
    @Order(12)
    void itShouldDeleteGrouById() throws Exception {
        // Given
        Group samsung = groupRepository.save(Group.builder()
                .name("Samsung")
                .build());
        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete(BASE_URL + "/groups/" +samsung.getId())
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is(" Suppression du groupe " +samsung.getId())));

    }

    @Test
    @Order(13)
    void itShouldnotCreateWhenGroupNameIsIncorrect() throws Exception {
        // Given
        Group wi = Group.builder()
                .name("Wi")
                .build();


        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL + "/groups")
                                .content(mapper.writeValueAsString(wi))
                                .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("Il y a des erreurs")))
                .andExpect(jsonPath("$.errors.name", is("Le nom doit obligatoirement etre compris entre 3 et 30 carateres")));

    }

}
