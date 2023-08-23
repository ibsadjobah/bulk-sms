package com.ibsadjobah.bulksms.bulksms.controller;

import com.ibsadjobah.bulksms.bulksms.model.entities.Group;
import com.ibsadjobah.bulksms.bulksms.model.requests.GroupRequest;
import com.ibsadjobah.bulksms.bulksms.model.responses.GroupResponse;
import com.ibsadjobah.bulksms.bulksms.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final ModelMapper modelMapper;


    @GetMapping
    public List<GroupResponse> list()
    {
        List<Group> groups = groupService.all();

        /* List<GroupResponse> groupResponses = new LinkedList<>();

        for(Group group: groups)
        {
            GroupResponse groupResponse = GroupResponse.builder()
                    .id(group.getId())
                    .name(group.getName())
                    .build();

            groupResponses.add(groupResponse);
        }*/

        return groups.stream()
                .map(group -> GroupResponse.builder()
                        .id(group.getId())
                        .name(group.getName())
                        .build())
                .collect(Collectors.toList());
    }

    @GetMapping("{groupId}")
    public GroupResponse show(@PathVariable("groupId") Long groupId)
    {
        /*Group group = groupService.show(groupId);

        GroupResponse groupResponse = GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .build();*/

        return modelMapper.map(groupService.show(groupId), GroupResponse.class);
    }

    @PostMapping
    public GroupResponse create(@Valid @RequestBody GroupRequest groupRequest)
    {
        Group group = new Group();
        group.setName(groupRequest.getName());

        return modelMapper.map( groupService.create(group), GroupResponse.class);


    }
}
