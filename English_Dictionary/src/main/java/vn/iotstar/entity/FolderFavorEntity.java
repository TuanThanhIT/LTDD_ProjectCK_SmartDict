package vn.iotstar.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "folder_favor")
public class FolderFavorEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int folder_id;
	private String folder_name;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;
	
	@OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteWordEntity> favoriteWords;
}
