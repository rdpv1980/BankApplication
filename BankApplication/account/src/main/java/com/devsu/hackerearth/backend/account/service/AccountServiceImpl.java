package com.devsu.hackerearth.backend.account.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<AccountDto> getAll() {
        // Get all accounts
        return accountRepository.findAll().stream()
                .map(account -> new AccountDto(
                        account.getId(), account.getNumber(), account.getType(),
                        account.getInitialAmount(), account.isActive(),
                        account.getClientId()))
                .collect(Collectors.toList());
    }

    @Override
    public AccountDto getById(Long id) {
        // Get accounts by id
        return accountRepository.findById(id)
                .map(account -> new AccountDto(
                        account.getId(), account.getNumber(), account.getType(),
                        account.getInitialAmount(), account.isActive(),
                        account.getClientId()))
                .orElse(null);
    }

    @Override
    public AccountDto create(AccountDto accountDto) {
        // Create account
        Account account = new Account();
        account.setNumber(accountDto.getNumber());
        account.setType(accountDto.getType());
        account.setInitialAmount(accountDto.getInitialAmount());
        account.setActive(accountDto.isActive());

        Account savedAccount = accountRepository.save(account);
        return new AccountDto(
                savedAccount.getId(), savedAccount.getNumber(), savedAccount.getType(),
                savedAccount.getInitialAmount(), savedAccount.isActive(),
                savedAccount.getClientId());
    }

    @Override
    public AccountDto update(AccountDto accountDto) {
        // Update account
        return accountRepository.findById(accountDto.getId()).map(account -> {
            account.setNumber(accountDto.getNumber());
            account.setType(accountDto.getType());
            account.setInitialAmount(accountDto.getInitialAmount());
            account.setActive(accountDto.isActive());

            Account updatedAccount = accountRepository.save(account);
            return new AccountDto(
                    updatedAccount.getId(), updatedAccount.getNumber(), updatedAccount.getType(),
                    updatedAccount.getInitialAmount(), updatedAccount.isActive(),
                    updatedAccount.getClientId());
        }).orElse(null);
    }

    @Override
    public AccountDto partialUpdate(Long id, PartialAccountDto partialAccountDto) {
        // Partial update account
        return accountRepository.findById(id).map(account -> {
            account.setActive(partialAccountDto.isActive());

            Account updatedAccount = accountRepository.save(account);
            return new AccountDto(
                    updatedAccount.getId(), updatedAccount.getNumber(), updatedAccount.getType(),
                    updatedAccount.getInitialAmount(), updatedAccount.isActive(),
                    updatedAccount.getClientId());
        }).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        // Delete account
        accountRepository.deleteById(id);
    }

}
