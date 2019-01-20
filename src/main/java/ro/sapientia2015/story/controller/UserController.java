package ro.sapientia2015.story.controller;

import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.User;
import ro.sapientia2015.story.service.UserService;

@Controller
public class UserController {
	protected static final String REQUEST_MAPPING_LIST = "/users";
	protected static final String REQUEST_MAPPING_ADD = "/users/add";
	protected static final String REQUEST_MAPPING_VIEW = "/users/{id}";
	protected static final String REQUEST_MAPPING_DELETE = "/users/delete/{id}";
	protected static final String REQUEST_MAPPING_UPDATE = "/users/update/{id}";
	
    protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "feedback.message.user.added";
    protected static final String FEEDBACK_MESSAGE_KEY_UPDATED = "feedback.message.user.updated";
    protected static final String FEEDBACK_MESSAGE_KEY_DELETED = "feedback.message.user.deleted";

    protected static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage";
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";
	
	protected static final String MODEL_ATTRIBUTE = "user";
	
	protected static final String PARAMETER_ID = "id";

	protected static final String VIEW_ADD = "users/add";
	protected static final String VIEW_LIST = "users/list";
	protected static final String VIEW_VIEW = "users/view";
	protected static final String VIEW_UPDATE = "users/update";

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

	@RequestMapping(value = REQUEST_MAPPING_ADD, method = RequestMethod.POST)
	public String add(@Valid @ModelAttribute("user") UserDTO dto, BindingResult result,
			RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return VIEW_ADD;
		}

		User added = userService.add(dto);
		addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED, added.getName());
		attributes.addAttribute(PARAMETER_ID, added.getId());

		return createRedirectViewPath(REQUEST_MAPPING_VIEW);
	}

	@RequestMapping(value = REQUEST_MAPPING_ADD, method = RequestMethod.GET)
	public String showUsers(Model model) {

		UserDTO user = new UserDTO();
		model.addAttribute("user", user);
		return VIEW_ADD;
	}
	
    @RequestMapping(value = REQUEST_MAPPING_DELETE, method = RequestMethod.GET)
    public String deleteById(@PathVariable("id") Long id, RedirectAttributes attributes) throws NotFoundException {
        User deleted = userService.deleteById(id);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_DELETED, deleted.getName());
        return createRedirectViewPath(REQUEST_MAPPING_LIST);
    }
    
    @RequestMapping(value = REQUEST_MAPPING_UPDATE, method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
        User updated = userService.findById(id);
        UserDTO formObject = constructFormObjectForUpdateForm(updated);
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_UPDATE;
    }
    
    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute(MODEL_ATTRIBUTE) UserDTO dto, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
        if (result.hasErrors()) {
            return VIEW_UPDATE;
        }

        User updated = userService.update(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_UPDATED, updated.getName());
        attributes.addAttribute(PARAMETER_ID, updated.getId());

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }
	
	@RequestMapping(value = REQUEST_MAPPING_VIEW, method = RequestMethod.GET)
    public String findById(@PathVariable("id") Long id, Model model) throws NotFoundException {
        User found = userService.findById(id);
        model.addAttribute(MODEL_ATTRIBUTE, found);
        return VIEW_VIEW;
    }
	
    private String createRedirectViewPath(String requestMapping) {
        StringBuilder redirectViewPath = new StringBuilder();
        redirectViewPath.append("redirect:");
        redirectViewPath.append(requestMapping);
        return redirectViewPath.toString();
    }
    
    private UserDTO constructFormObjectForUpdateForm(User updated) {
    	UserDTO dto = new UserDTO();

        dto.setId(updated.getId());
        dto.setName(updated.getName());
        dto.setBio(updated.getBio());

        return dto;
    }
    
    private void addFeedbackMessage(RedirectAttributes attributes, String messageCode, Object... messageParameters) {
        String localizedFeedbackMessage = getMessage(messageCode, messageParameters);
        attributes.addFlashAttribute(FLASH_MESSAGE_KEY_FEEDBACK, localizedFeedbackMessage);
    }
    
    private String getMessage(String messageCode, Object... messageParameters) {
        Locale current = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageCode, messageParameters, current);
    }

}
