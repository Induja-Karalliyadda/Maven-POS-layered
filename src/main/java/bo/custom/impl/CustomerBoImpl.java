package bo.custom.impl;

import bo.custom.CustomerBo;
import dao.custom.CustomerDao;
import dao.custom.impl.CustomerDaoImpl;
import dto.CustomerDto;
import entity.Customer;

import java.sql.SQLException;
import java.util.List;

public class CustomerBoImpl implements CustomerBo<CustomerDto> {

    private CustomerDao customerDao = new CustomerDaoImpl();
    @Override
    public boolean saveCustommer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        return customerDao.save(new Customer(
                dto.getId(),
                dto.getName(),
                dto.getAddress(),
                dto.getSalary()
                )

        );
    }

    @Override
    public boolean updateCustomer(CustomerDto dto) {
        return false;
    }

    @Override
    public boolean deleteCustomer(String id) {
        return false;
    }

    @Override
    public List<CustomerDto> allCustomer() {
        return null;
    }
}
