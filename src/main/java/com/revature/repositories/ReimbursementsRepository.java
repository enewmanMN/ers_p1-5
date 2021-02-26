package com.revature.repositories;

import com.revature.dtos.RbDTO;
import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementStatus;
import com.revature.models.ReimbursementType;
import com.revature.util.ConnectionFactory;
import com.revature.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A class to interact with the database to CRUD reimbursement objects
 */
public class ReimbursementsRepository {
    //base query that combines the name and resolver names from one query
    private String baseQuery = "SELECT er.id, er.amount, er.description, er.reimbursement_status_id, \n" +
            "er.reimbursement_type_id, er.resolved, er.submitted,  er.author_id , er.resolver_id,\n" +
            "author.first_name as author_first_name , author.last_name as author_last_name , \n" +
            "resolver.first_name as resolver_first_name, resolver.last_name as resolver_last_name\n" +
            "FROM project_1.ers_reimbursements er\n" +
            "left join project_1.ers_users author \n" +
            "on er.author_id = author.id\n" +
            "left join project_1.ers_users resolver \n" +
            "on er.resolver_id = resolver.id ";
    private String baseInsert = "INSERT INTO project_1.ers_reimbursements ";
    private String baseUpdate = "UPDATE project_1.ers_reimbursements er ";

    public ReimbursementsRepository(){
        super();
    }

    //---------------------------------- CREATE -------------------------------------------- //
    /**
     * Adds a reimburement to the database, Does not handle Images!
     * @param reimbursement the reimbursement to be added to the DB
     * @throws SQLException e
     * @throws IOException e
     */
    // TODO add support to persist receipt images to data source
    public boolean addReimbursement(Reimbursement reimbursement) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx= null;
        try {
            tx = session.beginTransaction();

            session.save(reimbursement);

            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            throw e;
        }
        finally {
            session.close();
        }

        return false;
    }

    //---------------------------------- READ -------------------------------------------- //

    public List<Reimbursement> getAllReimbursements() {

        List reimb = null;
        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            tx = session.beginTransaction();
            reimb = session.createQuery("FROM Reimbursement").list();

            for(Iterator iterator = reimb.iterator(); iterator.hasNext();) {
                Reimbursement getReimb = (Reimbursement) iterator.next();
            }
            tx.commit();
        } catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return reimb;

