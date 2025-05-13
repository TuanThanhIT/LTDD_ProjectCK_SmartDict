package vn.iotstar.service;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import vn.iotstar.entity.FolderFavorEntity;
import vn.iotstar.entity.UserEntity;
import vn.iotstar.model.FavoriteWordDTO;
import vn.iotstar.model.FolderDTO;
import vn.iotstar.model.UserDTO;
import vn.iotstar.model.WordDTO;

public interface UserService {

	Optional<UserEntity> findById(Integer id);

	<S extends UserEntity> S save(S entity);

	List<WordDTO> findWordSearchByUser(int userId);


	UserEntity addWordSearchByUser(int userId, Long wordId);

	List<FolderDTO> findFoldersByUser(int userId);

	void deleteById(Integer id);

	FolderDTO addFolderByUser(int userId, String folderName);

	FolderDTO updateFolderByUser(String folderName, int folderId);

	FavoriteWordDTO addFavoriteWord(int userId, int folderId, Long wordId);

	List<WordDTO> getWordsByFolder(int folderId);

	FolderDTO getFolderByWord(Long wordId, int userId);

	void deleteByWordId(Long wordId);

	boolean existsByUserIdAndWordId(int userId, Long wordId);

	List<FolderDTO> findOtherFoldersByUserIdAndWordId(int userId, Long wordId);

	void deleteByUserIdAndWordId(Long wordId, int userId);

	void updateFolderForWords(int folderId, int userId, List<Long> wordIds);

	void updateFolderForWord(int folderId, int userId, Long wordId);

	void deleteWordsByUserIdAndWordId(int userId, List<Long> wordIds);

	void addOrUpdateFavoriteWords(int userId, int folderId, List<Long> listWordId);

	void deleteSearchWords(int userId, List<Long> listSearchWords);

	List<UserDTO> findTop5UsersByCorrectAnswersAndTestTime();


}
