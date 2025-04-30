package vn.iotstar.service;

import java.util.List;
import java.util.Optional;

import vn.iotstar.entity.WordEntity;
import vn.iotstar.model.WordDTO;

public interface WordService {
	WordEntity addWord(String word);

	Optional<WordEntity> findById(Long id);

	List<WordDTO> findWordAll();
}
