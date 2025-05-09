package vn.iotstar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import vn.iotstar.entity.FavoriteWordEntity;
import vn.iotstar.entity.FolderFavorEntity;
import vn.iotstar.entity.WordEntity;

@Repository
public interface FavoriteWordRepository extends JpaRepository<FavoriteWordEntity, Integer>{
	
	@Query("Select fw.word From FavoriteWordEntity fw Where fw.folder.folder_id = :folderId")
	List<WordEntity> findWordsByFolderId(@Param("folderId") int folderId);
	
	@Query("Select fw.folder From FavoriteWordEntity fw Where fw.word.word_id = :wordId")
	FolderFavorEntity getFolderByWord(@Param("wordId") Long wordId);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM FavoriteWordEntity f WHERE f.word.word_id = :wordId")
	void deleteByWordId(@Param("wordId") Long wordId);
	
	@Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM FavoriteWordEntity f WHERE f.user.user_id = :userId AND f.word.word_id = :wordId")
	boolean existsByUserIdAndWordId(int userId, Long wordId);
	
	 @Query("SELECT f FROM FolderFavorEntity f " +
	           "WHERE f.user.id = :userId " +
	           "AND f.folder_id NOT IN (" +
	           "    SELECT fw.folder.folder_id FROM FavoriteWordEntity fw " +
	           "    WHERE fw.user.user_id = :userId AND fw.word.word_id = :wordId" +
	           ")")
	 List<FolderFavorEntity> findOtherFoldersByUserIdAndWordId(@Param("userId") int userId, @Param("wordId") Long wordId);
	  
	 @Transactional
	 @Modifying
	 @Query("DELETE FROM FavoriteWordEntity f WHERE f.word.word_id = :wordId AND f.user.user_id = :userId")
	 void deleteByUserIdAndWordId(@Param("wordId") Long wordId, @Param("userId") int userId);
	 
	 @Transactional
	 @Modifying
	 @Query("DELETE FROM FavoriteWordEntity f where f.user.user_id = :userId AND f.word.word_id IN :wordIds")
	 void deleteWordsByUserIdAndWordId(@Param("userId") int userId, @Param("wordIds") List<Long> wordIds);
	
	 @Transactional
	 @Modifying
	 @Query("UPDATE FavoriteWordEntity f SET f.folder.folder_id = :folderId WHERE f.user.user_id = :userId AND f.word.word_id IN :wordIds")
	 void updateFolderForWords(@Param("folderId") int folderId, @Param("userId") int userId, @Param("wordIds") List<Long> wordIds);
	 
	 @Transactional
	 @Modifying
	 @Query("UPDATE FavoriteWordEntity f SET f.folder.folder_id = :folderId WHERE f.user.user_id = :userId AND f.word.word_id = :wordId")
	 void updateFolderForWord(@Param("folderId") int folderId, @Param("userId") int userId, @Param("wordId") Long wordId);
	 
	 @Query("SELECT f FROM FavoriteWordEntity f WHERE f.user.id = :userId AND f.word.id = :wordId")
	 FavoriteWordEntity findByUserIdAndWordId(@Param("userId") int userId, @Param("wordId") Long wordId);

	 @Query(value = """
			    SELECT w.*
			    FROM word w
			    JOIN favorite_word fw ON w.word_id = fw.word_id
			    GROUP BY w.word_id
			    ORDER BY COUNT(fw.word_id) DESC
			    LIMIT 5
			    """, nativeQuery = true)
	List<WordEntity> findTop5MostFavoritedWords();

}
