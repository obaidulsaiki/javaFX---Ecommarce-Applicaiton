import java.net.URL;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class MainController {

    @FXML
    private Button ChangePasswordBtn;

    @FXML
    private PasswordField Confirm_password;

    @FXML
    private Button alreadylogged;

    @FXML
    private TextField createAns;

    @FXML
    private Button createBtn;

    @FXML
    private TextField createEmail;

    @FXML
    private PasswordField createPassword;

    @FXML
    private TextField createUsername;

    @FXML
    private TextField forgetP_answer;

    @FXML
    private AnchorPane forgetpassWord1;

    @FXML
    private AnchorPane forgetpassWord2;

    @FXML
    private Hyperlink forgotPass;

    @FXML
    private ComboBox<?> fp_questions;

    @FXML
    private TextField fp_username;

    @FXML
    private Button goBackBtn;

    @FXML
    private Button goBackBtn1;

    @FXML
    private Button loginBtn;

    @FXML
    private AnchorPane loginPage;

    @FXML
    private PasswordField new_password;

    @FXML
    private PasswordField password;

    @FXML
    private Button proceedBtn;

    @FXML
    private ComboBox<?> s_questions;

    @FXML
    private AnchorPane side_form;

    @FXML
    private Button signupbtn;

    @FXML
    private TextField username;

    @FXML
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Alert alert;
    // Preparing to make combox of question

    private String[] questionlist = {
            "What is your mother's maiden name?",
            "What is the name of your first pet?",
            "What is the name of your first school?",
            "What is the name of your first crush?",
            "What is the name of your first car?" };

    public void requestionList() {
        List<String> questions = new ArrayList<>();
        for (String question : questionlist) {
            questions.add(question);
        }
        ObservableList questionlists = FXCollections.observableArrayList(questions);
        s_questions.setItems(questionlists);
    }

    // ---------------------------------------------------
    // -------------------Login page---------------------
    public void loginBtn(ActionEvent event) {
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();
        } else {
            String query = "SELECT username, password FROM employee WHERE username = ? AND password = ?";
            connect = database.connectDB();
            try {
                prepare = connect.prepareStatement(query);
                prepare.setString(1, username.getText());
                prepare.setString(2, password.getText());
                result = prepare.executeQuery();
                if (result.next()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Login Successful");
                    alert.showAndWait();
                    // close login form------------- open admin form
                    try {
                        URL fxmlLocation = getClass().getResource("admin.fxml");
                        FXMLLoader loader = new FXMLLoader(fxmlLocation);
                        AnchorPane root = loader.load();
                        Scene scene = new Scene(root, 1100, 400);
                        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        primaryStage.setTitle("Samaaya Attire");
                        primaryStage.setScene(scene);
                        primaryStage.show();
                        loginBtn.getScene().getWindow().hide();
                        System.out.println("Button clicked");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }

                    // -------------------------------------------------
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid Username or Password");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                Platform.runLater(() -> {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Failed to login: " + e.getMessage());
                    errorAlert.showAndWait();
                });
                System.out.println("Error: " + e);
            } finally {
                try {
                    if (prepare != null)
                        prepare.close();
                    if (connect != null)
                        connect.close();
                } catch (SQLException e) {
                    System.out.println("Error closing resources: " + e.getMessage());
                }
            }
        }
    }

    // ---------------------------------------------------
    // -------------------Sign up page---------------------

    public void registrationBtn(ActionEvent event) {
        if (createUsername.getText().isEmpty() || createEmail.getText().isEmpty() || createPassword.getText().isEmpty()
                || createAns.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();
        } else {
            String query = "INSERT INTO employee (username, password, Email, questions, answer, date)"
                    + " VALUES (?, ?, ?, ?, ?, ?)";
            connect = database.connectDB();
            try {
                String checkUserNameEmail = "SELECT * FROM employee WHERE username = ? OR Email = ?";
                prepare = connect.prepareStatement(checkUserNameEmail);
                prepare.setString(1, createUsername.getText());
                prepare.setString(2, createEmail.getText());
                result = prepare.executeQuery();
                if (result.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Username or Email already exists");
                    alert.showAndWait();
                } else if (createPassword.getText().length() < 8
                        || !createPassword.getText()
                                .matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText(
                            "Password must be at least 8 characters long\n contain at least one digit\n one lowercase letter\n one uppercase letter\n one special character.");
                    alert.showAndWait();
                } else {
                    prepare = connect.prepareStatement(query);
                    prepare.setString(1, createUsername.getText());
                    prepare.setString(2, createPassword.getText());
                    prepare.setString(3, createEmail.getText());
                    prepare.setString(4, s_questions.getValue().toString());
                    prepare.setString(5, createAns.getText());
                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setDate(6, sqlDate);

                    prepare.executeUpdate();
                    Platform.runLater(() -> {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Account created successfully");
                        successAlert.showAndWait();
                    });
                    createUsername.setText("");
                    createPassword.setText("");
                    createEmail.setText("");
                    s_questions.getSelectionModel().clearSelection();
                    createAns.setText("");

                    TranslateTransition slide = new TranslateTransition();
                    slide.setNode(side_form);
                    slide.setToX(0);
                    slide.setDuration(Duration.seconds(0.5));
                    slide.setOnFinished((ActionEvent e) -> {
                        alreadylogged.setVisible(false);
                        createBtn.setVisible(true);
                    });
                    slide.play();
                }

            } catch (Exception e) {
                Platform.runLater(() -> {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Failed to create account: " + e.getMessage());
                    errorAlert.showAndWait();
                });
                System.out.println("Error: " + e);
            } finally {
                try {
                    if (prepare != null)
                        prepare.close();
                    if (connect != null)
                        connect.close();
                } catch (SQLException e) {
                    System.out.println("Error closing resources: " + e.getMessage());
                }
            }
        }
    }
    // --------------------------------------------------------------------------------

    // -------------------Forget password : question page---------------------
    public void switchingforgetPass() {
        forgetpassWord1.setVisible(true);
        loginPage.setVisible(false);
        forgetPassQues();
    }

    public void forgetPassQues() {
        List<String> questions = new ArrayList<>();
        for (String question : questionlist) {
            questions.add(question);
        }
        ObservableList questionlists = FXCollections.observableArrayList(questions);
        fp_questions.setItems(questionlists);
    }

    public void proceedBtn() {
        if (fp_username.getText().isEmpty() || fp_questions.getValue() == null || forgetP_answer.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();
        } else {
            String query = "SELECT username, questions, answer FROM employee WHERE username = ? AND questions = ? AND answer = ?";
            connect = database.connectDB();
            try {
                prepare = connect.prepareStatement(query);
                prepare.setString(1, fp_username.getText());
                prepare.setString(2, fp_questions.getValue().toString());
                prepare.setString(3, forgetP_answer.getText());
                result = prepare.executeQuery();
                if (result.next()) {
                    switchingforgetPass2();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid Username or Answer");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                Platform.runLater(() -> {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Failed to login: " + e.getMessage());
                    errorAlert.showAndWait();
                });
                System.out.println("Error: " + e);
            } finally {
                try {
                    if (prepare != null)
                        prepare.close();
                    if (connect != null)
                        connect.close();
                } catch (SQLException e) {
                    System.out.println("Error closing resources: " + e.getMessage());
                }
            }
        }
    }

    // ----------Forgot password: change password-----------------------------
    public void switchingforgetPass2() {
        forgetpassWord2.setVisible(true);
        forgetpassWord1.setVisible(false);
    }

    public void changePasswordBtn() {
        if (new_password.getText().isEmpty() || Confirm_password.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();

        } else if (!new_password.getText().equals(Confirm_password.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Password does not match");
            alert.showAndWait();
        } else if (new_password.getText().length() < 8
                || !new_password.getText()
                        .matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Password must be at least 8 characters long\n contain at least one digit\n one lowercase letter\n one uppercase letter\n one special character.");
            alert.showAndWait();
        } else {
            String query = "UPDATE employee SET password = ? WHERE username = ?";
            connect = database.connectDB();
            try {
                prepare = connect.prepareStatement(query);
                prepare.setString(1, new_password.getText());
                prepare.setString(2, fp_username.getText());
                int affectedRows = prepare.executeUpdate();
                if (affectedRows > 0) {
                    Platform.runLater(() -> {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Password changed successfully");
                        successAlert.showAndWait();
                    });
                    forgetpassWord2.setVisible(false);
                    loginPage.setVisible(true);
                    // clearing fields
                    new_password.setText("");
                    Confirm_password.setText("");
                    fp_username.setText("");
                    forgetP_answer.setText("");
                    fp_questions.getSelectionModel().clearSelection();

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to update password");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to change password: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    public void backTologinForm() {
        loginPage.setVisible(true);
        forgetpassWord1.setVisible(false);

    }

    public void backToQuestionForm() {
        forgetpassWord1.setVisible(true);
        forgetpassWord2.setVisible(false);
    }

    // -----------------------------------------
    public void switchForm(ActionEvent event) {
        TranslateTransition slide = new TranslateTransition();
        if (event.getSource().equals(createBtn)) {
            slide.setNode(side_form);
            slide.setToX(300);
            slide.setDuration(Duration.seconds(0.5));
            slide.setOnFinished((ActionEvent e) -> {
                alreadylogged.setVisible(true);
                createBtn.setVisible(false);
                requestionList();

            });
            loginPage.setVisible(true);
            forgetpassWord2.setVisible(false);
            forgetpassWord1.setVisible(false);
            slide.play();

        } else if (event.getSource().equals(alreadylogged)) {
            slide.setNode(side_form);
            slide.setToX(0);
            slide.setDuration(Duration.seconds(0.5));
            slide.setOnFinished((ActionEvent e) -> {
                alreadylogged.setVisible(false);
                createBtn.setVisible(true);

            });
            slide.play();
        }
    }

    public void initialize(URL url, ResourceBundle rb) {
        // Initialize method
    }
}
