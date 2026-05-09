package tech.essoktani.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.essoktani.backend.entities.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}