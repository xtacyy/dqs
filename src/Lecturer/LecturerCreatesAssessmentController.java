package Lecturer;

import Helpers.AlertHandler;
import Helpers.MySQLHandler;
import Model.Assessment;
import Model.Lecturer;
import Model.LecturerModule;
import Model.Question;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class LecturerCreatesAssessmentController {

    @FXML
    private TextField titleField;

    @FXML
    private DatePicker assignedDate;

    @FXML
    private DatePicker dueDate;

    @FXML
    private RadioButton summativeRadio;

    @FXML
    private RadioButton formativeRadio;

    @FXML
    private Button addQ1Button;

    @FXML
    private Button addQ2Button;

    @FXML
    private Button addQ3Button;

    @FXML
    private Button addQ4Button;

    @FXML
    private Button addQ5Button;

    private LecturerModule selectedModule;

    private Lecturer lecturer;

    private MySQLHandler SqlHandler;

    private Question questions[];

    private Assessment assessment;

    @FXML
    public void initialize(){
        try{
            SqlHandler = new MySQLHandler("c1841485", "6Z=q]K~GXKzcjW=d");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void initData(Lecturer lecturer, LecturerModule selectedModule, Question questions[], Assessment assessment){
        this.lecturer = lecturer;
        this.selectedModule = selectedModule;
        this.questions = questions;
        this.assessment = assessment;
        setupQuestionButtons();

        loadAssessmentData();

    }

    public void loadAssessmentData(){
        if(assessment.getNameProperty() != null){
            titleField.setText(assessment.getName());
        }

        if(assessment.getAssignedDateProperty() != null){
            String dateString = assessment.getAssignedDateProperty().get();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            assignedDate.setValue(localDate);
        }

        if(assessment.getDueDateProperty() != null){
            String dateString = assessment.getDueDateProperty().get();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            dueDate.setValue(localDate);
        }

        if(assessment.getType() == 0){
            onFormative();
        }else{
            onSummative();
        }
    }

    public void saveAssessmentInfo(){
        if(titleField.getText().length() > 0){
            assessment.setName(titleField.getText());
        }

        if(assignedDate.getValue() != null){
            assessment.setAssignedDate(assignedDate.getValue().toString());
        }

        if(dueDate.getValue() != null){
            assessment.setDueDate(dueDate.getValue().toString());
        }

        if(summativeRadio.isSelected()){
            assessment.setType(1);
        }

        if(formativeRadio.isSelected()){
            assessment.setType(0);
        }

    }

    public void setupQuestionButtons(){
        if(questions[0] == null){
            addQ1Button.setText("Add Question #1");
        }else{
            addQ1Button.setText("Edit Question #1");
        }

        if(questions[1] == null){
            addQ2Button.setText("Add Question #2");
        }else{
            addQ2Button.setText("Edit Question #3");
        }

        if(questions[2] == null){
            addQ3Button.setText("Add Question #3");
        }else{
            addQ3Button.setText("Edit Question #3");
        }

        if(questions[3] == null){
            addQ4Button.setText("Add Question #4");
        }else{
            addQ4Button.setText("Edit Question #4");
        }

        if(questions[4] == null){
            addQ5Button.setText("Add Question #5");
        }else{
            addQ5Button.setText("Edit Question #5");
        }
    }

    public void onBack(){
        loadLecturerSelectedModule();
    }

    public void loadLecturerSelectedModule(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Lecturer/LecturerSelectedModuleUI.fxml"));
            loader.load();
            LecturerSelectedModuleController controller = loader.getController();
            controller.initData(lecturer, selectedModule);

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Module");
            stage.setScene(new Scene(parent, 600, 400));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    public void onSave(){
        saveAssessmentInfo();

        if (titleField.getText().length() > 0 && assignedDate.getValue() != null && dueDate.getValue() != null && (summativeRadio.isSelected() || formativeRadio.isSelected()) && questions[0] != null && questions[1] != null && questions[2] != null && questions[3] != null && questions[4] != null) {
            // Can you please check if assigned dates and due dates are valid?

            System.out.println("Title: " + titleField.getText());
            System.out.println("Assigned Date: " + assignedDate.getValue().toString());
            System.out.println("Due Date: " + dueDate.getValue().toString());
            String type = summativeRadio.isSelected() ? "Summative" : "Formative";
            System.out.println("Type: " + type);

            // Questions are stored in the questions array

            // IF EVERYTHING IS SAVED, LOAD lecturer selected Module SCREEN AGAIN:
            //loadLecturerSelectedModule();
        }else{
            AlertHandler.showShortMessage("Error", "Please fill out every information to create an assessment");
        }
    }

    public void onSummative(){
        summativeRadio.setSelected(true);
        formativeRadio.setSelected(false);
    }

    public void onFormative(){
        summativeRadio.setSelected(false);
        formativeRadio.setSelected(true);
    }

    public void showQuestionDialog(int questionIndex){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setPrefWidth(500);
        alert.setTitle("Creating a new question");
        alert.setHeaderText("What type of question would you like to add?");
        alert.setContentText("Choose your option.");

        ButtonType buttonTypeOne = new ButtonType("Multiple Choice");
        ButtonType buttonTypeTwo = new ButtonType("Regular");

        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            // Create Multiple Choice Question
            loadMultipleQuestion(questionIndex);
        } else if (result.get() == buttonTypeTwo) {
            // Create Regular Question
            loadRegularQuestion(questionIndex);
        }
    }

    public void loadMultipleQuestion(int questionIndex){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Lecturer/LecturerAddsMultipleUI.fxml"));
            loader.load();
            LecturerAddsMultipleController controller = loader.getController();
            controller.initData(lecturer, selectedModule, questions, questionIndex, assessment);

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Multiple Choice Question");
            stage.setScene(new Scene(parent, 600, 400));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    public void loadRegularQuestion(int questionIndex){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Lecturer/LecturerAddsTextUI.fxml"));
            loader.load();
            LecturerAddsTextController controller = loader.getController();
            controller.initData(lecturer, selectedModule, questions, questionIndex, assessment);

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Regular Question");
            stage.setScene(new Scene(parent, 600, 243));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }


    public void onAddQ1(){
        saveAssessmentInfo();
        if(questions[0] == null){
            showQuestionDialog(0);
        }else{
            if(questions[0].getType() == "m"){
                loadMultipleQuestion(0);
            }else{
                loadRegularQuestion(0);
            }
        }
    }

    public void onAddQ2(){
        saveAssessmentInfo();
        if(questions[1] == null){
            showQuestionDialog(1);
        }else{
            if(questions[1].getType() == "m"){
                loadMultipleQuestion(1);
            }else{
                loadRegularQuestion(1);
            }
        }
    }

    public void onAddQ3(){
        saveAssessmentInfo();
        if(questions[2] == null){
            showQuestionDialog(2);
        }else{
            if(questions[2].getType() == "m"){
                loadMultipleQuestion(2);
            }else{
                loadRegularQuestion(2);
            }
        }
    }

    public void onAddQ4(){
        saveAssessmentInfo();
        if(questions[3] == null){
            showQuestionDialog(3);
        }else{
            if(questions[3].getType() == "m"){
                loadMultipleQuestion(3);
            }else{
                loadRegularQuestion(3);
            }
        }
    }

    public void onAddQ5(){
        saveAssessmentInfo();
        if(questions[4] == null){
            showQuestionDialog(4);
        }else{
            if(questions[4].getType() == "m"){
                loadMultipleQuestion(4);
            }else{
                loadRegularQuestion(4);
            }
        }
    }

    public void closeScreen(){
        Stage oldStage = (Stage)titleField.getScene().getWindow();
        oldStage.close();
    }


}
