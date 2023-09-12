package com.ibsadjobah.bulksms.bulksms.service;

import com.ibsadjobah.bulksms.bulksms.exception.ResourceAlreadyExistException;
import com.ibsadjobah.bulksms.bulksms.exception.ResourceNotFoundException;
import com.ibsadjobah.bulksms.bulksms.model.entities.Customer;
import com.ibsadjobah.bulksms.bulksms.model.entities.Group;
import com.ibsadjobah.bulksms.bulksms.repository.CustomerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;



@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private  CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp(){customerService = new CustomerService(customerRepository);}

    @Test
    void itShouldListEmptyCustomer() {
        //given
        //when
        List<Customer> expected = customerService.all();

        //then
        assertThat(expected).isEmpty();

    }

    @Test
    void itShouldNotEmptyCustomer() {
        //given
        Customer custo = Customer.builder()
                .id(1L)
                .name("sadio")
                .phone("621000000")
                .email("sadio@gmail.com")
                .build();

        List<Customer> data = new LinkedList<>();
        data.add(custo);

        when(customerRepository.findAll()).thenReturn(data);

        //when
        List<Customer> expected = customerService.all();

        //then
        assertThat(expected).isNotEmpty();
        //assertThat(expected)


    }

    @Test
    void itShouldNotDisplayCustomerById() {
        Long customerId= 4L;

        assertThatThrownBy(() ->customerService.show(customerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Le client avec l'ID " +customerId +" n'existe pas");

    }

    @Test
    void itShouldDisplayCustomerById() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("hihii")
                .phone("621111111")
                .email("hihi@gmail.com")
                .build();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        //when
        Customer expected = customerService.show(customer.getId());

        //then
        assertThat(expected).isEqualTo(customer);
    }

    @Test
    void itShouldDeleteCustomerById() {
        Customer customer =Customer.builder()
                .id(1L)
                .name("test")
                .phone("62122222222")
                .email("test@gmail.com")
                .build();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        Customer expected = customerService.delete(customer.getId());

        assertThat(expected).isEqualTo(customer);

    }

    @Test
    void itShouldNotDeleteCustomerById() {
        Long customerId = 43L;

        // When
        // Then
        assertThatThrownBy(() ->customerService.delete(customerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Le client avec l'ID " +customerId +" n'existe pas");
    }

    @Test
    void itShouldCreateCustomer() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("test")
                .phone("6212222222")
                .email("test@gmail.com")
                .build();
        // When
        this.customerService.create(customer);

        //then
        ArgumentCaptor<Customer> argumentCaptor =ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(argumentCaptor.capture());

        Customer expected = argumentCaptor.getValue();
        assertThat(expected).isEqualTo(customer);
    }

    @Test
    void itShouldNotCreateCustomerWhenPhoneAlreadyExist() {
        Customer customer = Customer.builder()
                .id(34L)
                .name("notCreate")
                .phone("621333333")
                .email("notCreate@gmail.com")
                .build();
        when(customerRepository.findByPhone(customer.getPhone())).thenReturn(Optional.of(customer));

        assertThatThrownBy(()-> this.customerService.create(customer))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessage("ce numero de telephone existe déjà");


    }

    @Test
    void itShouldNotCreateCustomerWhenEmailAlreadyExist() {
        Customer customer = Customer.builder()
                .id(35L)
                .name("notCreate")
                .phone("621333333")
                .email("notCreate@gmail.com")
                .build();

        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));

        assertThatThrownBy(()-> this.customerService.create(customer))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessage("cet email exist déjà");
    }

    @Test
    void itShouldUpdateCustomer() {
        Customer customer = Customer.builder()
                .id(3L)
                .name("notUpdate")
                .phone("621333333")
                .email("notCreate@gmail.com")
                .build();

        Customer updateCustomer = Customer.builder()
                .id(3L)
                .name("update")
                .phone("621333333")
                .email("update@gmail.com")
                .build();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        // When
        this.customerService.update(customer.getId(), updateCustomer);

        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);

        // Then
        verify(customerRepository).save(argumentCaptor.capture()) ;

        Customer expected = argumentCaptor.getValue();

        assertThat(expected.getName()).isEqualTo(updateCustomer.getName());


    }

    @Test
    void itShouldNotUpdateWhenCustomerIdDoesntExist() {
        // Given
        // When
        Long customerId = 23L;

        Customer customer = Customer.builder()

                .name("notFound")
                .phone("621333333")
                .email("notFound@gmail.com")
                .build();

        assertThatThrownBy(() -> this.customerService.update(customerId, customer))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Le client avec l'ID " +customerId +" n'existe pas");


    }

    @Test
    void itShouldNotUpdateWhenPhoneAlreadyExist() {
        // Given
        // When
        Customer customer = Customer.builder()
                .id(65L)
                .name("phone")
                .phone("621333333")
                .email("phone@gmail.com")
                .build();

        Customer byPhone = Customer.builder()
                .id(69L)
                .name("phoneExist")
                .phone("621333333")
                .email("phoneExist@gmail.com")
                .build();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerRepository.findByPhone(byPhone.getPhone())).thenReturn(Optional.of(byPhone));

        assertThatThrownBy(() -> this.customerService.update(customer.getId(), byPhone))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessage("Le client avec ce numero de telephone " + customer.getPhone() + " existe déja");



    }

    @Test
    void itShouldNotUpdateWhenEmailAlreadyExist() {
        // Given
        // When
        Customer customer = Customer.builder()
                .id(11L)
                .name("email")
                .phone("621444444")
                .email("emailExist@gmail.com")
                .build();

        Customer byEmail = Customer.builder()
                .id(22L)
                .name("emailExist")
                .phone("621444444")
                .email("emailExist@gmail.com")
                .build();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerRepository.findByEmail(byEmail.getEmail())).thenReturn(Optional.of(byEmail));

        assertThatThrownBy(() -> this.customerService.update(customer.getId(), byEmail))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessage("Le client avec cet email" + customer.getEmail() + " existe déja");
    }
}