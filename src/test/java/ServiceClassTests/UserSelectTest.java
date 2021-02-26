package ServiceClassTests;

import com.revature.models.User;
import com.revature.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserSelectTest {
    private static User eric;
    private static UserService userService;

    @BeforeAll
    public static void setUp() {
        //set up the user we can try to add to the db
        eric = new User();
        eric.setEmail("erics@email");
        eric.setFirstname("Eric");
        eric.setLastname("Newman");
        eric.setUsername("enewman");
        eric.setUserId(1);
        eric.setUserRole(1);
        eric.setPassword("Packers1");

        //initialize the userservice that actually does the act of registering and such
        userService = new UserService();

    }

    @Test
    @DisplayName("Adding user to be check")
    public void addUserToCheck() {
        userService.register(eric);

        assertEquals(true, userService.isUserValid(eric),
                "User created for delete setup was not valid");
    }

    @Test
    @DisplayName("Check a user by finding their username Test")
    public void getUserByUsernameTest(){

        userService.isUsernameAvailable(eric.getUsername());

            assertTrue(userService.isUserValid(eric),
                    "User still there after username");

    }

    @Test
    @DisplayName("Check a user by finding their email Test")
    public void getUserByEmailTest(){

        userService.isEmailAvailable(eric.getEmail());

        assertTrue(userService.isUserValid(eric),
                "User still there after email");

    }
}
