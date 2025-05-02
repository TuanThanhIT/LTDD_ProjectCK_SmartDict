package vn.iotstar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnswerDTO {
	
	private int answer_id;
	
	private String answer_text;
	
	private boolean isCorrect;

}
