package vn.iotstar.entity;



import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "word")
@Entity
public class WordEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long word_id;
	
	@Column(nullable = false, unique = true)
	private String word;
	
	private String phonetic;
	
	@OneToMany(mappedBy = "word", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PhoneticEntity> phonetics;
	
	@OneToMany(mappedBy = "word", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MeaningEntity> meanings;
	
}
