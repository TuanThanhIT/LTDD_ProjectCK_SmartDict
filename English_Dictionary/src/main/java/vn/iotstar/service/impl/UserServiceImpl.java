package vn.iotstar.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.iotstar.entity.FavoriteWordEntity;
import vn.iotstar.entity.FolderFavorEntity;
import vn.iotstar.entity.UserEntity;
import vn.iotstar.entity.WordEntity;
import vn.iotstar.model.FavoriteWordDTO;
import vn.iotstar.model.FolderDTO;
import vn.iotstar.model.UserDTO;
import vn.iotstar.model.WordDTO;
import vn.iotstar.repository.FavoriteWordRepository;
import vn.iotstar.repository.FolderRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.repository.WordRepository;
import vn.iotstar.service.UserService;
import vn.iotstar.utils.WordMapper;

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
	
	
	@Autowired
	private WordMapper wordMapper;

	@Override
	public Optional<UserEntity> findById(Integer id) {
		return userRepository.findById(id);
	}

	@Override
	public <S extends UserEntity> S save(S entity) {
		return userRepository.save(entity);
	}

	@Override
	public List<WordDTO> findWordSearchByUser(int userId) {
	    List<WordEntity> listWords = userRepository.findWordSearchByUser(userId);
	    return listWords.stream()
	                    .map(wordMapper::convertToDTO)
	                    .collect(Collectors.toList());
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
	public List<FolderDTO> findFoldersByUser(int userId) {
		return folderRepository.findFoldersByUser(userId);
	}
	
	
	@Override
	public void deleteById(Integer id) {
		folderRepository.deleteById(id);
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
	public FolderDTO getFolderByWord(Long wordId, int userId) {
	    FolderFavorEntity folderFavorEntity = favoriteWordRepository.getFolderByWordAndUser(wordId, userId);

	    FolderDTO folder = new FolderDTO();
	    if (folderFavorEntity != null) {
	        folder.setFolder_id(folderFavorEntity.getFolder_id());
	        folder.setFolder_name(folderFavorEntity.getFolder_name());
	    } else {
	        // Nếu không tìm thấy folder, trả về mặc định "Từ đã tra"
	        folder.setFolder_id(-1); // Hoặc -1, tùy bạn quy ước
	        folder.setFolder_name("TỪ ĐÃ TRA");
	    }
	    return folder;
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
	
	@Override
	public List<WordDTO> getWordsByFolder(int folderId){
		List<WordEntity> listWords = favoriteWordRepository.findWordsByFolderId(folderId);
	    return listWords.stream()
	                    .map(wordMapper::convertToDTO)
	                    .collect(Collectors.toList());
	}

	@Override
	public void deleteByWordId(Long wordId) {
		favoriteWordRepository.deleteByWordId(wordId);
	}
	

	@Override
	public boolean existsByUserIdAndWordId(int userId, Long wordId) {
		return favoriteWordRepository.existsByUserIdAndWordId(userId, wordId);
	}
	
	@Override
	public List<FolderDTO> findOtherFoldersByUserIdAndWordId(int userId, Long wordId){
		List<FolderFavorEntity> listFolders = favoriteWordRepository.findOtherFoldersByUserIdAndWordId(userId, wordId);
		List<FolderDTO> listFolderDTO = new ArrayList<>();
		for(FolderFavorEntity folder: listFolders) {
			FolderDTO folderDTO = new FolderDTO();
			folderDTO.setFolder_id(folder.getFolder_id());
			folderDTO.setFolder_name(folder.getFolder_name());
			listFolderDTO.add(folderDTO);
		}
		return listFolderDTO;
	}

	@Override
	public void deleteByUserIdAndWordId(Long wordId, int userId) {
		favoriteWordRepository.deleteByUserIdAndWordId(wordId, userId);
	}

	@Override
	public void deleteWordsByUserIdAndWordId(int userId, List<Long> wordIds) {
		favoriteWordRepository.deleteWordsByUserIdAndWordId(userId, wordIds);
	}

	@Override
	public void updateFolderForWords(int folderId, int userId, List<Long> wordIds) {
		favoriteWordRepository.updateFolderForWords(folderId, userId, wordIds);
	}

	@Override
	public void updateFolderForWord(int folderId, int userId, Long wordId) {
		favoriteWordRepository.updateFolderForWord(folderId, userId, wordId);
	}
	
	@Override
	@Transactional
	public void addOrUpdateFavoriteWords(int userId, int folderId, List<Long> listWordId) {
	    UserEntity user = userRepository.findById(userId).orElseThrow();
	    FolderFavorEntity folder = folderRepository.findById(folderId).orElseThrow();

	    for (Long wordId : listWordId) {
	        WordEntity word = wordRepository.findById(wordId).orElseThrow();

	        FavoriteWordEntity existing = favoriteWordRepository.findByUserIdAndWordId(userId, wordId);
	        if (existing != null) {
	            existing.setFolder(folder); // cập nhật folder
	            favoriteWordRepository.save(existing);
	        } else {
	            FavoriteWordEntity newFavor = new FavoriteWordEntity();
	            newFavor.setUser(user);
	            newFavor.setWord(word);
	            newFavor.setFolder(folder);
	            favoriteWordRepository.save(newFavor);
	        }
	    }
	}

	@Override
	public void deleteSearchWords(int userId, List<Long> listSearchWords) {
		userRepository.deleteSearchWords(userId, listSearchWords);
	}
	
	@Override
	public List<UserDTO> findTop5UsersByCorrectAnswersAndTestTime(){
		List<UserDTO> listUserDTO = new ArrayList<>();
		List<UserEntity> listUsers = new ArrayList<>();
		listUsers = userRepository.findTop5UsersByCorrectAnswersAndTestTime();
		for(UserEntity u: listUsers) {
			UserDTO userDTO = new UserDTO();
			userDTO.setUser_id(u.getUser_id());
			userDTO.setFullname(u.getFullname());
			listUserDTO.add(userDTO);
		}
		return listUserDTO;
	}

	
}

