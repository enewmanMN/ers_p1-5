package com.revature.repositories;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.revature.models.User;
import com.revature.util.ConnectionFactory;
import com.revature.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.*;

public class UserRepository {

    private static final UserRepository userRepo = new UserRepository();
    public static UserRepository getInstance() {
        return userRepo;
    }

    private String baseQuery = "SELECT * FROM project_1.ers_users eu ";
    private String baseInsert = "INSERT INTO project_1.ers_users ";
    private String baseUpdate = "UPDATE project_1.ers_users eu ";

    public UserRepository(){
        super();
    }


    //---------------------------------- CREATE -------------------------------------------- //

    /**
     * A method tho add a new user to the database, hashes passwords before inserting
     * @param newUser the user to be added
     * @return returns true if one and only one row was inserted
     * @throws SQLException e
     */
    public boolean addUser(User newUser)  {

        try {
            hashPass(newUser);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        boolean result= false;

        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        session.save(newUser);
        session.getTransaction().commit();

        session.close();

        return false;
    }

    public String hashPass(String originalPassword) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, originalPassword.toCharArray());
        return bcryptHashString;
    }

    public void hashPass(User user) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String originalPassword = user.getPassword();
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, originalPassword.toCharArray());
        user.setPassword(bcryptHashString);
    }

    //---------------------------------- READ -------------------------------------------- //

    @SuppressWarnings("unchecked")
    public List<User> getAllusers() {
        List<User> users = null;
        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            tx = session.beginTransaction();
            users = session.createQuery("FROM User").list();

            tx.commit();
        } catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return users;

    }

    /**
     * A method to get a single User by email
     * @param email the email address to search the DB for
     * @return returns an Optional user
     * @throws SQLException e
     */
    public Optional<User> getAUserByEmail(String email) {

        List users = null;
        User user = null;

        Transaction tx = null;

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            tx = session.beginTransaction();

            String hql = "FROM User u WHERE u.email = :email";
            Query query = session.createQuery(hql);
            query.setParameter("email", email);
            users = query.list();


            System.out.println(users.toString());

            if(!users.isEmpty())
                user = (User) users.get(0);

        }catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        if(user != null) {
            return Optional.of(user);
        }
        else{
            return Optional.empty();
        }

    }

    public Optional<User> getAUserByUsername(String userName) {

        List users = null;
        User user = null;

        Transaction tx = null;

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            tx = session.beginTransaction();

            String hql = "FROM User u WHERE u.username = :username";
            Query query = session.createQuery(hql);
            query.setParameter("username", userName);
            users = query.list();


            System.out.println(users.toString());

            if(!users.isEmpty())
                user = (User) users.get(0);

        }catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        if(user != null) {
            return Optional.of(user);
        }
        else{
            return Optional.empty();

        }
    }

    /**
     * A method to get a single user by a given username and password

     * @param userName the users username
     * @param password the users password
     * @return returns an optional user
     * @throws SQLException e
     */

    public Optional<User> getAUserByUsernameAndPassword(String userName, String password) {

        if (password.charAt(0) != '$') {
            try {
                password = hashPass(password);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }

        List users = null;
        User user = null;

        Transaction tx = null;

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            tx = session.beginTransaction();


            String hql = "FROM User u WHERE u.username = :username AND u.password = :password";
            Query query = session.createQuery(hql);
            query.setParameter("username", userName);
            query.setParameter("password", password);
            users = query.list();


            System.out.println(users.toString());

            if(!users.isEmpty())
                user = (User) users.get(0);

        }catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();

        }

        if(user != null) {
            return Optional.of(user);
        }
        else{
            return Optional.empty();
        }

    }

    //---------------------------------- UPDATE -------------------------------------------- //

    public boolean updateAUser(User newUser) {


        boolean result= false;

        Transaction tx = null;

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {

            tx = session.beginTransaction();

            session.update(newUser);


            result = true;

            tx.commit();

        }catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return result;

    }

    //---------------------------------- DELETE -------------------------------------------- //

    /**
     * A method to delete a single User from the database
     * @param userId the ID of the record to be deleted
     * @return returns true if one and only one record is updated
     * @throws SQLException
     */
    public boolean deleteAUserById(Integer userId) {

        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean deleted = false;

        try {
            tx = session.beginTransaction();
            User user = (User) session.get(User.class, userId);
            session.delete(user);
            deleted = true;
            tx.commit();
        }catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }

        return deleted;
    }

    /**
     * A method to delete a single User from the database
     * @param username string of username you are trying to delete
     * @return returns true if one and only one record is updated
     * @throws SQLException
     */
    public boolean deleteByUsername(String username) {
        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String deleteSql = "DELETE FROM " +
                    "project_1.ers_users where username=?";

            PreparedStatement ps = conn.prepareStatement(deleteSql);
            ps.setString(1, username);
            //get the number of affected rows
            System.out.println("[INFO] UserRepository.deleteAUserById - prepared statement: " + ps.toString());
            int rowsInserted = ps.executeUpdate();
            return rowsInserted == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



}
