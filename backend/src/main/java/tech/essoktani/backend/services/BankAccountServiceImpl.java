package tech.essoktani.backend.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.essoktani.backend.entities.*;
import tech.essoktani.backend.enums.OperationType;
import tech.essoktani.backend.exceptions.BalanceNotSufficientException;
import tech.essoktani.backend.exceptions.BankAccountNotFoundException;
import tech.essoktani.backend.exceptions.CustomerNotFoundException;
import tech.essoktani.backend.repositories.AccountOperationRepository;
import tech.essoktani.backend.repositories.BankAccountRepository;
import tech.essoktani.backend.repositories.CustomerRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private CustomerRepository customerRepository;

    private BankAccountRepository bankAccountRepository;

    private AccountOperationRepository accountOperationRepository;

    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("Saving new customer ");
        Customer savedCustomer= customerRepository.save(customer);
        return savedCustomer;
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long CustomerId) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(CustomerId).orElse(null);
        if(customer == null)
            throw new CustomerNotFoundException("Customer not found");
        CurrentAccount currentAccount = new CurrentAccount();



        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);

        currentAccount.setCustomer(customer);

        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);

        return savedBankAccount;


    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double InterestRate, Long CustomerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(CustomerId).orElse(null);
        if(customer == null)
            throw new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount = new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(InterestRate);

        savingAccount.setCustomer(customer);

        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);

        return savedBankAccount;
    }


    @Override
    public List<Customer> listCustomers() {

        return customerRepository.findAll();
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
        return bankAccount;
    }

    @Override
    public BankAccount getBankAccountById(String accountId) throws BankAccountNotFoundException {
        return null;
    }


    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {

        BankAccount bankAccount = getBankAccount(accountId);

        if(bankAccount.getBalance()<amount)
            throw new BalanceNotSufficientException("Balance not sufficient");

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);

        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance()-amount);

        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {

        BankAccount bankAccount = getBankAccount(accountId);

        AccountOperation accountOperation = new AccountOperation();

        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);

        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance()+ amount);

        bankAccountRepository.save(bankAccount);


    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {

        debit(accountIdSource,amount, "transfer to" +  accountIdDestination);

        credit(accountIdDestination,amount, "transfer from " +  accountIdSource);

    }

    @Override
    public List<BankAccount> bankAccountList(){
        return bankAccountRepository.findAll();
    };
}