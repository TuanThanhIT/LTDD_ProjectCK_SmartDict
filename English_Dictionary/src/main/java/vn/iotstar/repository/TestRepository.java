package vn.iotstar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.TestEntity;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Integer>{

	@Query("SELECT COUNT(t) FROM TestEntity t WHERE t.user.user_id = :userId AND t.quiz.quiz_id = :quizId")
    int countAttempts(@Param("userId") int userId, @Param("quizId") int quizId);
	
	@Query(value = "SELECT id FROM test WHERE user_id = :userId AND quiz_id = :quizId ORDER BY attempt DESC LIMIT 1", nativeQuery = true)
	int findTestIdHasMaxAttempt(@Param("userId") int userId, @Param("quizId") int quizId);
	
	// Tìm Test theo userId, quizId, và attempt lớn nhất
    @Query("SELECT t FROM TestEntity t WHERE t.user.id = :userId AND t.quiz.id = :quizId AND t.attempt = (" +
           "SELECT MAX(t2.attempt) FROM TestEntity t2 WHERE t2.user.id = :userId AND t2.quiz.id = :quizId)")
    Optional<TestEntity> findLatestAttemptByUserAndQuiz(@Param("userId") int userId, @Param("quizId") int quizId);
    
    @Query("SELECT t FROM TestEntity t WHERE t.user.user_id = :userId")
    List<TestEntity> findByUserId(@Param("userId") int userId);
}
