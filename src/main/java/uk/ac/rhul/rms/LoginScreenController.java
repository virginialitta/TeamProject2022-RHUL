package uk.ac.rhul.rms;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import uk.ac.rhul.screenmanager.ControlledScreen;
import uk.ac.rhul.screenmanager.ScreensController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.File;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * The screen controller for the login screen implementing the ControlledScreen Interface.
 *
 * @author Virginia Litta
 * @author Mohamed Yusuf
 *
 */

public class LoginScreenController implements ControlledScreen, Initializable {

  ScreensController screensController;

  /**
   * Sets the parent screen.
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

  @FXML
  private Pane pane;

  /**
   * Back button.
   *
   * @param event once back button pressed takes you back to start screen.
   */
  @FXML
  void backBtnPressed(ActionEvent event) {
    this.screensController.setScreen(Main.startScreenID);
  }


  /**
   * This method checks if there is another user logged in when someone is trying to log in.
   *
   * @param event if system is logged in a message will be printe d saying that this system is
   *        already logged in.
   */
  @FXML
  void login(ActionEvent event) {
    String username = usernameField.getText();
    String password = passwordField.getText();
    Security security = new Security(username, password);

    if (!(security.isSqlInjectionSafe(username) && security.isSqlInjectionSafe(password))) {
      displayError("I see what you did there, huh!");
      return;
    }


    // Grabbing values above this.

    try {
      ResultSet loginResult = DatabaseController.executeQuery(DatabaseConnection.getInstance(),
          "SELECT user_id, session_id FROM user_table WHERE user_name='" + username
              + "' AND password='" + security.getHashPassword() + "'");
      while (loginResult.next()) {
        Main.currentLoggedInUser = loginResult.getInt(1);
        if (!loginResult.getString(2).equals("NULL")) {
          displayError("Someone is already logged into this account.");
          return;
        }
      }
      System.out.println(Main.currentLoggedInUser);

      if (Main.currentLoggedInUser == 0) {
        displayError("Username or Password incorrect.");
        return;
      }

      Main.sessionId = security.getSessionID();

      DatabaseConnection.getInstance().createStatement()
          .execute("UPDATE user_table set session_id='" + Main.sessionId + "' WHERE user_id="
              + Main.currentLoggedInUser);

      Main.loginLoader();

    } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
      System.out.println(e.toString());
    }
  }

  void displayError(String error) {
    resultLabel.setText(error);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    File file = new File("src/main/resources/uk/ac/rhul/rms/media/login screen.png");
    Image image = new Image(file.toURI().toString());
    BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(1,1, true, true, false,false));
    Background background = new Background(backgroundImage);
    pane.setBackground(background);
  }
}


