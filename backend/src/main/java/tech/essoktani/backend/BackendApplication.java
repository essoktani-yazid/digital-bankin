package tech.essoktani.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tech.essoktani.backend.entities.*;
import tech.essoktani.backend.enums.AccountStatus;
import tech.essoktani.backend.enums.OperationType;
import tech.essoktani.backend.repositories.AccountOperationRepository;
import tech.essoktani.backend.repositories.BankAccountRepository;
import tech.essoktani.backend.repositories.CustomerRepository;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountRepository bankAccountRepository) {
        return args -> {

            BankAccount bankAccount = bankAccountRepository
                    .findById("11eae1bd-b3b9-4f48-92f2-33c5a63ebffa")
                    .orElse(null);

            if (bankAccount != null) {

                System.out.println(bankAccount.getId());
                System.out.println(bankAccount.getBalance());
                System.out.println(bankAccount.getAccStatus());
                System.out.println(bankAccount.getCreatedAt());
                System.out.println(bankAccount.getCustomer().getName());
                System.out.println(bankAccount.getClass().getSimpleName());

                if (bankAccount instanceof CurrentAccount) {
                    System.out.println("Over draft => "
                            + ((CurrentAccount) bankAccount).getOverDraft());

                } else if (bankAccount instanceof SavingAccount) {
                    System.out.println("Rate => "
                            + ((SavingAccount) bankAccount).getInterestRate());
                }

                bankAccount.getAccountOperations().forEach(op -> {
                    System.out.println("============");
                    System.out.println(
                            op.getType() + "\t"
                                    + op.getOperationDate() + "\t"
                                    + op.getAmount()
                    );
                });

            } else {
                System.out.println("Bank account not found");
            }
        };
    }

//    @Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository) {

        return args -> {

            Stream.of("Hassan", "Yassine", "Aicha").forEach(name -> {

                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");

                customerRepository.save(customer);
            });

            customerRepository.findAll().forEach(customer -> {

                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setCustomer(customer);
                currentAccount.setBalance(Math.random() * 90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setAccStatus(AccountStatus.CREATED);
                currentAccount.setOverDraft(9000);

                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setCustomer(customer);
                savingAccount.setBalance(Math.random() * 90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setAccStatus(AccountStatus.CREATED);
                savingAccount.setInterestRate(5.5);

                bankAccountRepository.save(savingAccount);
            });

            bankAccountRepository.findAll().forEach(bankAccount -> {

                for (int i = 0; i < 10; i++) {

                    AccountOperation accountOperation = new AccountOperation();

                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random() * 12000);
                    accountOperation.setType(
                            Math.random() > 0.5
                                    ? OperationType.DEBIT
                                    : OperationType.CREDIT
                    );

                    accountOperation.setBankAccount(bankAccount);

                    accountOperationRepository.save(accountOperation);
                }
            });
        };
    }
}
