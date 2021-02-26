package ServiceClassTests;

import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementStatus;
import com.revature.models.ReimbursementType;
import com.revature.models.User;
import com.revature.repositories.ReimbursementsRepository;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReimbursementTests {
    private ReimbursementsRepository repo;
    private Reimbursement reim;
    private Reimbursement reim2;
    private ReimbursementService reimbService;

    private User eric;
    private User eric2;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        //set up the user we can try to add to the db
        eric = new User();
        eric.setEmail("ericsreim@email");
        eric.setFirstname("Eric");
        eric.setLastname("Newman");
        eric.setUsername("enewman11");
        eric.setUserId(1);
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

        //initialize the userservice that actually does the act of registering and such
        userService = UserService.getInstance();
        userService.register(eric);
        userService.register(eric2);

        repo = new ReimbursementsRepository();

        reim = new Reimbursement();
        reim.setAmount(20.21);//
        reim.setReimbursementType(ReimbursementType.FOOD);//
        reim.setAuthorId(eric);//
        reim.setDescription("ericsreim");//
        reim.setResolved(new Timestamp(System.currentTimeMillis()));//
        //reim.setResolverId(eric2);
        reim.setReimbursementStatus(ReimbursementStatus.APPROVED);//
        reim.setSubmitted(new Timestamp(System.currentTimeMillis()));//
        reim.setReceipt(null);//

        reim2 = new Reimbursement();
        reim2.setAmount(20.22);//
        reim2.setReimbursementType(ReimbursementType.FOOD);//
        reim2.setAuthorId(eric2);//
        reim2.setDescription("ericsreim2");//
        reim2.setResolved(new Timestamp(System.currentTimeMillis()));//
        reim2.setResolverId(eric2);
        reim2.setReimbursementStatus(ReimbursementStatus.APPROVED);//
        reim2.setSubmitted(new Timestamp(System.currentTimeMillis()));//
        reim2.setReceipt(null);//

        reimbService = new ReimbursementService();
        repo = new ReimbursementsRepository();
    }

    @Test
    @DisplayName("Check authentication")
    public void saveAndAuthenticate() {
        reimbService.save(reim);
        Reimbursement testReim = reimbService.getReimbByUserId(eric.getUserId()).get(0);
        assertEquals(reim.getAuthorId(), testReim.getAuthorId(),
                "User not making it to the database or not coming back from getbyuserid");
    }

    @Test
    @DisplayName("Check by type")
    public void checkGetByType() {
        reimbService.save(reim);
        Reimbursement testReim = reimbService.getReimbByType(ReimbursementType.FOOD.ordinal()).get(0);
        assertEquals(reim.getReimbursementType(), testReim.getReimbursementType(),
                "User not making it to the database or not coming back from getbyuserid");
    }

    @Test
    @DisplayName("Check by status")
    public void checkGetByStatus() {
        reimbService.save(reim);
        Reimbursement testReim = reimbService.getReimbByStatus(reim.getReimbursementStatus().ordinal()).get(0);
        assertEquals(reim.getReimbursementStatus(), testReim.getReimbursementStatus(),
                "User not making it to the database or not coming back from getbyuserid");
    }

    @Test
    @DisplayName("Check valid reimbursement")
    public void checkReimbValid() {
        //just to keep the test clean
        reimbService.save(reim);
        assertTrue(reimbService.isReimbursementValid(reim),
                "User not making it to the database or not coming back from getbyuserid");
    }

    @Test
    @DisplayName("Check approval")
    public void checkApprove() {
        reimbService.save(reim);
        reimbService.approve(eric2.getUserId(), reim.getId());
//        assertEquals(reim.getReimbursementStatus(), testReim.getReimbursementStatus(),
//                "User not making it to the database or not coming back from getbyuserid");
        List<Reimbursement> queryRead = reimbService.getAllReimb();
        assertEquals(eric2.getUserId(), queryRead.get(0).getResolverId().getUserId(),
                "user id is not the resolver id expected");
        assertEquals(2, queryRead.get(0).getReimbursementStatus().ordinal(),
                "Status is not approved as expected");
    }

    @Test
    @DisplayName("Deny approval")
    public void checkDeny() {
        reimbService.save(reim);
        reimbService.deny(eric2.getUserId(), reim.getId());
//        assertEquals(reim.getReimbursementStatus(), testReim.getReimbursementStatus(),
//                "User not making it to the database or not coming back from getbyuserid");
        List<Reimbursement> queryRead = reimbService.getAllReimb();
        assertEquals(eric2.getUserId(), queryRead.get(0).getResolverId().getUserId(),
                "user id is not the resolver id expected");
        assertEquals(3, queryRead.get(0).getReimbursementStatus().ordinal(),
                "Status is not approved as expected");
    }

    @Test
    @DisplayName("Get all users")
    public void readAllReimbursements() {
        reimbService.save(reim);
        reimbService.save(reim2);


        List<Reimbursement> queryRead = reimbService.getAllReimb();

        assertEquals(2, queryRead.size(),
                "size in read all seems off");

        //individual cleanup for readalluserstest:
        try {
            System.out.println("Deleting reim 2 inside readall");
            repo.delete(reim2.getId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

//    public boolean isReimbursementValid(Reimbursement reimb){
//        if (reimb == null) return false;
//        if (reimb.getAmount() == null || reimb.getAmount() <= 0 ) return false;
//        if (reimb.getDescription() == null || reimb.getDescription().trim().equals("")) return false;
//        if (reimb.getAuthorId() != null ) return false;
//        if (reimb.getReimbursementType() == null ) return false;
//        return true;
//    }

//    @Test
//    @DisplayName("Check reimbursement valid")
//    public void checkUserIsValid() {
//        assertEquals(true, reimbService.isReimbursementValid(reim), "finding invalid values in reim fields");
//    }
//
//    @Test
//    @DisplayName("Check email avaliable")
//    public void checkEmailAvailable() {
//        userService.register(eric);
//        assertEquals(false, userService.isEmailAvailable(eric.getEmail()), "email is taken");
//    }
//
//    @Test
//    @DisplayName("Check username avaliable")
//    public void checkUsernameAvailable() {
//        userService.register(eric);
//        assertEquals(false, userService.isUsernameAvailable(eric.getUsername()), "Username is taken");
//    }

    @AfterEach
    public void tearDown() {
        try {
            repo.delete(reim.getId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        userService.deleteByUsername(eric.getUsername());
        userService.deleteByUsername(eric2.getUsername());

        try {
            List<User> queryRead = userService.getAllUsers();
            System.out.println("<-----------------LISTING READ USER QUERY RESULTS--------------------->");
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
