package com.example.test;

import com.example.test.data.Request;
import com.example.test.data.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.util.*;

public class MainWindowController {
    public ObjectMapper mapper = new ObjectMapper();
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

    @FXML
    private Label mainLabel;

    @FXML
    private TableView<ExtendedRequests> requestsTableView;
    @FXML
    private TableColumn<ExtendedRequests, String> requestTypeColumn;

    @FXML
    private TableColumn<ExtendedRequests, String> requestStatusColumn;

    @FXML
    private TableColumn<ExtendedRequests, String> requestDateStartColumn;

    @FXML
    private TableColumn<ExtendedRequests, String> requestDateEndColumn;

    @FXML
    private TableColumn<ExtendedRequests, String> visitPurposeColumn;

    @FXML
    private TableColumn<ExtendedRequests, String> employeeColumn;

    @FXML
    private TableColumn<ExtendedRequests, String> typeColumn;

    @FXML
    private TableColumn<ExtendedRequests, String> visitorColumn;

    private ObservableList<ExtendedRequests> requestsOList;

    @FXML
    private ComboBox subdivisionFilterCombo;
    @FXML
    private ComboBox typeFilterCombo;
    @FXML
    private ComboBox statusFilterCombo;

    private final List<Subdivisions> subdivisionsList;

    private final List<RequestTypes> requestTypesList;

    private final List<RequestStatuses> requestStatusesList;

    public MainWindowController() throws IOException {
        String requestResultSubdivisions =
                Request.create("http://localhost:8080/subdivisions", "GET")
                        .addHeader("Content-Type", "application/json")
                        .execute();

        Subdivisions[] subdivisions = mapper.readValue(requestResultSubdivisions, Subdivisions[].class);
        this.subdivisionsList = new ArrayList<>(Arrays.asList(subdivisions));
        String requestResultTypes =
                Request.create("http://localhost:8080/request_types", "GET")
                        .addHeader("Content-Type", "application/json")
                        .execute();

        RequestTypes[] types = mapper.readValue(requestResultTypes, RequestTypes[].class);
        this.requestTypesList = new ArrayList<>(Arrays.asList(types));
        String requestResultStatuses =
                Request.create("http://localhost:8080/request_statuses", "GET")
                        .addHeader("Content-Type", "application/json")
                        .execute();

        RequestStatuses[] statuses = mapper.readValue(requestResultStatuses, RequestStatuses[].class);
        this.requestStatusesList = new ArrayList<>(Arrays.asList(statuses));
    }

    public void setupPage() throws IOException {
        this.requestsTableView.setEditable(true);
        requestsTableView.setItems(this.requestsOList);
        visitorColumn.setCellValueFactory(request -> new SimpleStringProperty(request.getValue().getVisitor().getSurname() + " " + request.getValue().getVisitor().getName() + " " +request.getValue().getVisitor().getPatronymic()));
        requestTypeColumn.setCellValueFactory(request -> new SimpleStringProperty(request.getValue().getRequest_type().getName()));
        requestStatusColumn.setCellValueFactory(request -> new SimpleStringProperty(request.getValue().getRequest_status().getName()));
        requestDateStartColumn.setCellValueFactory(request -> new SimpleStringProperty(request.getValue().getDate_start().toString()));
        requestDateEndColumn.setCellValueFactory(request -> {
            String date;

            if (request.getValue().getDate_end() == null) {
                date = "";
            } else {
                date = request.getValue().getDate_end().toString();
            }

            return new SimpleStringProperty(date);
        });
        visitPurposeColumn.setCellValueFactory(request -> new SimpleStringProperty(request.getValue().getVisit_purpose().getName()));
        employeeColumn.setCellValueFactory(request -> new SimpleStringProperty(request.getValue().getEmployee().getSurname() + " " + request.getValue().getEmployee().getName() + " " +request.getValue().getEmployee().getPatronymic()));
        typeColumn.setCellValueFactory(request -> {
            String type;
            if (request.getValue().is_group()){
                type = "Групповое";
            } else{
                type = "Персональное";
            }
            return new SimpleStringProperty(type);
        });

        ObservableList<String> subdivisionsStrings = FXCollections.observableArrayList("Все");
        for (Subdivisions sub : subdivisionsList){
            subdivisionsStrings.add(sub.getName());
        }
        subdivisionFilterCombo.setItems(subdivisionsStrings);
        subdivisionFilterCombo.setValue("Все");

        ObservableList<String> typesStrings = FXCollections.observableArrayList("Все");
        for (RequestTypes sub : requestTypesList){
            typesStrings.add(sub.getName());
        }
        typeFilterCombo.setItems(typesStrings);
        typeFilterCombo.setValue("Все");

        ObservableList<String> statusesStrings = FXCollections.observableArrayList("Все");
        for (RequestStatuses sub : requestStatusesList){
            statusesStrings.add(sub.getName());
        }
        statusFilterCombo.setItems(statusesStrings);
        statusFilterCombo.setValue("Все");
        updateTable();
    }

