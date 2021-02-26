package ServiceClassTests;

import com.revature.models.User;
import com.revature.services.UserService;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserCreateTest {

        private User eric;
        private UserService userService;

        @BeforeEach
        public void setUp() {
                //set up the user we can try to add to the db
                eric = new User();
                eric.setEmail("erics@email");
                eric.setFirstname("Eric");
                eric.setLastname("Newman");
                eric.setUsername("enewman11");
                eric.setUserId(3);
                eric.setUserRole(1);
                eric.setPassword("Packers1");

                //initialize the userservice that actually does the act of registering and such
                userService = new UserService();
        }

        @Test
        @DisplayName("Check create method")
        public void createTest() {
                userService.register(eric);

                assertEquals(true, userService.isUserValid(eric),
                        "User Not Valid");
        }

        @Test
        @DisplayName("Check create method")
        public void checkUserIsValid() {
                userService.register(eric);

                assertEquals(true, userService.isUserValid(eric),
                        "User Not Valid");
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
