package vn.iotstar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_answer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_answer_id;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private TestEntity test;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionEntity question;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private AnswerEntity selectedAnswer;
}
