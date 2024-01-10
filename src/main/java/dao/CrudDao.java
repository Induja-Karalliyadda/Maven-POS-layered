package dao;

import java.sql.SQLException;
import java.util.List;

public interface CrudDao<T>  extends SuperDao{

    boolean save(T entity) throws SQLException, ClassNotFoundException;
    boolean update(T entity);
    boolean delete(String value);
    List<T> getAll();

}
