package ro.sapientia2015.story.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2015.story.model.User;

public class UserDTO {

	private Long id;

	@Length(max = User.MAX_LENGTH_BIO)
	private String bio;

	private User.Builder builder = new User.Builder();

	@NotEmpty
	@Length(max = User.MAX_LENGTH_NAME)
	private String name;

	public UserDTO() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public User.Builder getBuilder() {
		return builder;
	}

	public void setBuilder(User.Builder builder) {
		this.builder = builder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
