package ro.sapientia2015.story.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import org.junit.Test;

public class UserTest {

    private String NAME = "name";
    private String BIO = "bio";

    @Test
    public void buildWithMandatoryInformation() {
        User built = User.getBuilder(NAME).build();

        assertNull(built.getId());
        assertNull(built.getBio());
        assertEquals(NAME, built.getName());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void buildWithAllInformation() {
        User built = User.getBuilder(NAME)
                .bio(BIO)
                .build();

        assertNull(built.getId());
        assertEquals(BIO, built.getBio());
        assertEquals(NAME, built.getName());
        assertEquals(0L, built.getVersion());
    }
	
}
