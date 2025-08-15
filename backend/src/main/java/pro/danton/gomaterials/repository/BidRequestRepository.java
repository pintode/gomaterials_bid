package pro.danton.gomaterials.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import pro.danton.gomaterials.model.BidRequest;

@Repository
public interface BidRequestRepository extends JpaRepository<BidRequest, Long> {

    @Query("SELECT br FROM BidRequest br LEFT JOIN FETCH br.plantItems pi " +
            "WHERE (br.landscaper.id = :landscaperId OR :landscaperId IS NULL) " +
            "ORDER BY br.id, pi.id")
    List<BidRequest> findAllWithLandscaperAndPlantItems(@Param("landscaperId") Optional<Long> landscaperId);

    @Query("SELECT br FROM BidRequest br LEFT JOIN FETCH br.plantItems pi WHERE br.id = :id ORDER BY br.id, pi.id")
    BidRequest findByIdWithPlantItems(@Param("id") Long id);

    default BidRequest findByIdWithPlantItemsAndBidResponses(Long id) {
        BidRequest bidRequest = findByIdWithPlantItems(id);
        bidRequest.getBidResponses().size();
        return bidRequest;
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT br.id FROM BidRequest br WHERE br.id = :id")
    Optional<BidRequest> lockById(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT br.id FROM BidRequest br JOIN br.bidResponses b WHERE b.id = :bidResponseId")
    Optional<Long> lockByBidResponseIdInternal(@Param("bidResponseId") Long bidResponseId);

    default void lockByBidResponseId(Long bidResponseId) {
        lockByBidResponseIdInternal(bidResponseId).orElseThrow(() -> new RuntimeException("Bid request not found"));
    }

    @Query("SELECT COUNT(br) > 0 FROM BidRequest br WHERE br.id = :bidRequestId AND br.landscaper.id = :landscaperId")
    boolean existsByIdAndLandscaperId(@Param("bidRequestId") Long bidRequestId,
            @Param("landscaperId") Long landscaperId);
}
