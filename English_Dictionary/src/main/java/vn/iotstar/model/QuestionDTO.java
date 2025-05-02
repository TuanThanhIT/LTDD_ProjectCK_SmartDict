package vn.iotstar.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
	
	private int question_id;

	private String question_test;
	
	private List<AnswerDTO> listAnswers;

}
