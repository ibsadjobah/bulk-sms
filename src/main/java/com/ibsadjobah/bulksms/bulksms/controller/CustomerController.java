package com.ibsadjobah.bulksms.bulksms.controller;

import com.ibsadjobah.bulksms.bulksms.model.HttpResponse;
import com.ibsadjobah.bulksms.bulksms.model.entities.Customer;
import com.ibsadjobah.bulksms.bulksms.model.requests.CustomerRequest;
import com.ibsadjobah.bulksms.bulksms.model.responses.CustomerResponse;
import com.ibsadjobah.bulksms.bulksms.service.CustomerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/customer")
@RequiredArgsConstructor
@ApiOperation("api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    @GetMapping()
    @ApiOperation(value = "Liste des clients", notes = "Affiche la liste de tous les clients", response = HttpResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Liste des clients")
    })
    public ResponseEntity<HttpResponse> list(){

        List<Customer> customers = customerService.all();

        List<CustomerResponse>  data = customers.stream()
                .map(customer -> CustomerResponse.builder()
                        .id(customer.getId())
                        .name(customer.getName())
                        .phone(customer.getPhone())
                        .email(customer.getEmail())
                        .build())
                .collect(Collectors.toList());


        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("liste des clients")
                .data(Map.of("clients", data))
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);
    }

    @GetMapping("{customerId}")
    @ApiOperation(value = "Affichage d'un client à partir de son ID", response = HttpResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Affichage d'un client"),
            @ApiResponse(code = 404, message = "ID du client n'a pas été retrouvé"),
    })
    public ResponseEntity<HttpResponse> show(@PathVariable("customerId") Long customerId){

        CustomerResponse data = modelMapper.map(customerService.show(customerId), CustomerResponse.class);

        HttpResponse httpResponse= HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("affichage d'un client")
                .data(Map.of("client", data))
                .build();

        return  ResponseEntity.ok()
                .body(httpResponse);
    }


    @PostMapping
    @ApiOperation(value = "Ajout d'un client", response = HttpResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ajout d'un nouveau client"),
            @ApiResponse(code = 400, message = "Ce numero telephone existe déjà"),
            @ApiResponse(code = 400, message = "Cet email existe déjà"),
    })
    public ResponseEntity<HttpResponse> create(@Valid @RequestBody CustomerRequest customerRequest){

        Customer customer =  new Customer();

        customer.setName(customerRequest.getName());
        customer.setPhone(customerRequest.getPhone());
        customer.setEmail(customerRequest.getEmail());

        CustomerResponse data = modelMapper.map(customerService.create(customer), CustomerResponse.class);

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("ajout d'un nouveau client")
                .data(Map.of("client", data))
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);


    }


    @PutMapping("{customerId}")
    @ApiOperation(value = "Mise à jour d'un client", response = HttpResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Mise à jour du client"),
            @ApiResponse(code = 400, message = "Ce numero de telephone existe déjà"),
            @ApiResponse(code = 400, message = "Cet email déjà")
    })
    public ResponseEntity<HttpResponse> update(@PathVariable("customerId") Long customerId, @Valid @RequestBody  CustomerRequest customerRequest){

        Customer update = customerService.update(customerId, modelMapper.map(customerRequest, Customer.class));

        CustomerResponse data = modelMapper.map(update, CustomerResponse.class);


        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message(" Mise à jour du client " +customerId)
                .data((Map.of("client", data)))
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);

    }

    @DeleteMapping("{customerId}")
    @ApiOperation(value = "Suprression d'un client", response = HttpResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suppression du client"),
            @ApiResponse(code = 404, message = "Id du client pas été retrouvé")
    })
    public ResponseEntity<HttpResponse> delete(@PathVariable("customerId") Long customerId){

        customerService.delete(customerId);


        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Suppression d'un client " +customerId)
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);


    }



}
