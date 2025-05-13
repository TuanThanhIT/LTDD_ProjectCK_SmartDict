package vn.iotstar.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.entity.WordEntity;
import vn.iotstar.model.WordDTO;
import vn.iotstar.service.WordService;

@RestController
@RequestMapping("/api/words")
public class WordController {

    @Autowired
    private WordService wordService;

    @PostMapping("add")
    public ResponseEntity<?> addWord(@RequestParam String word) {
        try {
            WordEntity newWord = wordService.addWord(word);
            return ResponseEntity.ok(newWord);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("all")
    public ResponseEntity<List<WordDTO>> getAllWord(){
    	List<WordDTO> listWords = wordService.findWordAll();
    	return ResponseEntity.ok(listWords);
    }
    
    @GetMapping("/top5WordSearchs")
    public ResponseEntity<List<WordDTO>> getTop5WordSearchs(){ 
    	List<WordDTO> listWordDTOs = new ArrayList<>();
    	listWordDTOs = wordService.findTop5MostSearchedWords();
    	return ResponseEntity.ok(listWordDTOs);
    }
    
    @GetMapping("/top5WordFavors")
    public ResponseEntity<List<WordDTO>> getTop5WordFavors(){ 
    	List<WordDTO> listWordDTOs = new ArrayList<>();
    	listWordDTOs = wordService.findTop5MostFavoritedWords();
    	return ResponseEntity.ok(listWordDTOs);
    }
}
