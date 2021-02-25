import com.revature.models.*;
import com.revature.repositories.ReimbursementsRepository;
import com.revature.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;

public class UserRepoTest {

    private User eric;
    private UserService userService;

    @BeforeEach
    public void setUp() {
//        set up the user we can try to add to the db
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

//    @Test
//    @DisplayName("Check create method")
//    public void createTest() {
//
//        userService.register(eric);
//
//
//    }

//    @Test
//    @DisplayName("Check list all user method")
//    public void listAllTest(){
//
//        List<User> user = userService.getAllUsers();
//        for(User u : user) {
//            System.out.println(user);
//        }
//    }

    @Test
    @DisplayName("Check a user by finding their email Test")
    public void getUserByEmailTest(){

        String email = "test@email";
        boolean user = userService.isEmailAvailable(email);

        System.out.println(user);

    }
}
