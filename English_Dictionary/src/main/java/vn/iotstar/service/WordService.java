package vn.iotstar.service;

import java.util.List;
import java.util.Optional;

import vn.iotstar.entity.WordEntity;

public interface WordService {
	WordEntity addWord(String word);

	List<WordEntity> findAll();

	Optional<WordEntity> findById(Long id);
}
