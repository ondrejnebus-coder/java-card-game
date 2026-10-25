package GUI;

import GameControl.GameController;
import GameControl.GameState;
import Characters.Mage;
import Characters.Warrior;
import Characters.Thief;
import Utils.GameLogger;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.control.ContentDisplay;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class MainMenuView {

    private Stage stage;

    public MainMenuView(Stage stage) {
        this.stage = stage;
    }

    public void show() {

        GameLogger.logInfo("Main menu opened");

        Image bgImage = new Image(getClass().getResource("/assets/PNG/panel_beigeLight.png").toExternalForm());
        ImageView bgView = new ImageView(bgImage);
        bgView.fitWidthProperty().bind(stage.widthProperty());
        bgView.fitHeightProperty().bind(stage.heightProperty());

        Image startImg = new Image(getClass().getResource("/assets/PNG/buttonLong_beige.png").toExternalForm());
        ImageView startView = new ImageView(startImg);
        startView.setFitWidth(200);
        startView.setFitHeight(50);

        Button startBtn = new Button("Start", startView);
        startBtn.setContentDisplay(ContentDisplay.CENTER);
        startBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16px;");

        Image exitImg = new Image(getClass().getResource("/assets/PNG/buttonLong_beige.png").toExternalForm());
        ImageView exitView = new ImageView(exitImg);
        exitView.setFitWidth(200);
        exitView.setFitHeight(50);

        Button exitBtn = new Button("Exit", exitView);
        exitBtn.setContentDisplay(ContentDisplay.CENTER);
        exitBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16px;");

        startBtn.setOnAction(e -> {
            GameLogger.logInfo("Start button clicked");
            GameController controller = new GameController();
            CharacterSelectView select = new CharacterSelectView(stage, controller);
            select.show();
        });

        exitBtn.setOnAction(e -> {
            GameLogger.logInfo("Exit button clicked");
            stage.close();
        });

        // --- Continue button (only shown if save exists) ---
        GameState savedState = GameState.load();

        VBox menuBox;

        if (savedState != null) {

            Image contImg = new Image(getClass().getResource("/assets/PNG/buttonLong_beige.png").toExternalForm());
            ImageView contView = new ImageView(contImg);
            contView.setFitWidth(200);
            contView.setFitHeight(50);

            Button contBtn = new Button("Pokračovať", contView);
            contBtn.setContentDisplay(ContentDisplay.CENTER);
            contBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16px;");

            contBtn.setOnAction(e -> {
                GameLogger.logInfo("Continue button clicked");
                GameController controller = new GameController();
                controller.loadFromState(savedState);
                if (savedState.isInShop()) {
                    new ShopView(stage, controller).show();
                } else {
                    new GameView(stage, controller).show();
                }
            });

            menuBox = new VBox(20, startBtn, contBtn, exitBtn);

        } else {
            menuBox = new VBox(20, startBtn, exitBtn);
        }

        menuBox.setAlignment(Pos.CENTER);

        ImageView mage = createAnimated("/assets/PNG/mag1.png");
        ImageView necro = createAnimated("/assets/PNG/necromancer1.png");

        HBox content = new HBox(50, mage, menuBox, necro);
        content.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(bgView, content);

        Scene scene = new Scene(root, 800, 600);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setTitle("Game Menu");
        stage.show();
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