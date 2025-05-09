package vn.iotstar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.WordEntity;

@Repository
public interface WordRepository extends JpaRepository<WordEntity, Long>{
	
	Optional<WordEntity> findByWord(String word);
	
	@Query(value = """
		    SELECT w.*
		    FROM word w
		    JOIN word_search ws ON w.word_id = ws.word_id
		    GROUP BY w.word_id
		    ORDER BY COUNT(ws.user_id) DESC
		    LIMIT 5
		    """, nativeQuery = true)
	List<WordEntity> findTop5MostSearchedWords();

}