package vn.iotstar.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int user_id;
	
	@Column(columnDefinition = "nvarchar(255)", nullable = false)
	private String fullname;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	private String avatar;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<TestEntity> tests;
	
	@ManyToMany
	@JoinTable(
		name = "word_search",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "word_id"))
	@JsonManagedReference
	private List<WordEntity> words;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<FolderFavorEntity> folders;
	
	 @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	 private List<FavoriteWordEntity> favoriteWords;
}
