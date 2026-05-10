package tech.essoktani.backend.services;

import tech.essoktani.backend.entities.BankAccount;
import tech.essoktani.backend.entities.CurrentAccount;
import tech.essoktani.backend.entities.Customer;
import tech.essoktani.backend.entities.SavingAccount;
import tech.essoktani.backend.exceptions.BalanceNotSufficientException;
import tech.essoktani.backend.exceptions.BankAccountNotFoundException;
import tech.essoktani.backend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    Customer saveCustomer(Customer customer);

    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long CustomerId) throws CustomerNotFoundException;

    SavingAccount saveSavingBankAccount(double initialBalance, double InterestRate, Long CustomerId) throws CustomerNotFoundException;

    List<Customer> listCustomers();

    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;

    BankAccount getBankAccountById(String accountId) throws BankAccountNotFoundException;

    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;

    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;

    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccount> bankAccountList();
}