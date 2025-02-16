package com.devsu.hackerearth.backend.client.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.repository.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;

	public ClientServiceImpl(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@Override
	public List<ClientDto> getAll() {
		// Get all clients
		return clientRepository.findAll().stream()
				.map(client -> new ClientDto(
						client.getId(), client.getDni(), client.getName(),
						client.getPassword(), client.getGender(), client.getAge(),
						client.getAddress(), client.getPhone(), client.isActive()))
				.collect(Collectors.toList());
	}

	@Override
	public ClientDto getById(Long id) {
		// Get clients by id
		return clientRepository.findById(id)
				.map(client -> new ClientDto(
						client.getId(), client.getDni(), client.getName(),
						client.getPassword(), client.getGender(), client.getAge(),
						client.getAddress(), client.getPhone(), client.isActive()))
				.orElse(null);
	}

	@Override
	public ClientDto create(ClientDto clientDto) {
		// Create client
		Client client = new Client();
		client.setName(clientDto.getName());
		client.setDni(clientDto.getDni());
		client.setGender(clientDto.getGender());
		client.setAge(clientDto.getAge());
		client.setAddress(clientDto.getAddress());
		client.setPhone(clientDto.getPhone());
		client.setPassword(clientDto.getPassword());
		client.setActive(clientDto.isActive());

		Client savedClient = clientRepository.save(client);
		return new ClientDto(
				savedClient.getId(), savedClient.getDni(), savedClient.getName(),
				savedClient.getPassword(), savedClient.getGender(), savedClient.getAge(),
				savedClient.getAddress(), savedClient.getPhone(), savedClient.isActive());
	}

	@Override
	public ClientDto update(ClientDto clientDto) {
		// Update client
		return clientRepository.findById(clientDto.getId()).map(client -> {
			client.setName(clientDto.getName());
			client.setDni(clientDto.getDni());
			client.setGender(clientDto.getGender());
			client.setAge(clientDto.getAge());
			client.setAddress(clientDto.getAddress());
			client.setPhone(clientDto.getPhone());
			client.setPassword(clientDto.getPassword());
			client.setActive(clientDto.isActive());

			Client updatedClient = clientRepository.save(client);
			return new ClientDto(
					updatedClient.getId(), updatedClient.getDni(), updatedClient.getName(),
					updatedClient.getPassword(), updatedClient.getGender(), updatedClient.getAge(),
					updatedClient.getAddress(), updatedClient.getPhone(), updatedClient.isActive());
		}).orElse(null);
	}

	@Override
	public ClientDto partialUpdate(Long id, PartialClientDto partialClientDto) {
		// Partial update account
		return clientRepository.findById(id).map(client -> {
			client.setActive(partialClientDto.isActive());
	
			Client updatedClient = clientRepository.save(client);
			return new ClientDto(
				updatedClient.getId(), updatedClient.getDni(), updatedClient.getName(),
				updatedClient.getPassword(), updatedClient.getGender(), updatedClient.getAge(),
				updatedClient.getAddress(), updatedClient.getPhone(), updatedClient.isActive());
		}).orElse(null);
	}

	@Override
	public void deleteById(Long id) {
		// Delete client
		clientRepository.deleteById(id);
	}
}
