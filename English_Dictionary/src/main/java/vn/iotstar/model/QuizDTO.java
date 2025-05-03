package vn.iotstar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuizDTO {
	
	private int quiz_id;
	
	private String title;
	
	private String image;
	
	private String description;
	
	private int total_question;
	
	private int time_limit;

}
