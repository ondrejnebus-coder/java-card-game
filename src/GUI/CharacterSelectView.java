package GUI;

import GameControl.GameController;
import Utils.GameLogger;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.control.ContentDisplay;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class CharacterSelectView {

    private Stage stage;
    private GameController controller;
    private ImageView characterView;

    public CharacterSelectView(Stage stage, GameController controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public void show() {

        Image bgImage = new Image(getClass().getResource("/assets/PNG/panel_beigeLight.png").toExternalForm());
        ImageView bgView = new ImageView(bgImage);
        bgView.fitWidthProperty().bind(stage.widthProperty());
        bgView.fitHeightProperty().bind(stage.heightProperty());

        characterView = createAnimated("/assets/JPG/mag1.png");

        Button mageBtn = createButton("Mage");
        Button warriorBtn = createButton("Warrior");
        Button thiefBtn = createButton("Thief");

        mageBtn.setOnMouseEntered(e -> setCharacter("/assets/JPG/mag1.png"));
        warriorBtn.setOnMouseEntered(e -> setCharacter("/assets/JPG/warrior1.png"));
        thiefBtn.setOnMouseEntered(e -> setCharacter("/assets/JPG/thief1.png"));

        mageBtn.setOnAction(e -> {
            controller.selectCharacter("Mage");
            GameLogger.logInfo("Selected Mage");
            new GameView(stage, controller).show();
        });

        warriorBtn.setOnAction(e -> {
            controller.selectCharacter("Warrior");
            GameLogger.logInfo("Selected Warrior");
            new GameView(stage, controller).show();
        });

        thiefBtn.setOnAction(e -> {
            controller.selectCharacter("Thief");
            GameLogger.logInfo("Selected Thief");
            new GameView(stage, controller).show();
        });

        VBox buttonBox = new VBox(20, mageBtn, warriorBtn, thiefBtn);
        buttonBox.setAlignment(Pos.CENTER);

        HBox content = new HBox(100, characterView, buttonBox);
        content.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(bgView, content);

        Scene scene = new Scene(root, 800, 600);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
    }

    private void setCharacter(String path) {
        characterView.setImage(new Image(getClass().getResource(path).toExternalForm()));
    }

    private Button createButton(String text) {

        Image img = new Image(getClass().getResource("/assets/PNG/buttonLong_beige.png").toExternalForm());
        ImageView view = new ImageView(img);
        view.setFitWidth(200);
        view.setFitHeight(50);

        Button btn = new Button(text, view);
        btn.setContentDisplay(ContentDisplay.CENTER);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");

        return btn;
    }

    private ImageView createAnimated(String path) {

        Image image = new Image(getClass().getResource(path).toExternalForm());
        ImageView view = new ImageView(image);

        view.setFitWidth(250);
        view.setPreserveRatio(true);
        view.setTranslateY(0);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(400), e -> {
                    if (view.getScaleY() == 1.0) {
                        view.setScaleY(1.05);
                        view.setTranslateY(-5);
                    } else {
                        view.setScaleY(1.0);
                        view.setTranslateY(0);
                    }
                })
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        return view;
    }
}