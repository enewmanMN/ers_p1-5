//import com.revature.dtos.RbDTO;
//import com.revature.models.Reimbursement;
//import com.revature.models.ReimbursementStatus;
//import com.revature.models.ReimbursementType;
//import com.revature.models.User;
//import com.revature.repositories.ReimbursementsRepository;
//import com.revature.services.ReimbursementService;
//import com.revature.services.UserService;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.sql.Timestamp;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class ReimbursementRepoTest {
//
//    private ReimbursementsRepository repo;
//    private Reimbursement reim;
//    private ReimbursementService reimbService;
//    private User eric;
//    private UserService userService;
//
//    @BeforeEach
//    public void setUp() {
////        set up the user we can try to add to the db
//        eric = new User();
//        eric.setEmail("erics@email");
//        eric.setFirstname("Eric");
//        eric.setLastname("Newman");
//        eric.setUsername("enewman");
//        eric.setUserId(1);
//        eric.setUserRole(1);
//        eric.setPassword("Packers1");
//
//        //initialize the userservice that actually does the act of registering and such
//        userService = UserService.getInstance();
//
//
//        repo = new ReimbursementsRepository();
//
//        reim = new Reimbursement();
//        reim.setAmount(20.21);//
//        reim.setReimbursementType(ReimbursementType.FOOD);//
//        reim.setAuthorId(eric);//
//        reim.setDescription("keflklkef");//
//        reim.setResolved(new Timestamp(System.currentTimeMillis()));//
//        reim.setResolverId(eric);
//        reim.setReimbursementStatus(ReimbursementStatus.APPROVED);//
//        reim.setSubmitted(new Timestamp(System.currentTimeMillis()));//
//        reim.setReceipt(null);//
//
//        reimbService = new ReimbursementService();
//
//    }
//
////    @Test
////    @DisplayName("Check create method")
////    public void createTest() {
////
////        repo.addReimbursement(reim);
////
////    }
//
////    @Test
////    @DisplayName("Check list all reimbursement method")
////    public void listAllReimbursementTest() {
////        List<Reimbursement> r = reimbService.getAllReimb();
////
////        for (Reimbursement u : r) {
////            System.out.println(u);
////
////        }
////    }
//
////    @Test
////    @DisplayName("Check list all reimbursement by author id method")
////    public void listAllReimbursementByAuthorIdTest(){
////
////        List<RbDTO> r = reimbService.getReimbByUserId(1);
////
////        for(RbDTO u : r) {
////            System.out.println(u);
////        }
////
////    }
//
//    @Test
//    @DisplayName("Check list all reimbursement by author id method")
//    public void listAllReimbursementByStatusIdTest(){
//
//    }
//
//    @Test
//    @DisplayName("Check list all reimbursement by author id method")
//    public void listAllReimbursementByStatusIdAndAuthorIdTest(){
//
//    }
//
//
//
////    @AfterEach
////    public void tearDown() {
////        userService.deleteUserById(eric.getUserId());
////
////        List<User> queryRead = userService.getAllUsers();
////        System.out.println("<-----------------LISTING READ QUERY RESULTS--------------------->");
////        for (User user : queryRead) {
////            System.out.println("<-----" + user.getUsername() + "------->");
////            System.out.println("firstname: " + user.getFirstname());
////            System.out.println("lastname: " + user.getLastname());
////            System.out.println("id: " + user.getUserId());
////            System.out.println("role: " + user.getUserRole());
////            System.out.println("email: " + user.getEmail());
////        }
////
////
////    }
//
//}
