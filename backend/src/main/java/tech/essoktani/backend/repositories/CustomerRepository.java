package tech.essoktani.backend.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import tech.essoktani.backend.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}