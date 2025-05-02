package vn.iotstar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.UserAnswerEntity;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswerEntity, Integer>{

}
