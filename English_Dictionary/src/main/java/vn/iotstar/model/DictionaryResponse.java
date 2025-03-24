package vn.iotstar.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryResponse {
	
	private String word;
	private String phonetic;
	private List<MeaningDTO> meanings;
	private List<PhoneticDTO> phonetics;
	

}
