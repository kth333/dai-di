package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private Button playbutton;

    @FXML
    private Button playbutton1;

    @FXML
    private Button quitbutton;

    @FXML
    void switchToGameScene(ActionEvent event) throws IOException {
        Stage stage = (Stage)playbutton1.getScene().getWindow();
        // these two of them return the same stage
        // Swap screen
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("GameScreen.fxml"))));

//        Parent root = FXMLLoader.load(getClass().getResource("/Users/zernchng/Documents/JavaFX/DaiDiV1/src/GameScreen.fxml"));
//        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
        stage.show();
    }
}
