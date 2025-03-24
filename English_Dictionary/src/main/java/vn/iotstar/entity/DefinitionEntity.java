package vn.iotstar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "definition")
@Entity
public class DefinitionEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long definiton_id;
	
	@Column(columnDefinition = "TEXT")
	private String definition;
	
	
	@Column(columnDefinition = "TEXT")
	private String example;
	
	@ManyToOne
	@JoinColumn(name = "meaning_id")
	private MeaningEntity meaning;

}
