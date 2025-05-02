package vn.iotstar.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meaning")
@Entity
public class MeaningEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long meaning_id;
	
	private String partOfSpeech; // 
	
	
	@Column(columnDefinition = "nvarchar(255)")
	private String vietnameseMeaning;
	
	@ManyToOne
	@JoinColumn(name = "word_id", nullable = false)
	private WordEntity word;
	
	@OneToMany(mappedBy = "meaning", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DefinitionEntity> definitions;

}
