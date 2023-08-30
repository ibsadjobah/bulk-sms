package com.ibsadjobah.bulksms.bulksms.service;

import com.ibsadjobah.bulksms.bulksms.exception.ResourceAlreadyExistException;
import com.ibsadjobah.bulksms.bulksms.exception.ResourceNotFoundException;
import com.ibsadjobah.bulksms.bulksms.model.entities.Group;
import com.ibsadjobah.bulksms.bulksms.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {

    private final GroupRepository groupRepository;


    public List<Group> all()
    {
        log.info("Liste des groupes");
        return groupRepository.findAll();
    }

    public Group show(Long groupId)
    {
        log.info("Affichage du groupe " +groupId);
        Optional<Group> optionalGroup = groupFindbyId(groupId);

        return optionalGroup.get();
    }

    public Group create(Group group)
    {
        log.info("Creation d'un nouveau groupe ");
        Optional<Group> optionalGroup = groupRepository.findByName(group.getName());

        if (optionalGroup.isPresent())
            throw new ResourceAlreadyExistException("Ce nom existe deja");


        return groupRepository.save(group);
    }

    public Group update(Long groupId, Group group)
    {
        log.info("Mise à jour du groupe " +groupId);
        Optional<Group> optionalGroup = groupFindbyId(groupId);

        Optional<Group> byName = groupRepository.findByName(group.getName());

        if (byName.isPresent() && byName.get().getId() != groupId)
            throw new ResourceAlreadyExistException("Le groupe avec pour nom " +group.getName()+ " existe déja");

        group.setId(optionalGroup.get().getId());

        return groupRepository.save(group);
    }

    public Group delete(Long groupId)
    {
        log.info("Suppression du groupe " +groupId);
        Optional<Group> optionalGroup = groupFindbyId(groupId);

        groupRepository.deleteById(groupId);
        return optionalGroup.get();

    }

    private Optional<Group> groupFindbyId(Long groupId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);

        if (optionalGroup.isEmpty())
            throw new ResourceNotFoundException("Le groupe avec l'ID " +groupId+" n'existe pas");

        return optionalGroup;
    }


}
