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

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="quiz")
public class QuizEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int quiz_id;
	
	@Column(columnDefinition = "nvarchar(255)", nullable = false)
	private String title;
	
	
	private String image;
	
	
	@Column(columnDefinition = "nvarchar(255)")
	private String description;
	
	@Column(nullable = false)
	private int total_question;
	
	@Column(nullable = false)
	private int time_limit;
	
	
	@OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<QuestionEntity> questions;
	
	@OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
	private List<TestEntity> tests;
}
