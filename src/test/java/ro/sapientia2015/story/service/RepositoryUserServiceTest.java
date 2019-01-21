package ro.sapientia2015.story.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.UserTestUtil;
import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.User;
import ro.sapientia2015.story.repository.UserRepository;

public class RepositoryUserServiceTest {

	private RepositoryUserService service;

	private UserRepository repositoryMock;

	@Before
	public void setUp() {
		service = new RepositoryUserService();

		repositoryMock = mock(UserRepository.class);
		ReflectionTestUtils.setField(service, "userRepo", repositoryMock);
	}
	
    @Test
    public void add() {
        UserDTO dto = UserTestUtil.createFormObject(null, UserTestUtil.BIO, UserTestUtil.NAME);

        service.add(dto);

        ArgumentCaptor<User> userArgument = ArgumentCaptor.forClass(User.class);
        verify(repositoryMock, times(1)).save(userArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        User model = userArgument.getValue();

        assertNull(model.getId());
        assertEquals(dto.getBio(), model.getBio());
        assertEquals(dto.getName(), model.getName());
    }
    
    @Test
    public void deleteById() throws NotFoundException {
        User model = UserTestUtil.createModel(UserTestUtil.ID, UserTestUtil.BIO, UserTestUtil.NAME);
        when(repositoryMock.findOne(UserTestUtil.ID)).thenReturn(model);

        User actual = service.deleteById(UserTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(UserTestUtil.ID);
        verify(repositoryMock, times(1)).delete(model);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(UserTestUtil.ID)).thenReturn(null);

        service.deleteById(UserTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(UserTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void findAll() {
        List<User> models = new ArrayList<User>();
        when(repositoryMock.findAll()).thenReturn(models);

        List<User> actual = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(models, actual);
    }

    @Test
    public void findById() throws NotFoundException {
        User model = UserTestUtil.createModel(UserTestUtil.ID, UserTestUtil.BIO, UserTestUtil.NAME);
        when(repositoryMock.findOne(UserTestUtil.ID)).thenReturn(model);

        User actual = service.findById(UserTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(UserTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }
    
   
    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(UserTestUtil.ID)).thenReturn(null);

        service.findById(UserTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(UserTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void update() throws NotFoundException {
        UserDTO dto = UserTestUtil.createFormObject(UserTestUtil.ID, UserTestUtil.BIO_UPDATED, UserTestUtil.NAME_UPDATED);
        User model = UserTestUtil.createModel(UserTestUtil.ID, UserTestUtil.BIO, UserTestUtil.NAME);
        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

        User actual = service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getBio(), actual.getBio());
        assertEquals(dto.getName(), actual.getName());
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
        UserDTO dto = UserTestUtil.createFormObject(UserTestUtil.ID, UserTestUtil.BIO_UPDATED, UserTestUtil.NAME_UPDATED);
        when(repositoryMock.findOne(dto.getId())).thenReturn(null);

        service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);
    }
    
}
