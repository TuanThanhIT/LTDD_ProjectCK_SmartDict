package vn.iotstar.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeaningDTO {
	 private Long meaning_id;
	 private String partOfSpeech;
	 private  String vietNamese;
	 private List<DefinitionDTO> definitions;
}
