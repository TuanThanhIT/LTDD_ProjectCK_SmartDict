package vn.iotstar.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.entity.FavoriteWordEntity;
import vn.iotstar.entity.FolderFavorEntity;
import vn.iotstar.entity.UserEntity;
import vn.iotstar.entity.WordEntity;
import vn.iotstar.model.FavoriteWordDTO;
import vn.iotstar.model.FolderDTO;
import vn.iotstar.repository.FavoriteWordRepository;
import vn.iotstar.repository.FolderRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.repository.WordRepository;
import vn.iotstar.service.UserService;

@Service 
public class UserServiceImpl implements UserService { 
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private FolderRepository folderRepository;
	
	@Autowired
	private WordRepository wordRepository;
	
	@Autowired
	private FavoriteWordRepository favoriteWordRepository;

	@Override
	public Optional<UserEntity> findById(Integer id) {
		return userRepository.findById(id);
	}

	@Override
	public <S extends UserEntity> S save(S entity) {
		return userRepository.save(entity);
	}

	@Override
	public List<WordEntity> findWordSearchByUser(int userId) {
		return userRepository.findWordSearchByUser(userId);
	}
	
		
	@Override
	public List<FolderDTO> findFoldersByUser(int userId) {
		return folderRepository.findFoldersByUser(userId);
	}
	
	
	@Override
	public void deleteById(Integer id) {
		folderRepository.deleteById(id);
	}

	@Override
	public UserEntity addWordSearchByUser(int userId, Long wordId) {
		UserEntity user = userRepository.findById(userId)
			    .orElseThrow(() -> new RuntimeException("User not found"));
		
		WordEntity word = wordRepository.findById(wordId)
				.orElseThrow(() -> new RuntimeException("Word not fund"));
		
		if (!user.getWords().contains(word)) {
		    user.getWords().add(word);
		    word.getUsers().add(user);
		}
		
		return userRepository.save(user);
	}
	
	
	@Override
	public FolderDTO addFolderByUser(int userId, String folderName) {
	    UserEntity user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

	    // Kiểm tra trùng tên thư mục
	    boolean exists = user.getFolders().stream()
	            .anyMatch(f -> f.getFolder_name().equalsIgnoreCase(folderName));
	    if (exists) {
	        throw new RuntimeException("Tên thư mục đã tồn tại");
	    }

	    FolderFavorEntity folder = new FolderFavorEntity();
	    folder.setFolder_name(folderName);
	    folder.setUser(user);
	    folderRepository.save(folder);

	    return new FolderDTO(folder.getFolder_id(), folder.getFolder_name());
	}
	
	@Override
	public FolderDTO updateFolderByUser(String folderName, int folderId)
	{
		
		FolderFavorEntity folder = folderRepository.findById(folderId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy folder"));
		
		folder.setFolder_name(folderName);
		folderRepository.save(folder);
		
		return new FolderDTO(folder.getFolder_id(), folder.getFolder_name());
	}
	
	
	
	@Override
	public FavoriteWordDTO addFavoriteWord(int userId, int folderId, Long wordId)
	{
		
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
		
		FolderFavorEntity folder = folderRepository.findById(folderId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy folder"));
		
		WordEntity word = wordRepository.findById(wordId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy từ vựng"));
		
		FavoriteWordEntity favoriteWordEntity = new FavoriteWordEntity();
		favoriteWordEntity.setUser(user);
		favoriteWordEntity.setFolder(folder);
		favoriteWordEntity.setWord(word);
		
		favoriteWordRepository.save(favoriteWordEntity);

		return new FavoriteWordDTO(favoriteWordEntity.getUser().getUser_id(), favoriteWordEntity.getFolder().getFolder_id(), favoriteWordEntity.getWord().getWord_id());
	}


}
