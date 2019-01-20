package ro.sapientia2015.story.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "user")
public class User {

	public static final int MAX_LENGTH_BIO = 500;
	public static final int MAX_LENGTH_NAME = 50;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "name", nullable = false, length = MAX_LENGTH_NAME)
	private String name;

	@Column(name = "bio", nullable = true, length = MAX_LENGTH_BIO)
	private String bio;

	@Version
	private long version;
	
	public User() {

    }

    public static Builder getBuilder(String name) {
        return new Builder(name);
    }

	public void update(String name, String bio) {
		this.name = name;
		this.bio = bio;
	}
    
	public static class Builder {

        private User built;

        public Builder() {
            built = new User();
        }
        
        public Builder setName(String name)
        {
        	this.built.name=name;
        	return this;
        }
        
        public Builder(String name) {
            built = new User();
            built.name = name;
        }

        public User build() {
            return built;
        }

        public Builder bio(String bio) {
            built.bio = bio;
            return this;
        }
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
