package controller;
import bo.BoFactory;
import bo.custom.CustomerBo;
import bo.custom.impl.CustomerBoImpl;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import dao.util.BoType;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;
import dto.CustomerDto;
import dto.tm.CustomerTm;
import dao.custom.CustomerDao;
import dao.custom.impl.CustomerDaoImpl;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.*;
import java.util.List;


public class CustomerFormController{

    public TreeTableColumn colid;
    public JFXTreeTableView<CustomerTm> tblCustomer;
    public TreeTableColumn colname;
    public TreeTableColumn coladdress;
    public TreeTableColumn colsalary;
    public TreeTableColumn coloption;
    @FXML
    private TextField txt_id;

    @FXML
    private TextField txt_salary;

    @FXML
    private TextField txt_name;

    @FXML
    private TextField txt_address;
    

    private CustomerBo<CustomerDto> customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);

    {

    };

    public void initialize() {
        colid.setCellValueFactory(new TreeItemPropertyValueFactory<>("id"));
        colname.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        coladdress.setCellValueFactory(new TreeItemPropertyValueFactory<>("address"));
        colsalary.setCellValueFactory(new TreeItemPropertyValueFactory<>("salary"));
        coloption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));
        loadCustomerTable();

        tblCustomer.setOnMouseClicked(event -> {
            if(event.getClickCount()==1&&(!tblCustomer.getSelectionModel().isEmpty())){
                TreeItem<CustomerTm> customer = tblCustomer.getSelectionModel().getSelectedItem();
                txt_id.setText(customer.getValue().getId());
                txt_address.setText(customer.getValue().getAddress());
                txt_name.setText(customer.getValue().getName());
                txt_salary.setText(String.valueOf(customer.getValue().getSalary()));
            }
        });
    }

    private void setData(CustomerTm newvalue) {
        if(newvalue!=null) {
            txt_id.setEditable(false);
            txt_id.setText(newvalue.getId());
            txt_address.setText(newvalue.getAddress());
            txt_name.setText(newvalue.getName());
            txt_salary.setText(String.valueOf(newvalue.getSalary()));
        }
    }

    private void loadCustomerTable() {
        ObservableList<CustomerTm> tmlist = FXCollections.observableArrayList();
        try {
            List<CustomerDto> customerDtos = customerBo.allCustomer();
            for(CustomerDto customerDto:customerDtos){
                Button btn = new Button("delete");
                tmlist.add(new CustomerTm(
                        customerDto.getId(),
                        customerDto.getName(),
                        customerDto.getAddress(),
                        customerDto.getSalary(),
                        btn
                ));
                btn.setOnAction(actionEven->{
                    deleteCustomer(customerDto.getId());
                });
            }
            TreeItem<CustomerTm> treeItem= new RecursiveTreeItem<>(tmlist, RecursiveTreeObject::getChildren);
            tblCustomer.setRoot(treeItem);
            tblCustomer.setShowRoot(false);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    private void deleteCustomer(String id) {
        String sql = "delete from customer where id=?";

        boolean result = false;
        try {
            result = customerBo.deleteCustomer(id);
            if(result){
                new Alert(Alert.AlertType.INFORMATION,"Customer deleted").show();
                loadCustomerTable();
            }else {
                new Alert(Alert.AlertType.ERROR, "Something went wrong");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    void reloadButtonOnAction(ActionEvent event) {
        loadCustomerTable();
        tblCustomer.refresh();
        clearFields();
    }

    private void clearFields() {
        tblCustomer.refresh();
        txt_salary.clear();
        txt_name.clear();
        txt_id.clear();
        txt_address.clear();
        txt_id.setEditable(true);
    }

    @FXML
    void saveButtonOnAction(ActionEvent event) {
        if(!isEmpty()){
        try {
            boolean isSaved = customerBo.saveCustommer(new CustomerDto(txt_id.getText(),
                    txt_name.getText(),
                    txt_address.getText(),
                    Double.parseDouble(txt_salary.getText())
            ));
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Successfull").show();
                loadCustomerTable();
                clearFields();
            }else{
                new Alert(Alert.AlertType.ERROR,"Empty").show();
            }
        }catch (SQLIntegrityConstraintViolationException ex){
            new Alert(Alert.AlertType.ERROR,"Duplicate Entity").show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        }else{
            new Alert(Alert.AlertType.ERROR,"Empty field found").show();
        }
    }



    public void updateButtonOnAction(ActionEvent actionEvent) {
        if(!isEmpty()) {
            CustomerDto c = new CustomerDto(txt_id.getText(),
                    txt_name.getText(),
                    txt_address.getText(),
                    Double.parseDouble(txt_salary.getText())
            );
            String sql = "update customer set name='" + c.getName() + "',address='" + c.getAddress() + "',salary=" + c.getSalary() + "where id='" + c.getId() + "'";
            try {
                Statement stm = DBConnection.getInstance().getConnection().createStatement();
                int result = stm.executeUpdate(sql);
                if (result > 0) {
                    new Alert(Alert.AlertType.INFORMATION, "Update successfully").show();
                    loadCustomerTable();
                    clearFields();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else {
            new Alert(Alert.AlertType.ERROR,"Empty field found").show();
        }
    }

    public void backBtnOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage)tblCustomer.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashboardForm.fxml"))));
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }
    private  boolean isEmpty(){
        if((txt_name.getText().isEmpty()||txt_id.getText().isEmpty()
                ||txt_salary.getText().isEmpty()||txt_address.getText().isEmpty())){
            return true;
        }else {
            return false;
        }

    }

    public void reportButtonOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design= JRXmlLoader.load("src/main/resources/reports/customer_report.jrxml");
              //
            //
           JasperReport jasperReport =JasperCompileManager.compileReport(design);
           JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null,DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException | ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
