package Lecturer;

import Helpers.AlertHandler;
import Helpers.MySQLHandler;
import Model.Lecturer;
import Model.LecturerModule;
import Model.StudentModule;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class LecturerHomeController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView modulesTable;

    @FXML
    private TableColumn<LecturerModule, String> moduleColumn;

    private ObservableList<LecturerModule> modules;

    private LecturerModule selectedModule;

    private Lecturer lecturer;

    private MySQLHandler SqlHandler;

    @FXML
    public void initialize(){
        setupTableView();
        try{
            SqlHandler = new MySQLHandler("c1841485", "6Z=q]K~GXKzcjW=d");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void initData(Lecturer lecturer){
        this.lecturer = lecturer;
        loadUser();
        loadModules();
    }

    public void loadUser(){
        welcomeLabel.setText("Welcome " + lecturer.getName());
    }

    public void loadModules(){
        modules = FXCollections.observableArrayList();
        LecturerModule module1 = new LecturerModule(1, "Maths");
        LecturerModule module2 = new LecturerModule(1, "Phsyics");

        modules.add(module1);
        modules.add(module2);
        lecturer.modules.add(module1);
        lecturer.modules.add(module2);

        modulesTable.setItems(modules);
        modulesTable.refresh();
    }

    public void setupTableView(){
        moduleColumn.setCellValueFactory(cellData -> cellData.getValue().getModuleNameProperty());
        moduleColumn.setStyle("-fx-alignment: CENTER");

        modulesTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LecturerModule>() {
            @Override
            public void changed(ObservableValue<? extends LecturerModule> observable, LecturerModule oldValue, LecturerModule newValue) {
                selectedModule = newValue;
            }
        });
    }

    public void onCreateModule(){
        // Load create module screen
    }

    public void onSignOut(){
        SqlHandler.Close();
        loadLogin();
    }

    public void onSelectModule(){
        if (selectedModule == null){
            AlertHandler.showErrorAlert("Error", "Select a module first!", "Select a module from the list first and then click on open module");
        }else{
            loadModuleScreen();
        }
    }

    public void loadModuleScreen(){
        // Load module screen for selected module
    }

    public void loadLogin(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Login/LoginUI.fxml"));
            loader.load();

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(parent, 492, 388));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    public void closeScreen(){
        Stage oldStage = (Stage)welcomeLabel.getScene().getWindow();
        oldStage.close();
    }
}