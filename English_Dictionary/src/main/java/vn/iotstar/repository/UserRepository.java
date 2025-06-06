package vn.iotstar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import vn.iotstar.entity.UserEntity;
import vn.iotstar.entity.WordEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>{
	
	Optional<UserEntity> findByEmail(String email);
	@Query("Select u.words From UserEntity u Where u.user_id = :userId")
	List<WordEntity> findWordSearchByUser(@Param("userId") int userId);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM word_search WHERE user_id = :userId AND word_id IN (:listSearchWords)", nativeQuery = true)
	void deleteSearchWords(@Param("userId") int userId, @Param("listSearchWords") List<Long> listSearchWords);
	
	@Query(value = """
		    SELECT u.*
		    FROM user u
		    JOIN (
		        SELECT user_id, SUM(total_correct_answer) AS total_correct, SUM(test_time) AS total_time
		        FROM test
		        GROUP BY user_id
		    ) t ON u.user_id = t.user_id
		    ORDER BY t.total_correct DESC, t.total_time ASC
		    LIMIT 5
		    """, nativeQuery = true)
	List<UserEntity> findTop5UsersByCorrectAnswersAndTestTime();
}
