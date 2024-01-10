package bo.custom;

import bo.SuperBo;
import dto.CustomerDto;

import java.sql.SQLException;
import java.util.List;

public interface CustomerBo<T> extends SuperBo {
    boolean saveCustommer(T dto) throws SQLException, ClassNotFoundException;
    boolean updateCustomer(T dto);
    boolean deleteCustomer(String id);
    List<CustomerDto> allCustomer();
}
