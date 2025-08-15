package pro.danton.gomaterials.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pro.danton.gomaterials.model.Supplier;
import pro.danton.gomaterials.repository.SupplierRepository;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    @Transactional
    public Supplier create(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Optional<Supplier> findById(Long id) {
        return supplierRepository.findById(id);
    }

    public List<Supplier> findAll() {
        return supplierRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }
}
