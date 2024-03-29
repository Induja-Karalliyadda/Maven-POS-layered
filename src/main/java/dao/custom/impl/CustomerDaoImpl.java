package dao.custom.impl;

import dao.util.CrudUtil;
import db.DBConnection;
import dto.CustomerDto;
import dao.custom.CustomerDao;
import entity.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.security.auth.login.AppConfigurationEntry;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {


    @Override
    public boolean updateCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public List<CustomerDto> allCustomers() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public CustomerDto searchCustomer(String id) {
        return null;
    }

    @Override
    public boolean save(Customer entity) throws SQLException, ClassNotFoundException {
        String sql = "insert into customer value(?,?,?,?)";
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        pstm.setString(1,entity.getId());
//        pstm.setString(2,entity.getName());
//        pstm.setString(3,entity.getAddress());
//        pstm.setDouble(4,entity.getSalary());
//
//        int result = pstm.executeUpdate();
//        if(result>0){
//            return true;
//        }else {
//            return false;
//        }
        Configuration configuration=new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Customer.class);
      SessionFactory sessionFactory =configuration.buildSessionFactory();
      Session session =sessionFactory.openSession();
      Transaction transaction =session.beginTransaction();
      session.save(entity);
      transaction.commit();
      session.close();
      return true;
//        return CrudUtil.execute(sql, entity.getId(),entity.getName(), entity.getAddress(),entity.getSalary());

    }

    @Override
    public boolean update(Customer entity) throws SQLException, ClassNotFoundException {

    Configuration configuration = new Configuration()
            .configure("hibernate.cfg.xml")
            .addAnnotatedClass(Customer.class);
    SessionFactory sessionFactory = configuration.buildSessionFactory();
    Session session=sessionFactory.openSession();

    Transaction transaction = session.beginTransaction();
    Customer customer=session.find(Customer.class,entity.getId());
    customer.setName(entity.getName());
    customer.setAddress(entity.getAddress());
    customer.setSalary(entity.getSalary());
    session.save(customer);
    transaction.commit();
    return true;
    }

    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {
         Configuration configuration=new Configuration()
                 .configure("hibernate.cfg.xml")
                 .addAnnotatedClass(Customer.class);
         SessionFactory sessionFactory=configuration.buildSessionFactory();
         Session session = sessionFactory.openSession();
         Transaction transaction = session.beginTransaction();
         session.delete(session.find(Customer.class,value));
         transaction.commit();
         return true;
    }

    @Override
    public List<Customer> getAll() throws SQLException, ClassNotFoundException {
        List<Customer> list = new ArrayList<>();
        String sql = "select * from customer";
  //       PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = CrudUtil.execute(sql);
        while(resultSet.next()){
                                list.add(new Customer(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getDouble(4)
                    )
            );
        }
        return list;
    }
}
