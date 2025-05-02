package vn.iotstar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.DefinitionEntity;

@Repository
public interface DefinitionRepository extends JpaRepository<DefinitionEntity, Long>{
	
	@Query("Select d From DefinitionEntity d Where d.meaning.meaning_id = :meaningId")
	List<DefinitionEntity> findDefinitionsByMeaningId(@Param("meaningId") Long meaningId);
}
