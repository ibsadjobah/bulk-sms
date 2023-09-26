package com.ibsadjobah.bulksms.bulksms.service;

import com.ibsadjobah.bulksms.bulksms.exception.ResourceNotFoundException;
import com.ibsadjobah.bulksms.bulksms.model.entities.Campagne;
import com.ibsadjobah.bulksms.bulksms.model.entities.Group;
import com.ibsadjobah.bulksms.bulksms.repository.CampagneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static java.beans.Beans.isInstanceOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CampagneServiceTest {

    private  CampagneService campagneService;

    @Mock
    private  CampagneRepository campagneRepository;



    @BeforeEach
    void setUp() {
        campagneService = new CampagneService(campagneRepository);
    }

    @Test
    void itSouldListEmptyCampagne() {
        //given

        //WHEN
        List<Campagne> expected = campagneService.all();

        assertThat(expected).isEmpty();
    }

    @Test
    void itShouldNotEmptyCampagne() {
        //given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        LocalDateTime scheduleAt = LocalDateTime.parse("2023-04-28T12:00:01.545", formatter);

        Campagne developpement = Campagne.builder()
                .ref("AZZE112")
                .message("merci de bien developper ")
                .type("positif")
                .schedule_at(scheduleAt)
                .build();

        List<Campagne> data = new LinkedList<>();
        data.add(developpement);

        when(campagneRepository.findAll()).thenReturn(data);

        List<Campagne> expected = campagneService.all();

        assertThat(expected).isNotEmpty();

    }

    @Test
    void itShouldNotDisplayCampagneById() {

        Long campagneId = 45L;

        assertThatThrownBy(() -> campagneService.show(campagneId))
        .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("La campagne " + campagneId + "n'existe pas");

    }

    @Test
    void itShouldDisplayCampagneById() {
        //given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        LocalDateTime scheduleAt = LocalDateTime.parse("2023-04-28T12:00:01.545", formatter);

        Campagne developpement = Campagne.builder()
                .ref("HHILL112")
                .message("adaptation")
                .type("positif")
                .schedule_at(scheduleAt)
                .build();

        when(campagneRepository.findById(developpement.getId())).thenReturn(Optional.of(developpement));

        //WHEN
        Campagne expected = campagneService.show(developpement.getId());

        // Then
        assertThat(expected).isEqualTo(developpement);

    }

    @Test
    void itShouldDeleteCampagneById() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        LocalDateTime scheduleAt = LocalDateTime.parse("2023-04-28T12:00:01.545", formatter);

        Campagne developpement = Campagne.builder()
                .ref("HHILL112")
                .message("adaptation")
                .type("positif")
                .schedule_at(scheduleAt)
                .build();

        when(campagneRepository.findById(developpement.getId())).thenReturn(Optional.of(developpement));

        Campagne expected = campagneService.delete(developpement.getId());

        assertThat(expected).isEqualTo(developpement);

    }

    @Test
    void itShouldNotDeleteCampagneById() {

        Long campagneId = 45L;

        assertThatThrownBy(() -> campagneService.show(campagneId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("La campagne " + campagneId + "n'existe pas");


    }

    @Test
    void itShouldCreateCampagne() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        LocalDateTime scheduleAt = LocalDateTime.parse("2023-04-28T12:00:01.545", formatter);

        Campagne developpement = Campagne.builder()
                .ref("HHILL112")
                .message("adaptation")
                .type("positif")
                .schedule_at(scheduleAt)
                .build();

        // When
        this.campagneService.create(developpement);

        // Then
        ArgumentCaptor<Campagne> argumentCaptor = ArgumentCaptor.forClass(Campagne.class);

        verify(campagneRepository).save(argumentCaptor.capture());

        Campagne excepted = argumentCaptor.getValue();

        assertThat(excepted).isEqualTo(developpement);

    }


    @Test
    void itShouldUpdateCampagne() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        LocalDateTime scheduleAt = LocalDateTime.parse("2023-04-28T12:00:01.545", formatter);

        Campagne developpement = Campagne.builder()
                .ref("HHILL112")
                .message("adaptation")
                .type("positif")
                .schedule_at(scheduleAt)
                .build();


        DateTimeFormatter formatterUpdate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        LocalDateTime scheduleUpdate = LocalDateTime.parse("2023-04-28T12:00:01.545", formatterUpdate);

        Campagne programmation = Campagne.builder()
                .ref("HHEER33456")
                .message("adaptation")
                .type("positif")
                .schedule_at(scheduleUpdate)
                .build();

        when(campagneRepository.findById(developpement.getId())).thenReturn(Optional.of(developpement));

        // When
        this.campagneService.update(developpement.getId(), programmation);

        ArgumentCaptor<Campagne> argumentCaptor = ArgumentCaptor.forClass(Campagne.class);

        // Then
        verify(campagneRepository).save(argumentCaptor.capture());

        Campagne excepted = argumentCaptor.getValue();

        assertThat(excepted.getRef()).isEqualTo(programmation.getRef());
    }
}