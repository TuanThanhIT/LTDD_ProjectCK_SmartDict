package vn.iotstar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.WordEntity;

@Repository
public interface WordRepository extends JpaRepository<WordEntity, Long>{
	
	Optional<WordEntity> findByWord(String word);
}