package vn.iotstar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.iotstar.entity.QuestionEntity;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer>{

}
