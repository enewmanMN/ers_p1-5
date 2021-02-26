package ServiceClassTests;

import com.revature.models.User;
import com.revature.repositories.UserRepository;
import com.revature.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserValidationTests {

    private User eric;
    private User eric2;
    private UserService userService;
    private UserRepository userRepo;
    @BeforeEach
    public void setUp() {
        //set up the user we can try to add to the db
        eric = new User();
        eric.setEmail("ericsvalid@email");
        eric.setFirstname("Eric");
        eric.setLastname("Newman");
        eric.setUsername("enewman11");
        eric.setUserId(3);
        eric.setUserRole(1);
        eric.setPassword("Packers1");

        eric2 = new User();
        eric2.setEmail("eric2@email");
        eric2.setFirstname("Eric2");
        eric2.setLastname("Newman2");
        eric2.setUsername("enewman12");
        eric2.setUserId(2);
        eric2.setUserRole(2);
        eric2.setPassword("Packers2");

        userRepo = new UserRepository();
        //initialize the userservice that actually does the act of registering and such
        userService = UserService.getInstance();
    }

    @Test
    @DisplayName("Check create method")
    public void createAndAuthenticate() {
        userService.register(eric);

        assertEquals(eric, userService.authenticate(eric.getUsername(), eric.getPassword()),
                "User not making it to the database or not coming back from authenticate method");
    }

    @Test
    @DisplayName("Get all users")
    public void readAllUsers() {
        userService.register(eric);
        userService.register(eric2);

        List<User> queryRead = userService.getAllUsers();

        assertEquals(2, queryRead.size(),
                "size in read all seems off");

        //individual cleanup for readalluserstest:
        userService.deleteByUsername(eric2.getUsername());
    }

    @Test
    @DisplayName("Check user valid")
    public void checkUserIsValid() {

        assertEquals(true, userService.isUserValid(eric), "finding invalid values in user fields");

    }

    @Test
    @DisplayName("Check email avaliable")
    public void checkEmailAvailable() {
        userService.register(eric);
        assertEquals(false, userService.isEmailAvailable(eric.getEmail()), "email is taken");
    }

    @Test
    @DisplayName("Check username avaliable")
    public void checkUsernameAvailable() {
        userService.register(eric);
        assertEquals(false, userService.isUsernameAvailable(eric.getUsername()), "Username is taken");
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
