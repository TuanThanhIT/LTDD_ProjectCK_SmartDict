package vn.iotstar.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Table(name = "phonetic")
@Entity
public class PhoneticEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long phonetic_id;
	
	private String text;
	
	private String audio;
	
	@ManyToOne
	@JoinColumn(name = "word_id", nullable = false)
	@JsonBackReference
	private WordEntity word;
	

}
