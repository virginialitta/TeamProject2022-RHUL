package uk.ac.rhul.rms;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import uk.ac.rhul.screenmanager.ControlledScreen;
import uk.ac.rhul.screenmanager.ScreensController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The screen controller for the login screen implementing the ControlledScreen Interface.
 *
 * @author Virginia Litta
 * @author Mohamed Yusuf
 *
 */

public class LoginScreenController implements ControlledScreen {

    ScreensController screensController;

    /**
     *
     * @param screenParent The parent ScreensController of 'this' screen.
     */
    @Override
    public void setScreenParent(ScreensController screenParent) {
        this.screensController = screenParent;
    }

    /**
     * Below methods contains buttons, text Fields and password fields for login screen.
     */
    @FXML
    private Button backBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label resultLabel;

    /**
     *
     * @param event once back button pressed takes you back to start screen.
     */
    @FXML
    void backBtnPressed(ActionEvent event) {
        this.screensController.setScreen(Main.startScreenID);
    }

    /**
     *
     * @param event checks security credentials and logs user in.
     * @throws SQLException
     */
    @FXML
    void bypass(ActionEvent event) throws SQLException{
        String id = event.getSource().toString().split("=")[1].split(",")[0].split("b")[1];
        // This is my favorite line of code. Classic security through obscurity xD.

        Main.currentLoggedInUser = Integer.parseInt(id);

        DatabaseConnection.getInstance().createStatement().execute("UPDATE user_table set session_id="
                + Main.currentLoggedInUser + " WHERE user_id=" + Main.currentLoggedInUser); // this will be replaced by hashcode.

        Main.sessionId = String.valueOf(Main.currentLoggedInUser);

        Main.loginLoader();
    }


    /**
     * This method checks if there is another user logged in when someone is trying to log in.
     * @param event if system is logged in a message will be printe d saying that this system is already logged in.
     */
    @FXML
    void login(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        System.out.println("username: " + username);
        System.out.println("password: " + password);

        if ((Main.currentLoggedInUser != 0) || (Main.sessionId != null)) {
            System.out.println("this system is already logged into to a different user.");
            return;
        }

        try {
            ResultSet loginResult = DatabaseController.executeQuery(DatabaseConnection.getInstance(),
                    "SELECT user_id FROM user_table WHERE user_name='" + username + "' AND password='" + password + "'");
            while (loginResult.next()) {
                Main.currentLoggedInUser  = loginResult.getInt(1);
            }

            DatabaseConnection.getInstance().createStatement().execute("UPDATE user_table set session_id="
                    + Main.currentLoggedInUser + " WHERE user_id=" + Main.currentLoggedInUser); // this will be replaced by hashcode.

            Main.sessionId = String.valueOf(Main.currentLoggedInUser);

            Main.loginLoader();

        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
}


