package vn.iotstar.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.iotstar.entity.AnswerEntity;
import vn.iotstar.entity.QuestionEntity;
import vn.iotstar.entity.QuizEntity;
import vn.iotstar.entity.TestEntity;
import vn.iotstar.entity.UserAnswerEntity;
import vn.iotstar.entity.UserEntity;
import vn.iotstar.model.AnswerDTO;
import vn.iotstar.model.QuestionDTO;
import vn.iotstar.model.QuizDTO;
import vn.iotstar.model.QuizHistoryDTO;
import vn.iotstar.model.TestDTO;
import vn.iotstar.model.UserAnswerDTO;
import vn.iotstar.repository.AnswerRepository;
import vn.iotstar.repository.QuestionRepository;
import vn.iotstar.repository.QuizRepository;
import vn.iotstar.repository.TestRepository;
import vn.iotstar.repository.UserAnswerRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.QuizService;

@Service
public class QuizServiceImpl implements QuizService{

	@Autowired 
	private QuizRepository quizRepository;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private AnswerRepository answerRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TestRepository testRepository;
	
	@Autowired
	private UserAnswerRepository userAnswerRepository;
	
	

	@Override
	public List<QuizDTO> findAllQuiz(){
		List<QuizDTO> listQuizDTO = new ArrayList<>();
		List<QuizEntity> listQuiz = quizRepository.findAll();
		for(QuizEntity quiz: listQuiz)
		{
			QuizDTO quizDTO = new QuizDTO();
			quizDTO.setQuiz_id(quiz.getQuiz_id());
			quizDTO.setTitle(quiz.getTitle());
			quizDTO.setDescription(quiz.getDescription());
			quizDTO.setImage(quiz.getImage());
			quizDTO.setTime_limit(quiz.getTime_limit());
			quizDTO.setTotal_question(quiz.getTotal_question());
			listQuizDTO.add(quizDTO);
		}
		return listQuizDTO;
	}
	
	
	@Override
	public List<QuestionDTO> getQuestionsAndAnswers(int quizId)
	{
		List<QuestionEntity> listQuestions = quizRepository.getQuestionsByQuizId(quizId);
		List<QuestionDTO> listQuestionsDTO = new ArrayList<>();
		for(QuestionEntity question : listQuestions) {
			QuestionDTO questionDTO = new QuestionDTO();
			
			questionDTO.setQuestion_id(question.getQuestion_id());
			questionDTO.setQuestion_test(question.getQuestion_test());
			
			List<AnswerDTO> listAnswerDTO = new ArrayList<>();
			for(AnswerEntity answer: question.getAnswers()) {
				AnswerDTO answerDTO = new AnswerDTO();
				answerDTO.setAnswer_id(answer.getAnswer_id());
				answerDTO.setAnswer_text(answer.getAnswer_text());
				answerDTO.setCorrect(answer.isCorrect());
				listAnswerDTO.add(answerDTO);
			}
			
			questionDTO.setListAnswers(listAnswerDTO);
			listQuestionsDTO.add(questionDTO);
		}
		return listQuestionsDTO;
	}
	
	@Override
	public TestDTO addTest(int userId, int quizId)
	{
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
		
		QuizEntity quiz = quizRepository.findById(quizId)
			.orElseThrow(() -> new RuntimeException("Không tìm thấy bài quiz"));
		
	       // Đếm số lần đã làm bài
        int previousAttempts = testRepository.countAttempts(userId, quizId) + 1;
        
        TestEntity test = new TestEntity();
        test.setAttempt(previousAttempts);
        test.setQuiz(quiz);
        test.setUser(user);
        test.setTotalCorrectAnswer(0);
        test.setTestTime(0);
        
        testRepository.save(test);
        
        TestDTO testDTO = new TestDTO(test.getId(), test.getAttempt(),test.getTestTime(), test.getTotalCorrectAnswer(), test.getQuiz().getQuiz_id(), test.getUser().getUser_id());
        
        return testDTO;
	}
		

	@Override
	public int findTestIdHasMaxAttempt(int userId, int quizId) {
		return testRepository.findTestIdHasMaxAttempt(userId, quizId);
	}	
	
	@Override
	public void saveUserAnswer(UserAnswerDTO userAnswerDTO)
	{
		UserAnswerEntity userAnswer = new UserAnswerEntity();
		QuestionEntity question = questionRepository.findById(userAnswerDTO.getQuestion_id())
				.orElseThrow(()-> new RuntimeException("Không tìm thấy question"));
		AnswerEntity answer = answerRepository.findById(userAnswerDTO.getAnswer_id())
				.orElseThrow(()-> new RuntimeException("Không tìm thấy user"));
		TestEntity test = testRepository.findById(userAnswerDTO.getTest_id())
				.orElseThrow(()-> new RuntimeException("Không tìm thấy test"));
		
		userAnswer.setQuestion(question);
		userAnswer.setTest(test);
		userAnswer.setSelectedAnswer(answer);
		userAnswerRepository.save(userAnswer);
	}
	
	@Override
	@Transactional
    public boolean updateTestResult(int userId, int quizId, int testTime) {
        Optional<TestEntity> optionalTest = testRepository.findLatestAttemptByUserAndQuiz(userId, quizId);

        if (optionalTest.isPresent()) {
            TestEntity test = optionalTest.get();

            // Lọc ra số câu trả lời đúng (answer.isCorrect = true)
            long correctCount = test.getUserAnswers()
                .stream()
                .filter(ans -> ans.getSelectedAnswer() != null && ans.getSelectedAnswer().isCorrect())
                .count();

            test.setTestTime(testTime);
            test.setTotalCorrectAnswer((int) correctCount);

            testRepository.save(test);
            return true;
        }
        return false;
    }

	
	@Override
	public TestDTO findLatestAttemptByUserAndQuiz(int userId, int quizId) {
		Optional<TestEntity> optionalTest = testRepository.findLatestAttemptByUserAndQuiz(userId, quizId);
		TestDTO testDTO = new TestDTO();
		if(optionalTest.isPresent())
		{
			TestEntity test = optionalTest.get();
			testDTO.setId(test.getId());
			testDTO.setAttempt(test.getAttempt());
			testDTO.setTest_time(test.getTestTime());
			testDTO.setTotal_correct_answer(test.getTotalCorrectAnswer());
			testDTO.setQuiz_id(test.getQuiz().getQuiz_id());
			testDTO.setUser_id(test.getUser().getUser_id());
		}
		return testDTO;
	}
	
	@Override
	public List<QuizHistoryDTO> findByUserId(int userId)
	{
		List<QuizHistoryDTO> listQuizHisDTO = new ArrayList<>();
		List<TestEntity> listTest =  testRepository.findByUserId(userId);
		for(TestEntity test: listTest) {
			QuizHistoryDTO testDTO = new QuizHistoryDTO();
			testDTO.setAttempt(test.getAttempt());
			testDTO.setTestTime(test.getTestTime());
			testDTO.setTotalCorrectAnswer(test.getTotalCorrectAnswer());
			testDTO.setQuizTitle(test.getQuiz().getTitle());
			testDTO.setTotalQuestion(test.getQuiz().getTotal_question());
			testDTO.setUserId(test.getUser().getUser_id());
			testDTO.setQuizId(test.getQuiz().getQuiz_id());
			listQuizHisDTO.add(testDTO);
		}
		return listQuizHisDTO;
	}
}
