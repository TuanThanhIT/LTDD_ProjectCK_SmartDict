package vn.iotstar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneticDTO {
	
	private Long phonetic_id;
	private String text;
    private String audio;
    
    
}
