package vn.iotstar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.MeaningEntity;

@Repository
public interface MeaningRepository extends JpaRepository<MeaningEntity, Long>{

	@Query("Select m From MeaningEntity m Where m.word.word_id = :wordId")
	List<MeaningEntity> findMeaningsByWordId(@Param("wordId") Long wordId);
}
