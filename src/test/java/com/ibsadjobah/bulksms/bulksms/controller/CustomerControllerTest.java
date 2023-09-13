package com.ibsadjobah.bulksms.bulksms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibsadjobah.bulksms.bulksms.model.entities.Customer;
import com.ibsadjobah.bulksms.bulksms.repository.CustomerRepository;
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
@TestMethodOrder(MethodOrderer.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;

    private final String BASE_URL = "http://127.0.0.1:8080/api/v1";

    private final String contentType ="application/json";

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp(){
        mapper = new ObjectMapper();

        customerRepository.deleteAll();
    }

    @AfterAll
    void clearDatabase(){
        customerRepository.deleteAll();
    }

    @Test
    @Order(1)
    void itShouldListEmptyCustomer() throws Exception{
        // Given
        // When
        // Then
        this.mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + "/customer")
                        .contentType(contentType)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("liste des clients")))
                .andExpect(jsonPath("$.data.clients", hasSize(0)));
    }

    @Test
    @Order(2)
    void itShouldListCustomer() throws Exception{
        //given
        Customer customer1 = Customer.builder()
                .name("sadio")
                .phone("621000000")
                .email("sadio@gmail.com")
                .build();

        Customer customer2 = new Customer();

        customer2.setName("ibrahim");
        customer2.setPhone("621334455");
        customer2.setEmail("ibrahim@gmail.com");

        customerRepository.saveAll(Arrays.asList(customer1, customer2));

        //then
        this.mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + "/customer")
                        .contentType(contentType)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("liste des clients")))
                .andExpect(jsonPath("$.data.clients", hasSize(2)))
                .andExpect(jsonPath("$.data.clients[0].name", is(customer1.getName())))
                .andExpect(jsonPath("$.data.clients[1].name", is(customer2.getName())));

    }

    @Test
    @Order(3)
    void itShouldNotShowCustomerById() throws Exception{

        int customerId = 65;

        // When
        // Then

        this.mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + "/customer/" +customerId)
                        .contentType(contentType)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("Le client avec l'ID " +customerId +" n'existe pas")));

    }

    @Test
    @Order(4)
    void itShouldShowCustomerById() throws Exception{
        //given
        Customer customer = customerRepository.save(Customer.builder()
                .name("sadio")
                .phone("621000000")
                .email("sadio@gmail.com")
                .build());
        // When
        // Then

        this.mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + "/customer/" + customer.getId())
                        .contentType(contentType)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("affichage d'un client")))
                .andExpect(jsonPath("$.data.client.name", is(customer.getName())));

    }

    @Test
    @Order(5)
    void itShouldCreateCustomer() throws  Exception {
        //given
        Customer customer1 = Customer.builder()
                .name("alpha")
                .phone("621445566")
                .email("alpha@gmail.com")
                .build();
        // When
        // Then
        this.mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL + "/customer")
                        .content(mapper.writeValueAsString(customer1))
                        .contentType(contentType)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("ajout d'un nouveau client")))
                .andExpect(jsonPath("$.data.client.name", is(customer1.getName())));

    }

    @Test
    @Order(6)
    void itShouldNotCreateCustomerWhenPhoneAlreadyExist() throws Exception{
        //given
        Customer customer = customerRepository.save(Customer.builder()
                .name("saliou")
                .phone("621778899")
                .email("saliou@gmail.com")
                .build());

        // When
        // Then

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL + "/customer")
                        .content(mapper.writeValueAsString(customer))
                        .contentType(contentType)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(409)))
                .andExpect(jsonPath("$.message",is("ce numero de telephone existe déjà")));
    }

