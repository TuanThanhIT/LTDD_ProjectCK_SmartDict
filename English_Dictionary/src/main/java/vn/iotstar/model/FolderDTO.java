package vn.iotstar.model;

public class FolderDTO {
	
	private int folder_id;
	
	private String folder_name;

	public int getFolder_id() {
		return folder_id;
	}

	public void setFolder_id(int folder_id) {
		this.folder_id = folder_id;
	}

	public String getFolder_name() {
		return folder_name;
	}

	public void setFolder_name(String folder_name) {
		this.folder_name = folder_name;
	}

	public FolderDTO(int folder_id, String folder_name) {
		super();
		this.folder_id = folder_id;
		this.folder_name = folder_name;
	}
	
	

}
