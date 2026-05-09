package tech.essoktani.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.essoktani.backend.entities.AccountOperation;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
}