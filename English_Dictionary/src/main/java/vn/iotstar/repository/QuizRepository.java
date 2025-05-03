package vn.iotstar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.QuestionEntity;
import vn.iotstar.entity.QuizEntity;

@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, Integer>{
	
	@Query("SELECT q.questions FROM QuizEntity q WHERE q.quiz_id = :quizId")
	List<QuestionEntity> getQuestionsByQuizId(@Param("quizId") int quizId);

}