//        List<RbDTO> reimbursements = new ArrayList<>();
//        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
//            String sql = baseQuery + " order by er.id";
//            PreparedStatement ps = conn.prepareStatement(sql);
//
//            ResultSet rs = ps.executeQuery();
//
//            reimbursements = mapResultSetDTO(rs);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return reimbursements;
    }

    public List<Reimbursement> getAllReimbSetByStatus(Integer statusId) {
//        List<RbDTO> reimbursements = new ArrayList<>();
//        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
//            String sql = baseQuery + "WHERE er.reimbursement_status_id=? order by er.id";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setInt(1,statusId);
//            ResultSet rs = ps.executeQuery();
//            reimbursements = mapResultSetDTO(rs);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return reimbursements;

        List<Reimbursement> reimbStatus = null;
        Transaction tx = null;

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            tx = session.beginTransaction();

            String hql = "FROM Reimbursement r WHERE r.reimbursementStatus = :statusId";
            Query query = session.createQuery(hql);
            query.setParameter("statusId", ReimbursementStatus.APPROVED);
            reimbStatus = query.list();


            System.out.println(reimbStatus.toString());


        }catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return reimbStatus;
    }

    /**
     * A method to get Reimbursements by the id of the reimbursement itself
     * @param reimbId The ID of the reimbursement in the database that is requested
     * @return returns an Option Reimbursement object
     * @throws SQLException e
     */
    @SuppressWarnings("unchecked")
    public Optional<Reimbursement> getAReimbByReimbId(Integer reimbId) throws SQLException {

        List<Reimbursement> reimbursements = null;
        Reimbursement reimbursement = null;

        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try{
            tx = session.beginTransaction();
            String hql = "FROM Reimbursement r where r.id = :id";
            reimbursements = session.createQuery(hql)
                    .setParameter("id", reimbId)
                    .list();

            reimbursement = reimbursements.get(0);
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        if(reimbursement != null) {
            return Optional.of(reimbursement);
        }
        else{
            return Optional.empty();
        }
    }

    /**
     * A method to get all of the records for an author given their id
     * @param authorId the ID of the author of the reimbursement
     * @return a set of reimbursements mapped by the MapResultSet method
     * @throws SQLException e
     */
    public List<Reimbursement> getAllReimbSetByAuthorId(Integer authorId){

        List reimb = null;
        Transaction tx = null;

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            tx = session.beginTransaction();

            String hql = "FROM Reimbursement r WHERE r.authorId = :authorId";
            Query query = session.createQuery(hql);
            query.setParameter("authorId", authorId);
            reimb = query.list();


            System.out.println(reimb.toString());

        }catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return reimb;
    }

    /**
     * A method to get all of the records for an author given their id and filter by status
     * @param authorId the ID of the author of the reimbursement
     * @param reStat the status that the reimbursement is to be set to
     * @return a set of reimbursements mapped by the MapResultSet method
     * @throws SQLException e
     */
    @SuppressWarnings("unchecked")
    public List<Reimbursement> getAllReimbSetByAuthorIdAndStatus(Integer authorId, ReimbursementStatus reStat) throws SQLException {

        List<Reimbursement> reimbursements = null;

        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try{
            tx = session.beginTransaction();
            String hql = "FROM Reimbursement r where r.authorId = :authorId" +
                         " and r.reimbursementStatus = :reimbursement_status_id";
            reimbursements = session.createQuery(hql)
                             .setParameter("authorId", authorId)
                             .setParameter("reimbursement_status_id", reStat)
                             .list();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return reimbursements;
    }

    /**
     * A method to get all of the records for an author given their id and filter by type
     * @param authorId ID of the Author User
     * @param reType the Type to update the record to
     * @return a set of reimbursements mapped by the MapResultSet method
     * @throws SQLException e
     */
    @SuppressWarnings("unchecked")
    public List<Reimbursement> getAllReimbSetByAuthorIdAndType(Integer authorId, ReimbursementType reType) throws SQLException {

        List<Reimbursement> reimbursements = null;

        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try{
            tx = session.beginTransaction();
            String hql = "FROM Reimbursement r where r.authorId = :author_id" +
                    " and r.reimbursementType = :reimbursement_type_id";
            reimbursements = session.createQuery(hql)
                    .setParameter("author_id", authorId)
                    .setParameter("reimbursement_type_id", reType)
                    .list();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return reimbursements;
    }

    @SuppressWarnings("unchecked")
    public List<Reimbursement> getAllReimbSetByType(Integer typeId)  {

        List<Reimbursement> reimbursements = null;

        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try{
            tx = session.beginTransaction();
            String hql = "FROM Reimbursement r where r.reimbursementType = :reimbursement_type_id";
            reimbursements = session.createQuery(hql)
                    .setParameter("reimbursement_type_id", typeId)
                    .list();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return reimbursements;
    }

    /**
     * A method to get all of the records for a resolver given their id
     * @param resolverId ID of the Resolver User
     * @return a set of reimbursements mapped by the MapResultSet method
     * @throws SQLException e
     */
    @SuppressWarnings("unchecked")
    public List<Reimbursement> getAllReimbSetByResolverId(Integer resolverId) throws SQLException {

        List<Reimbursement> reimbursements = null;

        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try{
            tx = session.beginTransaction();
            String hql = "FROM Reimbursement r where r.resolverId = :resolver_id";
            reimbursements = session.createQuery(hql)
                    .setParameter("resolver_id", resolverId)
                    .list();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return reimbursements;
    }

    /**
     * A method to get all of the records for a resolver given their id and filter by status
     * @param resolverId  ID of the Resolver User
     * @param reStat the status to update the record to
     * @return a set of reimbursements mapped by the MapResultSet method
     * @throws SQLException e
     */
    @SuppressWarnings("unchecked")
    public List<Reimbursement> getAllReimbSetByResolverIdAndStatus(Integer resolverId, ReimbursementStatus reStat) throws SQLException {
//        List<RbDTO> reimbursements = new ArrayList<>();
//        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
//            String sql = baseQuery + "WHERE er.resolver_id=? AND er.reimbursement_status_id=? order by er.id";
//            PreparedStatement ps = conn.prepareStatement(sql);
//
//            ps.setInt(1,resolverId);
//            ps.setInt(2,reStat.ordinal() + 1);
//            ResultSet rs = ps.executeQuery();
//            reimbursements = mapResultSetDTO(rs);
//        }
//        return reimbursements;

        List<Reimbursement> reimbursements = null;

        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try{
            tx = session.beginTransaction();
            String hql = "FROM Reimbursement r where r.resolverId = :resolver_id"
                        +" and r.reimbursementStatus = :reimbursement_status_id";
            reimbursements = session.createQuery(hql)
                    .setParameter("resolver_id", resolverId)
                    .setParameter("reimbursement_status_id", reStat)
                    .list();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return reimbursements;
    }

    /**
     * A  method to get all of the records for a resolver given their id and filter by type
     * @param resolverId ID of the Resolver User
     * @param reType type of Reimbursements to select by
     * @return a set of reimbursements mapped by the MapResultSet method
     * @throws SQLException e
     */
    @SuppressWarnings("unchecked")
    public List<Reimbursement> getAllReimbSetByResolverIdAndType(Integer resolverId, ReimbursementType reType) throws SQLException {
//        List<RbDTO> reimbursements = new ArrayList<>();
//        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
//            String sql = baseQuery + "WHERE er.resolver_id=? AND er.reimbursement_type_id=? order by er.id";
//            PreparedStatement ps = conn.prepareStatement(sql);
//
//            ps.setInt(1,resolverId);
//            ps.setInt(2,reType.ordinal() + 1);
//            ResultSet rs = ps.executeQuery();
//            reimbursements = mapResultSetDTO(rs);
//        }
//        return reimbursements;
        List<Reimbursement> reimbursements = null;

        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try{
            tx = session.beginTransaction();
            String hql = "FROM Reimbursement r where r.resolverId = :resolver_id"
                        +" and r.reimbrusementType = :reimbursement_type_id";
            reimbursements = session.createQuery(hql)
                    .setParameter("reimbursement_type_id", reType)
                    .list();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return reimbursements;
    }

    //---------------------------------- UPDATE -------------------------------------------- //
    public boolean updateEMP(Reimbursement reimb) {
        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = baseUpdate +
                    "SET amount=?, description=?, reimbursement_type_id=?\n" +
                    "WHERE id=?\n";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, reimb.getAmount());
            ps.setString(2, reimb.getDescription());
            ps.setInt(3,reimb.getReimbursementType().ordinal() + 1);
            ps.setInt(4,reimb.getId());
            //get the number of affected rows
            int rowsInserted = ps.executeUpdate();
            return rowsInserted != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
//
//        Transaction tx = null;
//        Session session = HibernateUtil.getSessionFactory().openSession();
//
//        boolean success = false;

//        try {
//            tx = session.beginTransaction();
//            Reimbursement reimb =
//        }

    }

    public boolean updateFIN(Integer resolverId, Integer statusId, Integer reimbId) {
        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = baseUpdate +
                    "SET resolver_id=?, reimbursement_status_id=?, resolved=CURRENT_TIMESTAMP\n" +
                    "WHERE id=?\n";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, resolverId);
            ps.setInt(2, statusId);
            ps.setInt(3,reimbId);

            //get the number of affected rows
            int rowsInserted = ps.executeUpdate();
            return rowsInserted != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * A method to update only the resolved timestamp by the id of the reimbursement
     * @param reimbId The ID of the reimbursement in the database that is requested
     * @param timestamp an SQL timestamp object to set the time resolved to
     * @return returns true if one and only one record was updated
     * @throws SQLException e
     */
    public boolean updateResolvedTimeStampByReimbId(Integer reimbId, Timestamp timestamp) throws SQLException {

        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = baseUpdate +
                         "SET resolved=?\n" +
                         "WHERE id=?\n";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setTimestamp(1,timestamp);
            ps.setInt(2,reimbId);
            //get the number of affected rows
            int rowsInserted = ps.executeUpdate();
            return rowsInserted != 0;
        }
    }

    /**
     * A method to update only the resolver ID by the id of the reimbursement
     * @param reimbId The ID of the reimbursement in the database that is requested
     * @param resolverId the ID of the user that resolves the record to update the record to
     * @return returns true if one and only one record was updated
     * @throws SQLException e
     */
    public boolean updateResolverIdByReimbId(Integer reimbId, Integer resolverId) throws SQLException {

        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = baseUpdate +
                    "SET resolver_id=?\n" +
                    "WHERE id=?\n";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,resolverId);
            ps.setInt(2,reimbId);
            //get the number of affected rows
            int rowsInserted = ps.executeUpdate();
            return rowsInserted != 0;
        }

//        Session session = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//
//        boolean success = false;
//
//        try{
//            tx = session.beginTransaction();
//            Reimbursement reimb = getAReimbByReimbId(reimbId).get();
//            reimb.setResolverId(resolverId);
//            session.update();
//        }
    }

    /**
     * A method to update only the Reimb. TYPE by the id of the Reimbursement
     * @param reimbId The ID of the reimbursement in the database that is requested
     * @param reimbursementType the type to update the record to
     * @return returns true if one and only one record was updated
     * @throws SQLException e
     */
    public boolean updateReimbursementTypeByReimbId(Integer reimbId, ReimbursementType reimbursementType) throws SQLException {
        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = baseUpdate +
                    "SET reimbursement_type_id=? " +
                    "WHERE er.id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,reimbursementType.ordinal() + 1);
            ps.setInt(2,reimbId);
            //get the number of affected rows
            int rowsInserted = ps.executeUpdate();
            return rowsInserted != 0;
        }
    }

    /**
     * A method to update the status of a reimbursement in the database
     * @param reimbId The ID of the reimbursement in the database that is requested
     * @param newReimbStatus the status to update the record to
     * @return returns true if one and only one record was updated
     * @throws SQLException e
     */
    public boolean updateReimbursementStatusByReimbId(Integer reimbId, ReimbursementStatus newReimbStatus) throws SQLException {
        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = baseUpdate +
                         "SET reimbursement_status_id=? " +
                         "WHERE er.id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,newReimbStatus.ordinal() + 1);
            ps.setInt(2,reimbId);
            //get the number of affected rows
            int rowsInserted = ps.executeUpdate();
            return rowsInserted != 0;
        }
    }


    //---------------------------------- DELETE -------------------------------------------- //

    /**
     * A method to delete a single Reimbursement from the database
     * @param reimbId the ID of the record to be deleted
     * @return returns true if one and only one record is updated
     * @throws SQLException e
     */
    public boolean delete(Integer reimbId) throws SQLException {
        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = "DELETE FROM project_1.ers_reimbursements\n" +
                         "WHERE id=? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,reimbId);
            //get the number of affected rows
            int rowsInserted = ps.executeUpdate();
            return rowsInserted != 0;
        }


    }

    //---------------------------------- UTIL -------------------------------------------- //
    /**
     * A method to map the result sets from the reimbursement queries
     * @param rs a resultset
     * @return a set of reimbursements
     * @throws SQLException e
     */
    private Set<Reimbursement> mapResultSet(ResultSet rs) throws SQLException {
        Set<Reimbursement> reimbursements = new HashSet<>();
        while (rs.next()){
            Reimbursement temp = new Reimbursement();
            temp.setId(rs.getInt("id"));
            temp.setAmount(rs.getDouble("amount"));
            temp.setSubmitted(rs.getTimestamp("submitted"));
            temp.setResolved(rs.getTimestamp("resolved"));
            temp.setDescription(rs.getString("description"));
//            temp.setAuthorId(rs.getInt("author_id"));
//            temp.setResolverId(rs.getInt("resolver_id"));
            temp.setReimbursementStatus(ReimbursementStatus.getByNumber(rs.getInt("reimbursement_status_id")));
            temp.setReimbursementType(ReimbursementType.getByNumber(rs.getInt("reimbursement_type_id")));

            reimbursements.add(temp);
        }
        return reimbursements;
    }

    private List<RbDTO> mapResultSetDTO(ResultSet rs) throws SQLException {
        List<RbDTO> reimbs = new ArrayList<>();
        while (rs.next()){
            RbDTO temp = new RbDTO();
            temp.setId(rs.getInt("id"));
            temp.setAmount(rs.getDouble("amount"));
            temp.setSubmitted(rs.getTimestamp("submitted").toString().substring(0,19));
            temp.setDescription(rs.getString("description"));
            temp.setAuthorName(rs.getString("author_first_name") + " " + rs.getString("author_last_name"));
            temp.setStatus(ReimbursementStatus.getByNumber(rs.getInt("reimbursement_status_id")).toString());
            temp.setType(ReimbursementType.getByNumber(rs.getInt("reimbursement_type_id")).toString());
            try {
                temp.setResolved(rs.getTimestamp("resolved").toString().substring(0,19));
                temp.setResolverName(rs.getString("resolver_first_name") + " " + rs.getString("resolver_last_name"));
            } catch (NullPointerException e){
                //If Reimb. has not been resolved DB will return null for these values:
                temp.setResolved("");
                temp.setResolverName("");
            }

            reimbs.add(temp);
        }
        System.out.println(reimbs);
        return reimbs;
    }
}
