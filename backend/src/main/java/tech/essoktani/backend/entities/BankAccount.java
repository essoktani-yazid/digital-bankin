package tech.essoktani.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.essoktani.backend.enums.AccountStatus;

import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4)
@Data @NoArgsConstructor
@AllArgsConstructor
public class BankAccount {

    @Id
    private String id;
    private Date CreatedAt;
    private Double balance;
    private AccountStatus AccStatus;
    private String Currency;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "bankAccount")
    private List<AccountOperation>accountOperations;

}