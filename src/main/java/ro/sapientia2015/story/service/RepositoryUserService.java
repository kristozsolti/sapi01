package ro.sapientia2015.story.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.User;
import ro.sapientia2015.story.repository.UserRepository;

public class RepositoryUserService implements UserService {

	@Resource
	private UserRepository userRepo;

	@Transactional(readOnly = true)
	@Override
	public List<User> findAll() {
		return userRepo.findAll();
	}

	@Transactional
	@Override
	public User add(UserDTO added) {
		User model = User.getBuilder(added.getName()).bio(added.getBio()).build();

		return userRepo.save(model);
	}

	@Transactional(readOnly = true, rollbackFor = { NotFoundException.class })
	@Override
	public User findById(Long id) throws NotFoundException {
		User found = userRepo.findOne(id);
		
		if (found == null) {
			throw new NotFoundException("No entry found with id: " + id);
		}

		return found;
	}

	@Transactional(rollbackFor = { NotFoundException.class })
	@Override
	public User deleteById(Long id) throws NotFoundException {
		User deleted = findById(id);
		userRepo.delete(deleted);

		return deleted;
	}

    @Transactional(rollbackFor = {NotFoundException.class})
	@Override
	public User update(UserDTO updated) throws NotFoundException {
    	User model = findById(updated.getId());
    	model.update(updated.getName(), updated.getBio());
    	
		return model;
	}

}
