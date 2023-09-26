package com.ibsadjobah.bulksms.bulksms.service;

import com.ibsadjobah.bulksms.bulksms.exception.ResourceNotFoundException;
import com.ibsadjobah.bulksms.bulksms.model.entities.Destinataire;
import com.ibsadjobah.bulksms.bulksms.repository.DestinataireRepository;
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


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DestinataireServiceTest {

    @Mock
    private DestinataireRepository destinataireRepository;


    private DestinataireService destinataireService;

    @BeforeEach
    void setUp(){
        destinataireService = new DestinataireService(destinataireRepository);
    }

    @Test
    void itShouldListEmptyDestinataire() {
        //given
        List<Destinataire> expected = destinataireService.all();

        assertThat(expected).isEmpty();
    }

    @Test
    void itShouldListDestinataire() {

        //given
        Destinataire listDestinataire = Destinataire.builder()
                .status("delivré")
                .build();

        List<Destinataire> data = new LinkedList<>();
        data.add(listDestinataire);

        when(destinataireRepository.findAll()).thenReturn(data);

        //when
        List<Destinataire> expected = destinataireService.all();

        //then
        assertThat(expected).isNotEmpty();
        //assertThat(expected)
    }

    @Test
    void itShouldNotDisplayDestinataireById() {
        Long destinataireId = 67L;

        assertThatThrownBy(() ->destinataireService.show(destinataireId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Le destinataire avec cet ID " + destinataireId + " n'existe pas ");

    }

    @Test
    void itShouldDisplayDestinataireById() {
        Destinataire destinataire = Destinataire.builder()
                .id(1L)
                .status("reçu")
                .build();

        when(destinataireRepository.findById(destinataire.getId())).thenReturn(Optional.of(destinataire));

        //when
        Destinataire expected = destinataireService.show(destinataire.getId());

        //then
        assertThat(expected).isEqualTo(destinataire);
    }

    @Test
    void itShouldDeleteDestinataireById() {
        Destinataire destinataire =Destinataire.builder()
                .id(1L)
                .status("test")
                .build();

        when(destinataireRepository.findById(destinataire.getId())).thenReturn(Optional.of(destinataire));

        Destinataire expected = destinataireService.delete(destinataire.getId());

        assertThat(expected).isEqualTo(destinataire);
    }

    @Test
    void itShouldNotDeleteDestinataireById() {
        Long destinataireId = 43L;

        // When
        // Then
        assertThatThrownBy(() ->destinataireService.delete(destinataireId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Le destinataire avec cet ID " + destinataireId + " n'existe pas ");

    }

    @Test
    void itShouldCreateDestinataire() {

        Destinataire destinataire = Destinataire.builder()
                .id(1L)
                .status("Delivré")
                .build();
        // When
        this.destinataireService.create(destinataire);

        //then
        ArgumentCaptor<Destinataire> argumentCaptor =ArgumentCaptor.forClass(Destinataire.class);
        verify(destinataireRepository).save(argumentCaptor.capture());

        Destinataire expected = argumentCaptor.getValue();
        assertThat(expected).isEqualTo(destinataire);

    }

    @Test
    void itShouldUpdateDestinataire() {
        Destinataire destinataire = Destinataire.builder()
                .id(3L)
                .status("notUpdate")
                .build();

        Destinataire updateDestinataire = Destinataire.builder()
                .id(3L)
                .status("update")
                .build();

        when(destinataireRepository.findById(destinataire.getId())).thenReturn(Optional.of(destinataire));

        // When
        this.destinataireService.update(destinataire.getId(), updateDestinataire);

        ArgumentCaptor<Destinataire> argumentCaptor = ArgumentCaptor.forClass(Destinataire.class);

        // Then
        verify(destinataireRepository).save(argumentCaptor.capture()) ;

        Destinataire expected = argumentCaptor.getValue();

        assertThat(expected.getStatus()).isEqualTo(updateDestinataire.getStatus());

    }
}