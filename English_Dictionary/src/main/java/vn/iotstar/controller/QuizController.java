package vn.iotstar.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.model.QuestionDTO;
import vn.iotstar.model.QuizDTO;
import vn.iotstar.model.QuizHistoryDTO;
import vn.iotstar.model.TestDTO;
import vn.iotstar.model.UserAnswerDTO;
import vn.iotstar.service.QuizService;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
	
	@Autowired
	private QuizService quizService;
	
	@GetMapping("getQuizzes")
	public ResponseEntity<?> getQuizzes(){
		List<QuizDTO> listQuiz = new ArrayList<>();
		listQuiz = quizService.findAllQuiz();
		return ResponseEntity.ok(listQuiz);
	}
	
	@GetMapping("{quizId}/getQuestionsAndAnswers")
	public ResponseEntity<?> getQuestionsAndAnswers(@PathVariable("quizId") int quizId){
		List<QuestionDTO> listQuestions = new ArrayList<>();
		listQuestions = quizService.getQuestionsAndAnswers(quizId);
		return ResponseEntity.ok(listQuestions);
	}
	
	@PostMapping("addTest")
	public ResponseEntity<?> addTest(@RequestParam int userId, @RequestParam int quizId){
		TestDTO testDTO = new TestDTO();
		try {
			testDTO = quizService.addTest(userId, quizId);
		}
		catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
		return ResponseEntity.ok(testDTO);
	}
	
	@GetMapping("getTestId")
	public ResponseEntity<?> getTestId(@RequestParam int userId, @RequestParam int quizId){
		int testIdMax = quizService.findTestIdHasMaxAttempt(userId, quizId);
		return ResponseEntity.ok(testIdMax);
	}
	
	@PostMapping("userAnswer")
	public ResponseEntity<?> saveUserAnswer(@RequestBody UserAnswerDTO userAnswerDTO) {	
		try {
			quizService.saveUserAnswer(userAnswerDTO);
		}
		catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
		return ResponseEntity.ok("Câu trả lời của bạn đã được lưu lại");	
	}

	
	@PostMapping("/updateResult")
    public ResponseEntity<?> updateTestResult(
            @RequestParam int userId,
            @RequestParam int quizId,
            @RequestParam int testTime) {

        boolean success = quizService.updateTestResult(userId, quizId, testTime);
        if (success) {
            return ResponseEntity.ok("Cập nhật kết quả thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy bài test phù hợp!");
        }
    }
	
	@GetMapping("/getTestResult")
	public ResponseEntity<?> getTestResult(@RequestParam int userId, @RequestParam int quizId){
		TestDTO testDTO = new TestDTO();
		testDTO = quizService.findLatestAttemptByUserAndQuiz(userId, quizId);
		return ResponseEntity.ok(testDTO);
	}
	
	@GetMapping("/getHistoryTest/{userId}")
	public ResponseEntity<?> getHistoryTest(@PathVariable("userId") int userId){
		List<QuizHistoryDTO> listTestDTO = new ArrayList<>();
		listTestDTO = quizService.findByUserId(userId);
		return ResponseEntity.ok(listTestDTO);
	}
}
