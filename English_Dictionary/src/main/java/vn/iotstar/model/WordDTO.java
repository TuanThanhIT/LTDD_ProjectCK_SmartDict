package vn.iotstar.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WordDTO {
	
	private Long word_id;
	
	private String word;
	
	private List<MeaningDTO> meanings;
	
	private List<PhoneticDTO> phonetics;
	
	public WordDTO(Long word_id, String word) {
		this.word_id = word_id;
		this.word = word;
	}
	
	
}
