package ServiceClassTests;

import com.revature.models.User;
import com.revature.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserSelectTest {
    private static User eric;
    private static UserService userService;

    @BeforeAll
    public static void setUp() {
        //set up the user we can try to add to the db
        eric = new User();
        eric.setEmail("ericselect@email");
        eric.setFirstname("Eric");
        eric.setLastname("Newman");
        eric.setUsername("enewman");
        eric.setUserId(1);
        eric.setUserRole(1);
        eric.setPassword("Packers1");

        //initialize the userservice that actually does the act of registering and such
        userService = UserService.getInstance();

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

    @AfterEach
    public void tearDown() {
        userService.deleteByUsername(eric.getUsername());

        try {
            List<User> queryRead = userService.getAllUsers();
            System.out.println("<-----------------LISTING READ QUERY RESULTS--------------------->");
            for (User user : queryRead) {
                System.out.println("<-----" + user.getUsername() + "------->");
                System.out.println("firstname: " + user.getFirstname());
                System.out.println("lastname: " + user.getLastname());
                System.out.println("id: " + user.getUserId());
                System.out.println("role: " + user.getUserRole());
                System.out.println("email: " + user.getEmail());
            }
        } catch (RuntimeException e) {
            System.out.println("User removed during tear down as expected :)");
        }


    }
}
