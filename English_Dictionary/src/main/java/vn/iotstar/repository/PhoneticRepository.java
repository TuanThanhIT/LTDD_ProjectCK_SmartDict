package vn.iotstar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.PhoneticEntity;

@Repository
public interface PhoneticRepository extends JpaRepository<PhoneticEntity, Long>{

	@Query("Select p From PhoneticEntity p Where p.word.word_id = :wordId")
	List<PhoneticEntity> findPhoneticsByWordId(@Param("wordId") Long wordId);
}
