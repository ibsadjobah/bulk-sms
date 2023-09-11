package com.ibsadjobah.bulksms.bulksms.service;

import com.ibsadjobah.bulksms.bulksms.exception.ResourceAlreadyExistException;
import com.ibsadjobah.bulksms.bulksms.exception.ResourceNotFoundException;
import com.ibsadjobah.bulksms.bulksms.model.entities.Customer;
import com.ibsadjobah.bulksms.bulksms.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<Customer> all(){

        log.info("Liste des clients");
        return customerRepository.findAll();

    }

    public Customer show(Long customerId){
        log.info("affichage du client" +customerId);
        Optional<Customer> optionalCustomer = customerFindById(customerId);

        return optionalCustomer.get();
    }

    public Customer create(Customer customer){

        log.info("creation d'un client");
        Optional<Customer> optionalCustomer = customerRepository.findByPhone(customer.getPhone());
        Optional<Customer> optionalCustomer1 = customerRepository.findByEmail(customer.getEmail());

        if (optionalCustomer.isPresent()){
            throw new ResourceAlreadyExistException("ce numero de telephone existe déjà");
        } else if ( optionalCustomer1.isPresent()) {
            throw new ResourceAlreadyExistException("cet email exist déjà");
        }else {
            return customerRepository.save(customer);
        }

    }

    public Customer update(Long customerId, Customer customer){
        log.info("Mis à jour du client" +customerId);
        Optional<Customer> optionalCustomer = customerFindById(customerId);

        Optional<Customer> byPhone = customerRepository.findByPhone(customer.getPhone());
        Optional<Customer> byEmail = customerRepository.findByEmail(customer.getEmail());

        if (byPhone.isPresent() && byPhone.get().getId() !=customerId){
            throw new ResourceAlreadyExistException("Le client avec ce numero de telephone " +customer.getPhone()+ " existe déja");

        } else if (byEmail.isPresent() && byEmail.get().getId() !=customerId) {
            throw new ResourceAlreadyExistException("Le client avec cet email" +customer.getEmail()+ " existe déja");

        }
        else
            customer.setId(optionalCustomer.get().getId());

        return customerRepository.save(customer);
    }

    public Customer delete(Long customerId){
        log.info("Suppression du client" +customerId);
        Optional<Customer> optionalCustomer = customerFindById(customerId);

        customerRepository.deleteById(customerId);

        return optionalCustomer.get();


    }

    private Optional<Customer> customerFindById(Long customerId) {

        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isEmpty())
            throw new ResourceNotFoundException("Le groupe avec l'ID " +customerId +" n'existe pas");

        return optionalCustomer;
    }
}
