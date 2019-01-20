package ro.sapientia2015.story.service;

import java.util.List;

import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.User;

public interface UserService {

	public List<User> findAll();
	
	public User add(UserDTO added);
	
	public User findById(Long id) throws NotFoundException;
	
	public User deleteById(Long id) throws NotFoundException;
	
	public User update(UserDTO updated) throws NotFoundException;
	
}
