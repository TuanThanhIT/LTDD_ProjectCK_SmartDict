package vn.iotstar.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.iotstar.entity.DefinitionEntity;
import vn.iotstar.entity.MeaningEntity;
import vn.iotstar.entity.PhoneticEntity;
import vn.iotstar.entity.WordEntity;
import vn.iotstar.model.DefinitionDTO;
import vn.iotstar.model.MeaningDTO;
import vn.iotstar.model.PhoneticDTO;
import vn.iotstar.model.WordDTO;
import vn.iotstar.repository.DefinitionRepository;
import vn.iotstar.repository.MeaningRepository;
import vn.iotstar.repository.PhoneticRepository;

@Component
public class WordMapper {

    @Autowired
    private MeaningRepository meaningRepository;

    @Autowired
    private DefinitionRepository definitionRepository;

    @Autowired
    private PhoneticRepository phoneticRepository;

    public WordDTO convertToDTO(WordEntity wordEntity) {
        WordDTO wordDTO = new WordDTO();
        wordDTO.setWord_id(wordEntity.getWord_id());
        wordDTO.setWord(wordEntity.getWord());

        // Meanings
        List<MeaningEntity> listMeaning = meaningRepository.findMeaningsByWordId(wordEntity.getWord_id());
        List<MeaningDTO> listMeaningDTO = new ArrayList<>();
        for (MeaningEntity meaningEntity : listMeaning) {
            MeaningDTO meaningDTO = new MeaningDTO();
            meaningDTO.setMeaning_id(meaningEntity.getMeaning_id());
            meaningDTO.setVietNamese(meaningEntity.getVietnameseMeaning());
            meaningDTO.setPartOfSpeech(meaningEntity.getPartOfSpeech());

            // Definitions
            List<DefinitionEntity> listDefinitions = definitionRepository.findDefinitionsByMeaningId(meaningEntity.getMeaning_id());
            List<DefinitionDTO> listDefinitionDTO = new ArrayList<>();
            for (DefinitionEntity definitionEntity : listDefinitions) {
                DefinitionDTO definitionDTO = new DefinitionDTO();
                definitionDTO.setDefinition_id(definitionEntity.getDefiniton_id());
                definitionDTO.setDefinition(definitionEntity.getDefinition());
                definitionDTO.setExample(definitionEntity.getExample());
                listDefinitionDTO.add(definitionDTO);
            }

            meaningDTO.setDefinitions(listDefinitionDTO);
            listMeaningDTO.add(meaningDTO);
        }
        wordDTO.setMeanings(listMeaningDTO);

        // Phonetics
        List<PhoneticEntity> listPhonetic = phoneticRepository.findPhoneticsByWordId(wordEntity.getWord_id());
        List<PhoneticDTO> listPhoneticDTO = new ArrayList<>();
        for (PhoneticEntity phoneticEntity : listPhonetic) {
            PhoneticDTO phoneticDTO = new PhoneticDTO();
            phoneticDTO.setPhonetic_id(phoneticEntity.getPhonetic_id());
            phoneticDTO.setText(phoneticEntity.getText());
            phoneticDTO.setAudio(phoneticEntity.getAudio()); // Sửa dòng này (bạn đang set lại chính giá trị null)
            listPhoneticDTO.add(phoneticDTO);
        }
        wordDTO.setPhonetics(listPhoneticDTO);

        return wordDTO;
    }
}
