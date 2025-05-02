package vn.iotstar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserAnswerDTO {
	
	private int question_id;
	private int answer_id;
	private int test_id;
}
