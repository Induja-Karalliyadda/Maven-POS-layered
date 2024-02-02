package bo.custom.impl;

import bo.custom.OrderBo;
import dao.custom.OrderDao;
import dao.custom.impl.OrderDaoImpl;
import dto.OrderDto;

import java.sql.SQLException;

public class OrderBoImpl implements OrderBo {

    private OrderDao orderDao = new OrderDaoImpl();
    @Override
    public boolean saveOrder(OrderDto dto) throws SQLException, ClassNotFoundException {
        return orderDao.save(dto);
    }

    @Override
    public String generateId() throws SQLException, ClassNotFoundException {
        OrderDto dto = orderDao.getLastOrder();
        if (dto!=null){
            String id = dto.getOrderid();
            int num = Integer.parseInt(id.split("[D]")[1]);
            num++;
            return String.format("D%03d",num);
        }else{
            return  "D001";
        }
    }


}
