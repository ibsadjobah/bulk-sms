package com.ibsadjobah.bulksms.bulksms.controller;

import com.ibsadjobah.bulksms.bulksms.model.HttpResponse;
import com.ibsadjobah.bulksms.bulksms.model.entities.Group;
import com.ibsadjobah.bulksms.bulksms.model.requests.GroupRequest;
import com.ibsadjobah.bulksms.bulksms.model.responses.GroupResponse;
import com.ibsadjobah.bulksms.bulksms.service.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/groups")
@RequiredArgsConstructor
@Api("api/v1/groups")
public class GroupController {

    private final GroupService groupService;
    private final ModelMapper modelMapper;


    @GetMapping
    @ApiOperation(value = "Liste des groupes", notes = "Affiche la liste de tous les groupes", response = HttpResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Liste des articles")
    })
    public ResponseEntity<HttpResponse> list()
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

        List<GroupResponse> data = groups.stream()
                .map(group -> GroupResponse.builder()
                        .id(group.getId())
                        .name(group.getName())
                        .build())
                .collect(Collectors.toList());

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Liste des groupes")
                .data((Map.of("groupes", data)))
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);


    }

    @GetMapping("{groupId}")
    @ApiOperation(value = "Affichage d'un groupe à partir de son ID", response = HttpResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Affichage d'un groupe"),
            @ApiResponse(code = 404, message = "Id du groupe n'a pas été retrouvé"),
    })
    public ResponseEntity<HttpResponse> show(@PathVariable("groupId") Long groupId)
    {
        /*Group group = groupService.show(groupId);

        GroupResponse groupResponse = GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .build();*/

        GroupResponse data = modelMapper.map(groupService.show(groupId), GroupResponse.class);

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Affichage du groupe " +data.getName())
                .data((Map.of("groupe", data)))
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Création d'un groupe", response = HttpResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ajout d'un nouveau groupe"),
            @ApiResponse(code = 400, message = "Le nom du groupe existe déjà"),
    })
    public ResponseEntity<HttpResponse> create(@Valid @RequestBody GroupRequest groupRequest)
    {
        Group group = new Group();
        group.setName(groupRequest.getName());

        GroupResponse data = modelMapper.map(groupService.create(group), GroupResponse.class);

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Ajout d'un nouveau groupe")
                .data((Map.of("groupe", data)))
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);


    }

    @PutMapping("{groupId}")
    @ApiOperation(value = "Mise à jour d'un groupe", response = HttpResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Mise à jour du groupe"),
            @ApiResponse(code = 400, message = "Le nom du groupe existe déjà"),
    })
    public ResponseEntity<HttpResponse> update(@PathVariable("groupId") Long groupId, @Valid @RequestBody GroupRequest groupRequest)
    {
        Group update = groupService.update(groupId, modelMapper.map(groupRequest, Group.class));

        GroupResponse data = modelMapper.map(update, GroupResponse.class);

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Mise à jour du groupe " +groupId)
                .data((Map.of("groupe", data)))
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);
    }

    @DeleteMapping("{groupId}")
    @ApiOperation(value = "Suppréssion d'un groupe", response = HttpResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suppression d'un groupe"),
            @ApiResponse(code = 404, message = "Id du groupe n'a pas été retrouvé"),
    })
    public ResponseEntity<HttpResponse> delete(@PathVariable("groupId") Long groupId)
    {
        groupService.delete(groupId);

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message(" Suppression du groupe " +groupId)
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);
    }

}
