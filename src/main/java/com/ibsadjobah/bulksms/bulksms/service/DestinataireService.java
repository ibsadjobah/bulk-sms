package com.ibsadjobah.bulksms.bulksms.service;

import com.ibsadjobah.bulksms.bulksms.exception.ResourceNotFoundException;
import com.ibsadjobah.bulksms.bulksms.model.entities.Destinataire;
import com.ibsadjobah.bulksms.bulksms.repository.DestinataireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DestinataireService {

    private final DestinataireRepository destinataireRepository;

    public List <Destinataire>  all(){

        return destinataireRepository.findAll();
    }

    public Destinataire show(Long destinataireId){

       Optional<Destinataire> optionalDestinataire = destinataireFindById(destinataireId);

      return optionalDestinataire.get();
    }

    public Destinataire create(Destinataire destinataire){

        return destinataireRepository.save(destinataire);
    }

    public Destinataire update(Long destinataireId, Destinataire destinataire){

        Optional<Destinataire> optionalDestinataire = destinataireFindById(destinataireId);

        destinataire.setId(optionalDestinataire.get().getId());

        return destinataireRepository.save(destinataire);

    }

    public Destinataire delete(Long destinataireId){

        Optional<Destinataire> optionalDestinataire = destinataireFindById(destinataireId);

       destinataireRepository.deleteById(destinataireId);

       return optionalDestinataire.get();


    }


    private Optional<Destinataire> destinataireFindById(Long destinataireId)
    {
        Optional<Destinataire> destinataireOptional = destinataireRepository.findById(destinataireId);

        if (destinataireOptional.isEmpty())
            throw new ResourceNotFoundException("Le destinataire avec cet ID " + destinataireId + " n'existe pas ");

        return destinataireOptional;
    }
}
