package com.ibsadjobah.bulksms.bulksms.service;

import com.ibsadjobah.bulksms.bulksms.exception.ResourceAlreadyExistException;
import com.ibsadjobah.bulksms.bulksms.exception.ResourceNotFoundException;
import com.ibsadjobah.bulksms.bulksms.model.entities.Group;
import com.ibsadjobah.bulksms.bulksms.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;


    public List<Group> all()
    {
        return groupRepository.findAll();
    }

    public Group show(Long groupId)
    {
        Optional<Group> optionalGroup = groupFindbyId(groupId);

        return optionalGroup.get();
    }

    public Group create(Group group)
    {
        Optional<Group> optionalGroup = groupRepository.findByName(group.getName());

        if (optionalGroup.isPresent())
            throw new ResourceAlreadyExistException("Ce nom existe deja");


        return groupRepository.save(group);
    }

    public Group update(Long groupId, Group group)
    {
        Optional<Group> optionalGroup = groupFindbyId(groupId);

        group.setId(optionalGroup.get().getId());

        return groupRepository.save(group);
    }

    public Group delete(Long groupId)
    {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);

        groupRepository.deleteById(groupId);

        return optionalGroup.get();
    }

    private Optional<Group> groupFindbyId(Long groupId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);

        if (optionalGroup.isEmpty())
            throw new ResourceNotFoundException("Ce group n'existe pas");
        return optionalGroup;
    }

    /**/

    /*



    */

}
