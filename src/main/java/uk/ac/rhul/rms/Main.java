package uk.ac.rhul.rms;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uk.ac.rhul.screenmanager.ScreensController;


/**
 * A public Main class that starts up the application.
 *
 * @author Mohamed Yusuf
 *
 */
public class Main extends Application {

  // The ID and location for the fxml startScreen. Make sure to follow the id naming standard I have
  // used below.
  public static String startScreenID = "StartScreen";
  public static String startScreenFile =
      "/uk/ac/rhul/rms/StartScreen.fxml";
  public static String menuScreenID = "MenuScreen";
  public static String menuScreenFile = "/uk/ac/rhul/rms/MenuScreen.fxml";



  @Override
  public void start(Stage primaryStage) throws Exception {
    ScreensController mainScreenController = new ScreensController();
    mainScreenController.loadScreen(startScreenID, startScreenFile);
    mainScreenController.setScreen(startScreenID);
    


    Group root = new Group();
    root.getChildren().addAll(mainScreenController);
    Scene scene = new Scene(root, 960, 540);

    primaryStage.setScene(scene);
    primaryStage.show();

  }

  /**
   * A public static function that is run on startup.
   *
   * @param args The default argument requirement for public static void main functions.
   */
  public static void main(String[] args) {
    launch(args);
  }

}   
