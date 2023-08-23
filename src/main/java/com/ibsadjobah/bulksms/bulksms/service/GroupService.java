package com.ibsadjobah.bulksms.bulksms.service;

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
        return  groupRepository.findById(groupId).get();
    }

    public Group create(Group group)
    {
        Optional<Group> optionalGroup = groupRepository.findByName(group.getName());

        if (optionalGroup.isPresent())
            throw new RuntimeException("Ce nom existe deja");


        return groupRepository.save(group);
    }

    /**/

    /*

    public Group update(Long groupId, Group group)
    {

    }

    public void delete(Long groupId)
    {

    }*/

}
