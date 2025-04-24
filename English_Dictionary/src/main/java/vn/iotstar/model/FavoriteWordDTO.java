package vn.iotstar.model;

public class FavoriteWordDTO {
	
	private int id;
	
	private int user_id;
	
	private int folder_id;
	
	private long word_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getFolder_id() {
		return folder_id;
	}

	public void setFolder_id(int folder_id) {
		this.folder_id = folder_id;
	}

	public long getWord_id() {
		return word_id;
	}

	public void setWord_id(long word_id) {
		this.word_id = word_id;
	}

	public FavoriteWordDTO(int user_id, int folder_id, long word_id) {
		this.user_id = user_id;
		this.folder_id = folder_id;
		this.word_id = word_id;
	}

	public FavoriteWordDTO() {
	}
}
