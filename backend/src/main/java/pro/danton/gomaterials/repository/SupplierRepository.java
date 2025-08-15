package pro.danton.gomaterials.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pro.danton.gomaterials.model.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {}
