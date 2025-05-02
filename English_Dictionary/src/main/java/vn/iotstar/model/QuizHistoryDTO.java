package vn.iotstar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuizHistoryDTO {
	
	private String quizTitle;
	
	private int attempt;
	
	private int totalCorrectAnswer;
	
	private int testTime;
	
	private int totalQuestion;
	
	private int userId;
	
	private int quizId;
}