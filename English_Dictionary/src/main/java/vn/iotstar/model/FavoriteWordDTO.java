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
	
	private String word;

	public FavoriteWordDTO(int user_id, int folder_id, long word_id) {
		this.user_id = user_id;
		this.folder_id = folder_id;
		this.word_id = word_id;
	}
	
	
}
