package vn.iotstar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TestDTO {
	
	    private int id;
	    
	    private int test_time;

	    private int attempt;
	    
	    private int total_correct_answer;

	    private int user_id;

	    private int quiz_id;
	    
}
