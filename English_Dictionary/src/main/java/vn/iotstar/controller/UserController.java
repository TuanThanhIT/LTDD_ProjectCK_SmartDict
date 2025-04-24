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

import vn.iotstar.entity.FavoriteWordEntity;
import vn.iotstar.entity.WordEntity;
import vn.iotstar.model.FavoriteWordDTO;
import vn.iotstar.model.FolderDTO;
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
	public ResponseEntity<List<WordEntity>> getWordLookedUp(@PathVariable Integer userId)
	{
		List<WordEntity> listWordLookedUp = new ArrayList<>();
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
}
