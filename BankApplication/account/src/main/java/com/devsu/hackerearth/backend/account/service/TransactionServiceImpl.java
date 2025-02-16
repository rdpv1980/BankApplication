package com.devsu.hackerearth.backend.account.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.devsu.hackerearth.backend.account.model.Transaction;
import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;
import com.devsu.hackerearth.backend.account.model.dto.ClientDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

        private final TransactionRepository transactionRepository;
        
        @Autowired
        private RestTemplate restTemplate;

        public TransactionServiceImpl(TransactionRepository transactionRepository) {
                this.transactionRepository = transactionRepository;
        }

        @Override
        public List<TransactionDto> getAll() {
                // Get all transactions
                return transactionRepository.findAll().stream()
                                .map(transaction -> new TransactionDto(
                                                transaction.getId(), transaction.getDate(),
                                                transaction.getType(), transaction.getAmount(),
                                                transaction.getBalance(), transaction.getAccount().getId()))
                                .collect(Collectors.toList());
        }

        @Override
        public TransactionDto getById(Long id) {
                // Get transactions by id
                return transactionRepository.findById(id)
                                .map(transaction -> new TransactionDto(
                                                transaction.getId(), transaction.getDate(),
                                                transaction.getType(), transaction.getAmount(),
                                                transaction.getBalance(), transaction.getAccount().getId()))
                                .orElse(null);
        }

        @Override
        public TransactionDto create(TransactionDto transactionDto) {
                // Create transaction
                Transaction transaction = new Transaction();
                transaction.setDate(transactionDto.getDate());
                transaction.setType(transactionDto.getType());
                transaction.setAmount(transactionDto.getAmount());
                transaction.setBalance(transactionDto.getBalance());

                Transaction savedTransaction = transactionRepository.save(transaction);
                return new TransactionDto(
                                savedTransaction.getId(), savedTransaction.getDate(),
                                savedTransaction.getType(), savedTransaction.getAmount(),
                                savedTransaction.getBalance(), savedTransaction.getAccount().getId());
        }

        @Override
        public List<BankStatementDto> getAllByAccountClientIdAndDateBetween(Long clientId, Date dateTransactionStart,
                        Date dateTransactionEnd) {
                // Report
                return transactionRepository.findAll().stream()
                                .filter(transaction -> transaction.getAccount().getClientId().equals(clientId) &&
                                                !transaction.getDate().before(dateTransactionStart) &&
                                                !transaction.getDate().after(dateTransactionEnd))
                                .map(transaction -> {
                                        // Llamamos al microservicio client para obtener el nombre del cliente
                                        ClientDto clientDto = restTemplate.getForObject(
                                                        "http://client-service/api/clients/"
                                                                        + transaction.getAccount().getClientId(),
                                                        ClientDto.class);

                                        return new BankStatementDto(
                                                        transaction.getDate(),
                                                        clientDto != null ? clientDto.getName() : "Cliente Desconocido", 
                                                        transaction.getAccount().getNumber(),
                                                        transaction.getAccount().getType(),
                                                        transaction.getAccount().getInitialAmount(),
                                                        transaction.getAccount().isActive(),
                                                        transaction.getType(),
                                                        transaction.getAmount(),
                                                        transaction.getBalance());
                                })
                                .collect(Collectors.toList());
        }

        @Override
        public TransactionDto getLastByAccountId(Long accountId) {
                return transactionRepository.findAll().stream()
                                .filter(transaction -> transaction.getAccount().getId().equals(accountId))
                                .max((t1, t2) -> t1.getDate().compareTo(t2.getDate())) // Obtener la última transacción
                                .map(transaction -> new TransactionDto(
                                                transaction.getId(),
                                                transaction.getDate(),
                                                transaction.getType(),
                                                transaction.getAmount(),
                                                transaction.getBalance(),
                                                transaction.getAccount().getId()))
                                .orElse(null);
        }

}