/*
    @Test
    @Order(7)
    void itShouldNotCreateCustomerWhenEmailAlreadyExist() throws Exception{
        //given
        Customer customer = customerRepository.save(Customer.builder()
                .name("messii")
                .phone("661334455")
                .email("messi@gmail.com")
                .build());
        // When
        // Then

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL + "/customer")
                        .content(mapper.writeValueAsString(customer))
                        .contentType(contentType)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code",is(409)))
                .andExpect(jsonPath("$.message", is("cet email exist déjà")));

    }*/

    @Test
    @Order(8)
    void itShouldUpdateCustomerById () throws Exception {

        Customer customer1 = customerRepository.save(Customer.builder()
                .name("saliou")
                .phone("621778899")
                .email("saliou@gmail.com")
                .build());

        Customer customer2 = Customer.builder()
                .name("sadio")
                .phone("621009988")
                .email("sadio@gmail.com")
                .build();

        // When
        // Then

        this.mockMvc.perform(
                MockMvcRequestBuilders.put(BASE_URL + "/customer/" + customer1.getId())
                        .content(mapper.writeValueAsString(customer2))
                        .contentType(contentType)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is(" Mise à jour du client " + customer1.getId())))
                .andExpect(jsonPath("$.data.client.name", is(customer2.getName())));

    }


    @Test
    @Order(9)
    void itShouldNotUpdateWhenPhoneIsIncorrect() throws Exception {
        Customer customer = customerRepository.save(Customer.builder()
                .name("drake")
                .phone("6211111112")
                .email("sadio@gmail.com")
                .build());

        Customer customer1= customerRepository.save(Customer.builder()
                .name("drake")
                .phone("632445566")
                .email("drake@gmail.com")
                .build());

        Customer updateElon = Customer.builder()
                .phone(customer.getPhone())
                .build();

        // When
        // Then

        this.mockMvc.perform(
                MockMvcRequestBuilders.put(BASE_URL + "/customer/" +customer1.getId())
                        .content(mapper.writeValueAsString(updateElon))
                        .contentType(contentType)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("Il y a des erreurs")))
                .andExpect(jsonPath("$.errors.phone",is("le numero de telephone doit obligatoirement avoir que 9 chiffres")));

        }

    @Test
    @Order(10)
    void itShouldNotUpdateWhenCustomerDoesntExist() throws Exception {
        // Given
        Customer customer = Customer.builder()
                .name("hihihi")
                .phone("645889900")
                .email("hihi@gmail.com")
                .build();

        int customerId = 67;
        // When
        // Then

        this.mockMvc.perform(
                MockMvcRequestBuilders.put(BASE_URL + "/customer/" + customerId)
                        .content(mapper.writeValueAsString(customer))
                        .contentType(contentType)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("Le client avec l'ID " +customerId +" n'existe pas")));
    }


    @Test
    @Order(11)
    void itShouldNotUpdateWhenNameIncorrect() throws Exception{

        Customer customer = customerRepository.save(Customer.builder()
                .name("drake")
                .phone("621999999")
                .email("drake@gmail.com")
                .build());

        Customer notUpdate = Customer.builder()
                .name("hi")
                .phone("611223344")
                .email("hi@gmail.com")
                .build();
        // When
        // Then
        this.mockMvc.perform(
                MockMvcRequestBuilders.put(BASE_URL +"/customer/" + customer.getId())
                        .content(mapper.writeValueAsString(notUpdate))
                        .contentType(contentType)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("Il y a des erreurs")))
                .andExpect(jsonPath("$.errors.name", is("le nom doit être compris entre 5 et 50 caractère")));

    }

    @Test
    @Order(12)
    void itShouldNotDeleteCustomerById() throws Exception {
        // Given
        int customerId = 90;
        // When
        // Then

        this.mockMvc.perform(
                MockMvcRequestBuilders.delete(BASE_URL + "/customer/" + customerId)
                        .contentType(contentType)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("Le client avec l'ID " +customerId +" n'existe pas")));
    }

    @Test
    @Order(13)
    void itShouldDeleteCustomerById() throws Exception {
        // Given
        Customer customer = customerRepository.save(Customer.builder()
                .name("ronaldo")
                .phone("654112233")
                .email("ronaldo@gmail.com")
                .build());

        // When
        // Then

        this.mockMvc.perform(
                MockMvcRequestBuilders.delete(BASE_URL + "/customer/" +customer.getId())
                        .contentType(contentType)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("Suppression d'un client " +customer.getId())));
    }

    @Test
    @Order(14)
    void itShouldNotCreateCustomerWhenNameIsIncorrect() throws Exception{
        // Given
        Customer customer = Customer.builder()
                .name("hi")
                .phone("611440077")
                .email("hi@gmail.com")
                .build();

        // When
        // Then

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL + "/customer" )
                        .content(mapper.writeValueAsString(customer))
                        .contentType(contentType)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("Il y a des erreurs")))
                .andExpect(jsonPath("$.errors.name", is("le nom doit être compris entre 5 et 50 caractère")));
    }

    @Test
    @Order(15)
    void itShouldNotCreateWhenPhoneIsIncorrect() throws Exception{
        //given
        Customer customer =Customer.builder()
                .name("messi")
                .phone("6541122334")
                .email("messi@gmail.com")
                .build();
        // When
        // Then
        this.mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL + "/customer")
                        .content(mapper.writeValueAsString(customer))
                        .contentType(contentType)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code",is(400)))
                .andExpect(jsonPath("$.message", is("Il y a des erreurs")))
                .andExpect(jsonPath("$.errors.phone", is("le numero de telephone doit obligatoirement avoir que 9 chiffres")));

    }

    @Test
    @Order(16)
    void itShouldNotUpdateWhenPhoneAlreadyExist() throws Exception {
        Customer customer = customerRepository.save(Customer.builder()
                .name("elonmusk")
                .phone("643112233")
                .email("elon@gmail.com")
                .build());

        Customer customer1 = customerRepository.save(Customer.builder()
                .name("billgate")
                .phone("623558800")
                .email("bill@gmail.com")
                .build());

        Customer updateCust = Customer.builder()
                .name(customer.getName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .build();

        // When
        // Then
        this.mockMvc.perform(
                MockMvcRequestBuilders.put(BASE_URL + "/customer/" +customer1.getId())
                        .content(mapper.writeValueAsString(updateCust))
                        .contentType(contentType)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(409)))
                .andExpect(jsonPath("$.message", is("Le client avec ce numero de telephone " +customer.getPhone()+ " existe déja")));

    }

     /*
    @Test
    @Order(17)
    void itShouldNotUpdateWhenEmailAlreadyExist() throws Exception {
        Customer customer = customerRepository.save(Customer.builder()
                .name("elonmusk")
                .phone("643112233")
                .email("elon@gmail.com")
                .build());

        Customer customer1 = customerRepository.save(Customer.builder()
                .name("billgate")
                .phone("623558800")
                .email("bill@gmail.com")
                .build());

        Customer updateCust = Customer.builder()
                .name(customer.getName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .build();

        // When
        // Then
        this.mockMvc.perform(
                MockMvcRequestBuilders.put(BASE_URL + "/customer/" +customer1.getId())
                        .content(mapper.writeValueAsString(updateCust))
                        .contentType(contentType)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(409)))
                .andExpect(jsonPath("$.message", is("Le client avec cet email" +updateCust.getEmail()+ " existe déja")));

    }*/



}
