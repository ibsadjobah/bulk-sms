package com.ibsadjobah.bulksms.bulksms.service;

import com.ibsadjobah.bulksms.bulksms.exception.ResourceAlreadyExistException;
import com.ibsadjobah.bulksms.bulksms.exception.ResourceNotFoundException;
import com.ibsadjobah.bulksms.bulksms.model.entities.Group;
import com.ibsadjobah.bulksms.bulksms.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {


    private GroupService groupService;

    @Mock
    private GroupRepository groupRepository;


    @BeforeEach
    void setUp() {
        groupService = new GroupService(groupRepository);
    }

    @Test
    void itShouldListEmptyGroup() {
        // Given

        // When
        List<Group> expected = groupService.all();

        // Then
        assertThat(expected).isEmpty();
    }

    @Test
    void itShouldNotEmptyGroup() {
        // Given
        Group devOps = Group.builder()
                .id(1L)
                .name("DevOps")
                .build();

        List<Group> data = new LinkedList<>();
        data.add(devOps);

        when(groupRepository.findAll()).thenReturn(data);

        // When
        List<Group> expected = groupService.all();

        // Then
        assertThat(expected).isNotEmpty();
        assertThat(expected.size()).isEqualTo(1);
        assertThat(expected.get(0).getId()).isEqualTo(devOps.getId());
        assertThat(expected.get(0).getName()).isEqualTo(devOps.getName());
        //assertThat(expected.get(0)).isEqualTo(devOps);
    }

    @Test
    void itShouldNotDisplayGroupById() {
        // Given
        Long groupId = 23L;

        // When
        // Then
        assertThatThrownBy(() -> groupService.show(groupId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Le groupe avec l'ID "+groupId+" n'existe pas");
    }

    @Test
    void itShouldDisplayGroupById() {
        // Given
        Group android = Group.builder()
                .id(5L)
                .name("Android")
                .build();

        when(groupRepository.findById(android.getId())).thenReturn(Optional.of(android));

        // When
        Group expected = groupService.show(android.getId());

        // Then
        assertThat(expected).isEqualTo(android);
    }

    @Test
    void itShouldDeleteGroupById() {
        // Given
        Group reactJs = Group.builder()
                .id(10L)
                .name("ReactJs")
                .build();
        when(groupRepository.findById(reactJs.getId())).thenReturn(Optional.of(reactJs));

        Group expected = groupService.delete(reactJs.getId());

        assertThat(expected).isEqualTo(reactJs);
    }

    @Test
    void itShouldNotDeleteGroupById() {
        // Given
        Long groupId = 20L;

        // When
        // Then
        assertThatThrownBy(() -> groupService.delete(groupId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Le groupe avec l'ID "+groupId+" n'existe pas");
    }

    @Test
    void itShouldCreateGroup() {
        // Given
        Group laravel = Group.builder()
                .id(15L)
                .name("Laravel")
                .build();

        // When
        this.groupService.create(laravel);

        // Then
        ArgumentCaptor<Group> argumentCaptor = ArgumentCaptor.forClass(Group.class);

        verify(groupRepository).save(argumentCaptor.capture());

        Group excepted = argumentCaptor.getValue();

        assertThat(excepted).isEqualTo(laravel);
    }

    @Test
    void itShouldNotCreateGroup() {
        // Given
        Group spring = Group.builder()
                .id(15L)
                .name("Spring")
                .build();

        when(groupRepository.findByName(spring.getName())).thenReturn(Optional.of(spring));

        // When
        // Then
        assertThatThrownBy(() ->  this.groupService.create(spring))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessage("Ce nom existe deja");

    }

    @Test
    void itShouldUpdateGroup() {
        // Given
        Group spring = Group.builder()
                .id(19L)
                .name("Spring")
                .build();

        Group springBoot = Group.builder()
                .name("Spring Boot")
                .build();

        when(groupRepository.findById(spring.getId())).thenReturn(Optional.of(spring));

        // When
        this.groupService.update(spring.getId(), springBoot);

        ArgumentCaptor<Group> argumentCaptor = ArgumentCaptor.forClass(Group.class);

        // Then
        verify(groupRepository).save(argumentCaptor.capture());

        Group excepted = argumentCaptor.getValue();

        assertThat(excepted.getName()).isEqualTo(springBoot.getName());
    }

    @Test
    void itShouldNotUpdateWhenGroupIdDoesntExist() {
        // Given
        // When
        Long groupId = 100L;

        Group springBoot = Group.builder()
                .name("Spring Boot")
                .build();

        // Then
        assertThatThrownBy(() -> groupService.update(groupId, springBoot))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Le groupe avec l'ID "+groupId+" n'existe pas");
    }

    @Test
    void itShouldNotUpdateWhenGroupNameAlreadyExist() {
        // Given
        // When

        Group spring = Group.builder()
                .id(55L)
                .name("Spring boot")
                .build();

        Group springBoot = Group.builder()
                .id(33L)
                .name("Spring boot")
                .build();

        when(groupRepository.findById(spring.getId())).thenReturn(Optional.of(spring));
        when(groupRepository.findByName(springBoot.getName())).thenReturn(Optional.of(springBoot));

        // Then
        assertThatThrownBy(() -> groupService.update(spring.getId(), springBoot))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessage("Le groupe avec pour nom "+spring.getName()+" existe déja");
    }
}
