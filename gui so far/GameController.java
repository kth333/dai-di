package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class GameController {

    @FXML
    private ImageView P3C1;

    @FXML
    private ImageView P3C10;

    @FXML
    private ImageView P3C11;

    @FXML
    private ImageView P3C12;

    @FXML
    private ImageView P3C13;

    @FXML
    private ImageView P3C2;

    @FXML
    private ImageView P3C3;

    @FXML
    private ImageView P3C4;

    @FXML
    private ImageView P3C5;

    @FXML
    private ImageView P3C6;

    @FXML
    private ImageView P3C7;

    @FXML
    private ImageView P3C8;

    @FXML
    private ImageView P3C9;

    @FXML
    private Button homebutton;

    @FXML
    void switchToHomeScene(ActionEvent event) throws IOException {
        Stage stage = (Stage)homebutton.getScene().getWindow();
        // these two of them return the same stage
        // Swap screen
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("HomeScreen.fxml"))));

//        Parent root = FXMLLoader.load(getClass().getResource("/Users/zernchng/Documents/JavaFX/DaiDiV1/src/GameScreen.fxml"));
//        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
        stage.show();
    }

}
