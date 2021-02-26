package ServiceClassTests;

import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.User;
import com.revature.services.UserService;
import org.junit.jupiter.api.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserDeleteTest {
    private static User eric;
    private static UserService userService;

    @BeforeAll
    public static void setUp() {
        //set up the user we can try to add to the db
        eric = new User();
        eric.setEmail("ericsdelete@email");
        eric.setFirstname("Eric");
        eric.setLastname("Newman");
        eric.setUsername("enewman");
        eric.setUserId(1);
        eric.setUserRole(1);
        eric.setPassword("Packers1");

        //initialize the userservice that actually does the act of registering and such
        userService = UserService.getInstance();

    }

//    @Test
//    @DisplayName("Adding user to be removed")
//    public void addUserToDelete() {
//        userService.register(eric);
//
//        assertEquals(true, userService.isUserValid(eric),
//                "User created for delete setup was not valid");
//    }

//    @Test
//    @DisplayName("Checking soft delete by id method")
//    public void softDeleteByIdTest() {
//        userService.register(eric);
////        Optional<User> eric2 = userService.getAllUsers().stream().filter(u -> u.getUsername() == "enewman").findAny();
////        if (eric2.isPresent()) {
////            eric.setUserId(eric2.get().getUserId());
////        }
//
//        List<User> queryRead = userService.getAllUsers();
//        for (User user: queryRead) {
//            if (user.getUsername().equals(eric.getUsername())) {
//                eric.setUserId(user.getUserId());
//            }
//        }
//
//        //user id set to 1 during setup - if doing no userid insert can try to grab it with optional script above
//        userService.deleteUserById(eric.getUserId());
//
//        eric = userService.authenticate(eric.getUsername(), eric.getPassword());
//        assertEquals(4, eric.getUserRole(),
//                "User role was not changed to deleted");
//    }
//
    @Test
    @DisplayName("Checking full removal delete")
    public void hardDeleteTest() {
        userService.register(eric);
        userService.deleteByUsername(eric.getUsername());


        assertThrows(ResourceNotFoundException.class, () -> userService.authenticate(eric.getUsername(), eric.getPassword()));

    }


    @AfterAll
    public static void tearDown() {

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
