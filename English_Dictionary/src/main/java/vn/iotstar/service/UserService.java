package vn.iotstar.service;

import java.util.List;
import java.util.Optional;

import vn.iotstar.entity.UserEntity;
import vn.iotstar.entity.WordEntity;
import vn.iotstar.model.FavoriteWordDTO;
import vn.iotstar.model.FolderDTO;

public interface UserService {

	Optional<UserEntity> findById(Integer id);

	<S extends UserEntity> S save(S entity);

	List<WordEntity> findWordSearchByUser(int userId);


	UserEntity addWordSearchByUser(int userId, Long wordId);

	List<FolderDTO> findFoldersByUser(int userId);

	void deleteById(Integer id);

	FolderDTO addFolderByUser(int userId, String folderName);

	FolderDTO updateFolderByUser(String folderName, int folderId);

	FavoriteWordDTO addFavoriteWord(int userId, int folderId, Long wordId);

}
