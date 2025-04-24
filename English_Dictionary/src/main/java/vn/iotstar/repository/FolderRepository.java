package vn.iotstar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.FolderFavorEntity;
import vn.iotstar.model.FolderDTO;

@Repository
public interface FolderRepository extends JpaRepository<FolderFavorEntity, Integer>{
	

	@Query("SELECT f FROM FolderFavorEntity f WHERE f.user.user_id = :userId AND f.folder_name = :name")
	Optional<FolderFavorEntity> findByUserIdAndName(@Param("userId") int userId, @Param("name") String name);
	
	@Query("SELECT new vn.iotstar.model.FolderDTO(f.folder_id, f.folder_name) " +
		       "FROM FolderFavorEntity f WHERE f.user.user_id = :userId")
	List<FolderDTO> findFoldersByUser(int userId);
	
}
