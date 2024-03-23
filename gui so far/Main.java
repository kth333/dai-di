import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import static javafx.fxml.FXMLLoader.load;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            Parent root = load(getClass().getResource("HomeScreen.fxml"));
            stage.setTitle("Dai Di Game");
            stage.setResizable(false);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

//    public void logout(Stage stage){
//
//    }

}