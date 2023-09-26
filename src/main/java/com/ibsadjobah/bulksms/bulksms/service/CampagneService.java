package com.ibsadjobah.bulksms.bulksms.service;

import com.ibsadjobah.bulksms.bulksms.exception.ResourceAlreadyExistException;
import com.ibsadjobah.bulksms.bulksms.exception.ResourceNotFoundException;
import com.ibsadjobah.bulksms.bulksms.model.entities.Campagne;
import com.ibsadjobah.bulksms.bulksms.repository.CampagneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CampagneService {

    private final CampagneRepository campagneRepository;

    public List<Campagne> all(){

        return campagneRepository.findAll();
    }

    public Campagne show(Long campagneId) {

        Optional<Campagne> campagne = campagneFindById(campagneId);

        return campagne.get();
    }

    public Campagne create(Campagne campagne) {

        Optional<Campagne> campagneByRef = campagneRepository.findByRef(campagne.getRef());

        if (campagneByRef.isPresent())
            throw new ResourceAlreadyExistException("cette reference existe déjà");

        return campagneRepository.save(campagne);
    }



    public Campagne update(Long campagneId, Campagne campagne){

        Optional<Campagne> optionalCampagne = campagneFindById(campagneId);

        Optional<Campagne> campagneByRef = campagneRepository.findByRef(campagne.getRef());

        if (campagneByRef.isPresent())
            throw new ResourceAlreadyExistException("cette reference existe déjà");

        campagne.setId(optionalCampagne.get().getId());

        return campagneRepository.save(campagne);
    }

    public Campagne delete(Long campagneId){
        Optional<Campagne> optionalCampagne = campagneFindById(campagneId);

         campagneRepository.deleteById(campagneId);

         return optionalCampagne.get();
    }
    private Optional<Campagne> campagneFindById(Long campagneId)
    {
        Optional<Campagne> optionalCampagne = campagneRepository.findById(campagneId);
        if (optionalCampagne.isEmpty())
            throw new ResourceNotFoundException("La campagne " + campagneId + "n'existe pas");

        return optionalCampagne;
    }


}
