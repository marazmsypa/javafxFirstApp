package com.example.test;

import com.example.test.data.Request;
import com.example.test.data.model.Employees;
import com.example.test.data.model.ExtendedRequests;
import com.example.test.data.model.Requests;
import com.example.test.data.model.Visitors;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;

public class ModelWindowController {
    @FXML
    private Stage window;
    @FXML
    private Label surnameLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label patronymicLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label organizationLabel;
    @FXML
    private Label birthdateLabel;
    @FXML
    private Label passseriaLabel;
    @FXML
    private Label passnumberLabel;
    @FXML
    private Label visitPurposeLabel;
    @FXML
    private Label requestTypeLabelLabel;
    @FXML
    private Label statusLabel;

    @FXML
    private DatePicker mainDatePicker;

    private Visitors curentVisitor;

    private ExtendedRequests extendedRequests;
    public ObjectMapper mapper = new ObjectMapper();


    public ModelWindowController() {
    }

    @FXML
    public void init(ExtendedRequests extendedRequests, Stage window) throws IOException {
        this.window = window;
        this.extendedRequests = extendedRequests;
        curentVisitor = extendedRequests.getVisitor();
        window.setTitle("Редактирование заявки посетителя " +  curentVisitor.getSurname() + " " + curentVisitor.getName() + " " + curentVisitor.getPatronymic() + " от " + extendedRequests.getDate_start().toString() );
        window.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                loadData();
            }
        });
    }

    public void closeWindow(){
        if (window.isShowing()){
            window.close();
        }

    }

    public void loadData(){
        surnameLabel.setText(curentVisitor.getSurname());
        nameLabel.setText(curentVisitor.getName());
        patronymicLabel.setText(curentVisitor.getPatronymic());
        phoneLabel.setText(curentVisitor.getPhone());
        emailLabel.setText(curentVisitor.getEmail());
        organizationLabel.setText(curentVisitor.getOrganization());
        birthdateLabel.setText(curentVisitor.getBirth_date().toString());
        passseriaLabel.setText(curentVisitor.getPassport_series());
        passnumberLabel.setText(curentVisitor.getPassport_number());
        visitPurposeLabel.setText(extendedRequests.getVisit_purpose().getName());
        requestTypeLabelLabel.setText(extendedRequests.getRequest_type().getName());
        statusLabel.setText(extendedRequests.getRequest_status().getName());
    }

    public void declineRequest() throws IOException {
        Requests req = new Requests();
        req.setId(extendedRequests.getId());
        req.setRequest_type_id(extendedRequests.getRequest_type().getId());
        req.setRequest_status_id(3);
        req.setDate_start(extendedRequests.getDate_start());
        req.setDate_end(null);
        req.setVisit_purpose_id(extendedRequests.getVisit_purpose().getId());
        req.setEmployee_id(extendedRequests.getEmployee().getId());
        req.setGroup_id(extendedRequests.getGroup_id());
        req.setVisitor_id(extendedRequests.getVisitor().getId());
        req.setIs_group(extendedRequests.is_group());
        req.setMessage("Заявка на посещение объекта КИИ отклонена в связи с нарушением Федерального закона от 26.07.2017 № 187-ФЗ «О безопасности критической информационной инфраструктуры Российской Федерации»");
        String requestResult =
                Request.create("http://localhost:8080/requests", "PUT")
                        .addJSONstring(mapper.writeValueAsString(req))
                        .execute();

        closeWindow();

    }

    public void aceptRequest() throws IOException {
        if (mainDatePicker.getValue() != null){
            Requests req = new Requests();
            req.setId(extendedRequests.getId());
            req.setRequest_type_id(extendedRequests.getRequest_type().getId());
            req.setRequest_status_id(2);
            req.setDate_start(extendedRequests.getDate_start());
            req.setDate_end(Date.from(mainDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            req.setVisit_purpose_id(extendedRequests.getVisit_purpose().getId());
            req.setEmployee_id(extendedRequests.getEmployee().getId());
            req.setGroup_id(extendedRequests.getGroup_id());
            req.setVisitor_id(extendedRequests.getVisitor().getId());
            req.setIs_group(extendedRequests.is_group());
            req.setMessage( String.format("Заявка на посещение объекта КИИ одобрена,  дата посещения: %s, время посещения: ХХ.ХХ", Date.from(mainDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()) ));
            String requestResult =
                    Request.create("http://localhost:8080/requests", "PUT")
                            .addJSONstring(mapper.writeValueAsString(req))
                            .execute();
            closeWindow();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING, "Вы не выбрали дату посещения");
            alert.showAndWait();
        }

    }
}
