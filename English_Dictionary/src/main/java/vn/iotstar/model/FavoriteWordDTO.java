package vn.iotstar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteWordDTO {
	
	private int user_id;
	
	private int folder_id;
	
	private long word_id;

	
}
