package com.devsu.hackerearth.backend.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.devsu.hackerearth.backend.client.controller.ClientController;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.repository.ClientRepository;
import com.devsu.hackerearth.backend.client.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
public class sampleTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ClientService clientService = mock(ClientService.class);
    private ClientController clientController = new ClientController(clientService);

    @Test
    void createClientTest() {
        // Arrange
        ClientDto newClient = new ClientDto(1L, "12345678", "Juan Perez", "password123", "M", 30, "Av Principal 123", "954245755",
                true);
        ClientDto createdClient = new ClientDto(1L, "45781245", "Julio Figueroa", "Pass456", "M", 19, "Address", "9999999999",
                true);
        when(clientService.create(newClient)).thenReturn(createdClient);

        // Act
        ResponseEntity<ClientDto> response = clientController.create(newClient);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdClient, response.getBody());
    }

    @Test
void getClientByIdTest() {
    // Arrange
    Long clientId = 1L;
    ClientDto existingClient = new ClientDto(clientId, "12345678", "Juan Pérez", "password123", "M", 30, "Av. Principal 123", "987654321", true);
    
    when(clientService.getById(clientId)).thenReturn(existingClient);

    // Act
    ResponseEntity<ClientDto> response = clientController.get(clientId);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(existingClient, response.getBody());
}

@Test
void getClientByIdNotFoundTest() {
    // Arrange
    Long clientId = 2L;
    
    when(clientService.getById(clientId)).thenReturn(null); // Simulamos que el cliente no existe

    // Act
    ResponseEntity<ClientDto> response = clientController.get(clientId);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
}

@Test
void createClientIntegrationTest() throws Exception {
    // Arrange (Crear un cliente)
    ClientDto newClient = new ClientDto(null, "12345678", "Juan Pérez", "password123", "M", 30, "Av. Principal 123", "987654321", true);

    // Act (Enviar la petición HTTP al controlador)
    mockMvc.perform(post("/api/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(newClient)))
            .andExpect(status().isOk()); // Verifica que la respuesta sea 200 OK

    // Assert (Verificar que el cliente fue guardado en la BD)
    assertThat(clientRepository.findAll()).hasSize(1);
}
}
