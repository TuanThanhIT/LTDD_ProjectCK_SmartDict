package vn.iotstar.service;

import java.util.List;

import jakarta.transaction.Transactional;
import vn.iotstar.model.QuestionDTO;
import vn.iotstar.model.QuizDTO;
import vn.iotstar.model.QuizHistoryDTO;
import vn.iotstar.model.TestDTO;
import vn.iotstar.model.UserAnswerDTO;

public interface QuizService {

	List<QuizDTO> findAllQuiz();

	List<QuestionDTO> getQuestionsAndAnswers(int quizId);

	TestDTO addTest(int userId, int quizId);

	int findTestIdHasMaxAttempt(int userId, int quizId);

	void saveUserAnswer(UserAnswerDTO userAnswerDTO);

	boolean updateTestResult(int userId, int quizId, int testTime);

	TestDTO findLatestAttemptByUserAndQuiz(int userId, int quizId);

	List<QuizHistoryDTO> findByUserId(int userId);

}