    public void switchToAuthScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(Application.class.getResource("auth-page.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 800, 800);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void onBackButtonAction(ActionEvent event) throws IOException {
        switchToAuthScene(event);
    }

    public void setDisplayCode(String name){
        mainLabel.setText("Добро пожаловать, " + name);
    }

    public void updateTable() throws IOException {
        this.requestsTableView.setEditable(true);
        String requestResult =
                Request.create("http://localhost:8080/requests?extend=true", "GET")
                        .addHeader("Content-Type", "application/json")
                        .execute();

        ExtendedRequests[] requests = mapper.readValue(requestResult, ExtendedRequests[].class);
        List<ExtendedRequests> requestsList = new ArrayList<>(Arrays.asList(requests));
        List<ExtendedRequests> filteredRequestsList = new ArrayList<>();
        Subdivisions newsub = new Subdivisions();
        for (Subdivisions sb : subdivisionsList){
            if (sb.getName().equals(subdivisionFilterCombo.getValue())) newsub = sb;
        }
        RequestTypes newType = new RequestTypes();
        for (RequestTypes sb : requestTypesList){
            if (sb.getName().equals(typeFilterCombo.getValue())) newType = sb;
        }
        RequestStatuses newStatus = new RequestStatuses();
        for (RequestStatuses sb : requestStatusesList){
            if (sb.getName().equals(statusFilterCombo.getValue())) newStatus = sb;
        }
        for (ExtendedRequests exr :  requestsList){

            if (subdivisionFilterCombo.getValue().equals("Все")) {
                if (typeFilterCombo.getValue().equals("Все")) {
                    if (statusFilterCombo.getValue().equals("Все")) {
                        filteredRequestsList.add(exr);
                    } else if (newStatus.getId() == exr.getRequest_status().getId()){
                        filteredRequestsList.add(exr);
                    }
                } else if (newType.getId() == exr.getRequest_type().getId()){
                    if (statusFilterCombo.getValue().equals("Все")) {
                        filteredRequestsList.add(exr);
                    } else if (newStatus.getId() == exr.getRequest_status().getId()){
                        filteredRequestsList.add(exr);
                    }
                }

            } else if (newsub.getId() == exr.getEmployee().getSubdivision_id()){
                if (typeFilterCombo.getValue().equals("Все")) {
                    if (statusFilterCombo.getValue().equals("Все")) {
                        filteredRequestsList.add(exr);
                    } else if (newStatus.getId() == exr.getRequest_status().getId()){
                        filteredRequestsList.add(exr);
                    }
                } else if (newType.getId() == exr.getRequest_type().getId()){
                    if (statusFilterCombo.getValue().equals("Все")) {
                        filteredRequestsList.add(exr);
                    } else if (newStatus.getId() == exr.getRequest_status().getId()){
                        filteredRequestsList.add(exr);
                    }
                }

            }
        }
        this.requestsOList = FXCollections.observableArrayList(filteredRequestsList);
        requestsTableView.setItems(this.requestsOList);
    }

    public void redactRequest() throws IOException {
        if ( requestsTableView.getSelectionModel().getSelectedItem() != null){
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("model-window.fxml"));
            Parent root = loader.load();



            Stage window = new Stage();
            window.initModality(Modality.WINDOW_MODAL);
            ModelWindowController modelWindowController = loader.getController();
            modelWindowController.init(requestsTableView.getSelectionModel().getSelectedItem(), window);
            Scene scene = new Scene(root, 800, 800);
            window.setScene(scene);
            window.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Вы не выбрали запись для редактирования");
            alert.showAndWait();
        }

    }
}
