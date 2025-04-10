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

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "answer")
public class AnswerEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int answer_id;
	
	@Column(nullable = false)
	private String answer_text;
	
	@Column(nullable = false)
	private boolean is_correct;
	
	@ManyToOne
	@JoinColumn(name = "question_id", nullable = false)
	private QuestionEntity question;
}
