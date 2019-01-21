package ro.sapientia2015.story;

import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.model.User;

public class UserTestUtil {

    public static final Long ID = 1L;
    public static final String BIO = "bio";
    public static final String BIO_UPDATED = "updatedBiography";
    public static final String NAME = "name";
    public static final String NAME_UPDATED = "updatedName";

    private static final String CHARACTER = "a";

    public static UserDTO createFormObject(Long id, String bio, String name) {
    	UserDTO dto = new UserDTO();

        dto.setId(id);
        dto.setBio(bio);
        dto.setName(name);

        return dto;
    }

    public static User createModel(Long id, String bio, String name) {
    	User model = User.getBuilder(name)
                .bio(bio)
                .build();

        ReflectionTestUtils.setField(model, "id", id);

        return model;
    }

    public static String createRedirectViewPath(String path) {
        StringBuilder redirectViewPath = new StringBuilder();
        redirectViewPath.append("redirect:");
        redirectViewPath.append(path);
        return redirectViewPath.toString();
    }

    public static String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();

        for (int index = 0; index < length; index++) {
            builder.append(CHARACTER);
        }

        return builder.toString();
    }
}
