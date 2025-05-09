package vn.iotstar.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.model.FavoriteWordDTO;
import vn.iotstar.model.FolderDTO;
import vn.iotstar.model.UserDTO;
import vn.iotstar.model.WordDTO;
import vn.iotstar.service.UserService;
import vn.iotstar.service.WordService;

@RequestMapping("/api/users")
@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WordService wordService;
	
	
	@PostMapping("/addWordSearch")
	public ResponseEntity<?> addWordSearch(@RequestParam int userId, @RequestParam Long wordId)
	{
		try {
			userService.addWordSearchByUser(userId, wordId);
		}
		catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
		return ResponseEntity.ok("Đã thêm từ được người dùng tìm kiếm");
	}

	
	@GetMapping("/{userId}/wordLookedUp")
	public ResponseEntity<List<WordDTO>> getWordLookedUp(@PathVariable Integer userId)
	{
		List<WordDTO> listWordLookedUp = new ArrayList<>();
		listWordLookedUp = userService.findWordSearchByUser(userId);
		return ResponseEntity.ok(listWordLookedUp);
	}
	

    @PostMapping("/addFolder")
    public ResponseEntity<?> addFolder(@RequestParam int userId, @RequestParam String folderName) {
        try {
            FolderDTO folder = userService.addFolderByUser(userId, folderName);
            return ResponseEntity.ok(folder);
        } catch (RuntimeException e) {
        	return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PatchMapping("/updateFolder")
    public ResponseEntity<?> updateFolder(@RequestParam int folderId, @RequestParam String folderName){
    	try {
    		FolderDTO folder = userService.updateFolderByUser(folderName, folderId);
    		return ResponseEntity.ok(folder);   		
    	} catch(RuntimeException e) {
    		return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    	}
    }
    
    @GetMapping("/{userId}/getFolders")
    public ResponseEntity<List<FolderDTO>> getFolders(@PathVariable int userId){
    	List<FolderDTO> listFolders = new ArrayList<>();
    	listFolders = userService.findFoldersByUser(userId);
    	return ResponseEntity.ok(listFolders);
    }
    
    @DeleteMapping("/deleteFolder/{folderId}")
    public ResponseEntity<?> deleteFolder(@PathVariable int folderId) {
        userService.deleteById(folderId);
        return ResponseEntity.ok("Xóa folder thành công với ID: " + folderId);
    }
    
    @PostMapping("/addFavoriteWord")
    public ResponseEntity<?> addFavoriteWord(@RequestParam int userId, @RequestParam int folderId, @RequestParam Long wordId){
    	try {
    		FavoriteWordDTO favoriteWordDTO = userService.addFavoriteWord(userId, folderId, wordId);
    		return ResponseEntity.ok(favoriteWordDTO);
    	} catch (RuntimeException e) {
    		return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
    }
    
    @GetMapping("/getWordsFolder/{folderId}")
    public ResponseEntity<?> getWordsByFolder(@PathVariable int folderId){
    	List<WordDTO> listWordsFolder = new ArrayList<>();
    	listWordsFolder = userService.getWordsByFolder(folderId);
    	return ResponseEntity.ok(listWordsFolder);
    }
    
    @GetMapping("/getFolderWord/{wordId}")
    public ResponseEntity<FolderDTO> getFolderByWord(@PathVariable Long wordId)
    {
    	FolderDTO folderDisplay = new FolderDTO();
    	folderDisplay = userService.getFolderByWord(wordId);
    	return ResponseEntity.ok(folderDisplay);
    }
    
    @DeleteMapping("/deleteWord/{wordId}")
    public ResponseEntity<?> deleteFavoriteWord(@PathVariable Long wordId) {
        userService.deleteByWordId(wordId);
        return ResponseEntity.ok("Từ này đã được xóa khỏi thư mục");
    }
    
    @GetMapping("/wordFavor/exists")
    public ResponseEntity<Boolean> checkFavorite(
            @RequestParam int userId,
            @RequestParam Long wordId) {
        
        boolean exists = userService.existsByUserIdAndWordId(userId, wordId);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/folders/except")
    public ResponseEntity<List<FolderDTO>> findFoldersExcept(@RequestParam int userId, @RequestParam Long wordId){
    	List<FolderDTO> listFolderDTOEx = new ArrayList<>();
    	listFolderDTOEx = userService.findOtherFoldersByUserIdAndWordId(userId, wordId);
    	return ResponseEntity.ok(listFolderDTOEx);
    }
    
    @DeleteMapping("/{userId}/deleteFavorWord/{wordId}")
    public ResponseEntity<?> deleteFavorWord(@PathVariable int userId, @PathVariable Long wordId)
    {
    	userService.deleteByUserIdAndWordId(wordId, userId);
    	return ResponseEntity.ok("Từ này đã được xóa khỏi thư mục");
    }
    
    @DeleteMapping("deleteFavorWords")
    public ResponseEntity<?> deleteFavorWords(@RequestParam int userId, @RequestParam List<Long> listWordIds)
    {
    	userService.deleteWordsByUserIdAndWordId(userId, listWordIds);
    	return ResponseEntity.ok("Các từ này đã được xóa khỏi thư mục");
    }
    
    @PatchMapping("/updateFolderWords")
    public ResponseEntity<?> updateFolderWords(@RequestParam int userId, @RequestParam int folderId, @RequestParam List<Long> listWordIds){
    	userService.updateFolderForWords(folderId, userId, listWordIds);
    	return ResponseEntity.ok("Thư mục của các từ được thay đổi thành công");
    }
    
    @PatchMapping("/updateFolderWord")
    public ResponseEntity<?> updateFolderWord(@RequestParam int userId, @RequestParam int folderId, @RequestParam Long wordId){
    	userService.updateFolderForWord(folderId, userId, wordId);
    	return ResponseEntity.ok("Thư mục của từ được thay đổi thành công");
    }
    
    @PostMapping("/addFavorWords")
    public ResponseEntity<?> addOrUpdateFavorWords(@RequestParam int userId, @RequestParam int folderId, @RequestParam List<Long> listWords){	
    	try {
    		userService.addOrUpdateFavoriteWords(userId, folderId, listWords);
    	} catch (RuntimeException e) {
    		return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
    	return ResponseEntity.ok("Các từ vựng này đã được thêm vào thư mục thành công");
    }
    
    @DeleteMapping("/deleteSearchWords")
    public ResponseEntity<?> deleteSearchWords(@RequestParam int userId, @RequestParam List<Long> listSearchWords){
    	userService.deleteSearchWords(userId, listSearchWords);
    	return ResponseEntity.ok("Các từ này đã được xóa khỏi Từ đã tra");
    }
    
    @GetMapping("/top5UsersBest")
    public ResponseEntity<List<UserDTO>> getTop5Users(){
    	List<UserDTO> listUserDTOs = new ArrayList<>();
    	listUserDTOs = userService.findTop5UsersByCorrectAnswersAndTestTime();
    	return ResponseEntity.ok(listUserDTOs);
    }
  
}
