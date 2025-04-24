package vn.iotstar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.UserEntity;
import vn.iotstar.entity.WordEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>{
	
	
	@Query("Select u.words From UserEntity u Where u.user_id = :userId")
	List<WordEntity> findWordSearchByUser(@Param("userId") int userId);
	
}
