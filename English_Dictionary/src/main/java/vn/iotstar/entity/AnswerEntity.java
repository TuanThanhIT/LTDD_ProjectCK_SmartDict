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
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "answer")
@NoArgsConstructor
@AllArgsConstructor
@Data
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
	
    @OneToMany(mappedBy = "selectedAnswer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAnswerEntity> userAnswers;
}
