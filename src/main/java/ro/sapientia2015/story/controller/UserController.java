package ro.sapientia2015.story.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.model.User;
import ro.sapientia2015.story.service.UserService;

@Controller
public class UserController {
	 protected static final String REQUEST_MAPPING_LIST = "/users";
	 protected static final String REQUEST_MAPPING_VIEW = "/users/{id}";
	
    protected static final String VIEW_ADD = "users/add";
    protected static final String VIEW_LIST = "users/list";
    
    @Resource
    private UserService userService;

    @Resource
    private MessageSource messageSource;
    
    @RequestMapping(value = REQUEST_MAPPING_LIST, method = RequestMethod.GET)
    public String findAll(Model model) {
        List<User> models = userService.findAll();
        model.addAttribute("users", models);
        return VIEW_LIST;
    }

	@RequestMapping(value = "/users/add", method = RequestMethod.GET)
	public String showUsers(Model model) {

		UserDTO user = new UserDTO();
		model.addAttribute("user", user);
		return VIEW_ADD;
	}
	
}
