package dao.custom.impl;

import db.DBConnection;
import dto.CustomerDto;
import dao.custom.CustomerDao;
import entity.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {

    @Override
    public boolean updateCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        String sql = "update from customer set name=? address=? salary=? where id=?";
        PreparedStatement prstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        prstm.setString(1,dto.getName());
        prstm.setString(2,dto.getAddress());
        prstm.setDouble(3,dto.getSalary());
        prstm.setString(4,dto.getId());
        int result = prstm.executeUpdate();
        if(result>0) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        String sql = "delete from customer where id=?";
        PreparedStatement prstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        prstm.setString(1,id);
        int result = prstm.executeUpdate();
        if(result>0){
            return true;
        }
        return false;
    }

    @Override
    public List<CustomerDto> allCustomers() throws SQLException, ClassNotFoundException {
        List<CustomerDto> customerDtos = new ArrayList<>();
        String sql = "select * from customer";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet rst = pstm.executeQuery();
        while(rst.next()){
            customerDtos.add(new CustomerDto(
                            rst.getString(1),
                            rst.getString(2),
                            rst.getString(3),
                            rst.getDouble(4)
                    )
            );
        }
        return customerDtos;
    }

    @Override
    public CustomerDto searchCustomer(String id) {
        return null;
    }

    @Override
    public boolean save(Customer entity) throws SQLException, ClassNotFoundException {
        String sql = "insert into customer value(?,?,?,?)";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1,entity.getId());
        pstm.setString(2,entity.getName());
        pstm.setString(3,entity.getAddress());
        pstm.setDouble(4,entity.getSalary());

        int result = pstm.executeUpdate();
        if(result>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean update(Customer entity) {
        return false;
    }

    @Override
    public boolean delete(String value) {
        return false;
    }

    @Override
    public List<Customer> getAll() {
        return null;
    }
}
