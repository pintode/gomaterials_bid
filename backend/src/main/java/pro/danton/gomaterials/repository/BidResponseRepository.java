package pro.danton.gomaterials.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pro.danton.gomaterials.model.BidResponse;

@Repository
public interface BidResponseRepository extends JpaRepository<BidResponse, Long> {
    @Query("SELECT COUNT(br) > 0 FROM BidResponse br WHERE br.id = :bidResponseId AND br.supplier.id = :supplierId")
    boolean existsByIdAndSupplierId(@Param("bidResponseId") Long bidResponseId, @Param("supplierId") Long supplierId);
}
