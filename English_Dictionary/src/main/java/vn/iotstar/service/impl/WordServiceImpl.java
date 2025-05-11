package vn.iotstar.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vn.iotstar.entity.DefinitionEntity;
import vn.iotstar.entity.MeaningEntity;
import vn.iotstar.entity.PhoneticEntity;
import vn.iotstar.entity.WordEntity;
import vn.iotstar.model.DictionaryResponse;
import vn.iotstar.model.WordDTO;
import vn.iotstar.repository.FavoriteWordRepository;
import vn.iotstar.repository.WordRepository;
import vn.iotstar.service.WordService;
import vn.iotstar.utils.WordMapper;

@Service
public class WordServiceImpl implements WordService{

    @Autowired
    private WordRepository wordRepository;
    
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private WordMapper wordMapper;
    
    @Autowired
    private FavoriteWordRepository favoriteWordRepository;

    private static final String API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    @Override
	public WordEntity addWord(String word) {
    	if (wordRepository.findByWord(word).isPresent()) {
            throw new IllegalArgumentException("Từ đã tồn tại trong hệ thống!");
        }

        DictionaryResponse[] response = fetchFromApi(word);
        if (response != null && response.length > 0) {
            return saveWord(response[0]);
        }
        throw new IllegalArgumentException("Không tìm thấy từ này trên API!");
    }
    
        
    @Override
    public List<WordDTO> findWordAll() {
        List<WordEntity> listWords = wordRepository.findAll();
        return listWords.stream()
                        .map(wordMapper::convertToDTO)
                        .collect(Collectors.toList());
    }
    
    
    @Override
	public Optional<WordEntity> findById(Long id) {
		return wordRepository.findById(id);
	}

    

	private DictionaryResponse[] fetchFromApi(String word) {
        try {
            return restTemplate.getForObject(API_URL + word, DictionaryResponse[].class);
        } catch (Exception e) {
            return null;
        }
    }

    private WordEntity saveWord(DictionaryResponse response) {
    	WordEntity wordEntity = new WordEntity();
        wordEntity.setWord(response.getWord());
        wordEntity.setPhonetic(response.getPhonetic());

        List<PhoneticEntity> phonetics = response.getPhonetics().stream().map(p -> {
            PhoneticEntity phonetic = new PhoneticEntity();
            phonetic.setText(p.getText());
            phonetic.setAudio(p.getAudio());
            phonetic.setWord(wordEntity);
            return phonetic;
        }).collect(Collectors.toList());

        wordEntity.setPhonetics(phonetics);

        List<MeaningEntity> meanings = response.getMeanings().stream().map(m -> {
            MeaningEntity meaning = new MeaningEntity();
            meaning.setPartOfSpeech(m.getPartOfSpeech());
            meaning.setWord(wordEntity);
            
            List<DefinitionEntity> definitions = m.getDefinitions().stream().map(d -> {
            	DefinitionEntity definition = new DefinitionEntity();
                
                definition.setDefinition(d.getDefinition());
                definition.setExample(d.getExample());

                definition.setMeaning(meaning);
                return definition;
            }).collect(Collectors.toList());
            
            

            meaning.setDefinitions(definitions);
            return meaning;
        }).collect(Collectors.toList());

        wordEntity.setMeanings(meanings);

        return wordRepository.save(wordEntity);
    }
    
    @Override
	public List<WordDTO> findTop5MostSearchedWords(){
    	List<WordDTO> listWordDTOs = new ArrayList<>();
    	List<WordEntity> listWordsTop5 = new ArrayList<>();
    	listWordsTop5 = wordRepository.findTop5MostSearchedWords();
    	for(WordEntity w: listWordsTop5) {
    		WordDTO wordDTO = new WordDTO(w.getWord_id(), w.getWord());
    		listWordDTOs.add(wordDTO);
    	}
    	return listWordDTOs;
    }
    
    @Override
	public List<WordDTO> findTop5MostFavoritedWords(){
    	List<WordDTO> listWordDTOs = new ArrayList<>();
    	List<WordEntity> listWordFavorsTop5 = new ArrayList<>();
    	listWordFavorsTop5 = favoriteWordRepository.findTop5MostFavoritedWords();
    	for(WordEntity w: listWordFavorsTop5) {
    		WordDTO wordDTO = new WordDTO(w.getWord_id(), w.getWord());
    		listWordDTOs.add(wordDTO);
    	}
    	return listWordDTOs;
    }
    
}
