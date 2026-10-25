package GUI;

import GameControl.GameController;
import Shop.Shop;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.control.ContentDisplay;
import javafx.animation.*;
import javafx.util.Duration;

public class ShopView {

    private Stage stage;
    private GameController controller;

    public ShopView(Stage stage, GameController controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public void show() {

        Shop shop = new Shop();

        Image bgImage = new Image(getClass().getResource("/assets/PNG/panel_beigeLight.png").toExternalForm());
        ImageView bgView = new ImageView(bgImage);
        bgView.fitWidthProperty().bind(stage.widthProperty());
        bgView.fitHeightProperty().bind(stage.heightProperty());

        ImageView playerView = createAnimated("/assets/JPG/" + controller.getPlayerType() + "1.png");

        VBox statsBox = new VBox(8,
                createBar("HP", controller.getPlayer().getHealth(), controller.getPlayer().getMaxHealth(), "green"),
                createBar("Mana", controller.getPlayer().getMana(), controller.getPlayer().getMaxMana(), "blue")
        );
        statsBox.setAlignment(Pos.CENTER_LEFT);

        VBox left = new VBox(20, playerView, statsBox);
        left.setAlignment(Pos.CENTER);
        left.setPadding(new Insets(0, 0, 0, 80));

        Label money = new Label();
        Label error = new Label();
        error.setStyle("-fx-text-fill: red;");

        Button attack = createButton("Attack (50)");
        Button heal = createButton("Heal (50)");
        Button mana = createButton("Mana (50)");
        Button ultimate = createButton("Ultimate (50)");
        Button character = createButton("Character (100)");
        Button buySpecial = createButton("Buy Special (70)");
        Button cont = createButton("Continue");

        Runnable refresh = () -> {
            money.setText("Gold: " + controller.getPlayer().getMoney());
            controller.saveStateInShop(); // save after every action in shop
        };

        // save immediately when shop opens
        controller.saveStateInShop();

        attack.setOnAction(e -> {
            try { shop.upgradeCardType("Attack", controller.getPlayer()); error.setText(""); }
            catch (Exception ex) { error.setText("Not enough gold!"); }
            refresh.run();
        });

        heal.setOnAction(e -> {
            try { shop.upgradeCardType("Heal", controller.getPlayer()); error.setText(""); }
            catch (Exception ex) { error.setText("Not enough gold!"); }
            refresh.run();
        });

        mana.setOnAction(e -> {
            try { shop.upgradeCardType("Mana", controller.getPlayer()); error.setText(""); }
            catch (Exception ex) { error.setText("Not enough gold!"); }
            refresh.run();
        });

        ultimate.setOnAction(e -> {
            try { shop.upgradeCardType("Ultimate", controller.getPlayer()); error.setText(""); }
            catch (Exception ex) { error.setText("Not enough gold!"); }
            refresh.run();
        });

        character.setOnAction(e -> {
            try { shop.buyUpgrade(controller.getPlayer(), controller.getPlayer()); error.setText(""); }
            catch (Exception ex) { error.setText("Not enough gold!"); }
            refresh.run();
        });

        buySpecial.setOnAction(e -> {
            if (controller.buySpecialCharge()) {
                error.setText("");
            } else {
                error.setText("Not enough gold!");
            }
            refresh.run();
        });

        cont.setOnAction(e -> {
            controller.nextEnemy();
            controller.getPlayer().getHand().clear();
            new GameView(stage, controller).show();
        });

        VBox box = new VBox(10, money, attack, heal, mana, ultimate, character, buySpecial, error);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(0, 80, 0, 0));

        HBox center = new HBox(left, box);
        center.setAlignment(Pos.CENTER);
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(box, Priority.ALWAYS);

        VBox contBox = new VBox(cont);
        contBox.setAlignment(Pos.CENTER);
        contBox.setPadding(new Insets(0, 0, 20, 0));

        BorderPane layout = new BorderPane();
        layout.setCenter(center);
        layout.setBottom(contBox);

        StackPane root = new StackPane(bgView, layout);

        Scene scene = new Scene(root, 800, 600);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");

        refresh.run();
    }

    private HBox createBar(String labelText, int value, int max, String color) {

        Label label = new Label(labelText + ": " + value);

        Image leftImg, midImg, rightImg;

        if (color.equals("blue")) {
            leftImg  = new Image(getClass().getResource("/assets/PNG/barBlue_horizontalLeft.png").toExternalForm());
            midImg   = new Image(getClass().getResource("/assets/PNG/barBlue_horizontalBlue.png").toExternalForm());
            rightImg = new Image(getClass().getResource("/assets/PNG/barBlue_horizontalRight.png").toExternalForm());
        } else {
            leftImg  = new Image(getClass().getResource("/assets/PNG/barGreen_horizontalLeft.png").toExternalForm());
            midImg   = new Image(getClass().getResource("/assets/PNG/barGreen_horizontalMid.png").toExternalForm());
            rightImg = new Image(getClass().getResource("/assets/PNG/barGreen_horizontalRight.png").toExternalForm());
        }

        double totalWidth = 150.0;
        double endCap = 12.0;
        double midWidth = Math.max(0, (value / (double) max) * (totalWidth - endCap * 2));

        ImageView leftView = new ImageView(leftImg);
        leftView.setFitWidth(endCap);
        leftView.setFitHeight(16);
        leftView.setPreserveRatio(false);

        ImageView midView = new ImageView(midImg);
        midView.setFitWidth(midWidth);
        midView.setFitHeight(16);
        midView.setPreserveRatio(false);

        ImageView rightView = new ImageView(rightImg);
        rightView.setFitWidth(endCap);
        rightView.setFitHeight(16);
        rightView.setPreserveRatio(false);

        Region background = new Region();
        background.setPrefSize(totalWidth, 16);
        background.setStyle("-fx-background-color: #555555; -fx-background-radius: 4;");

        HBox foreground = new HBox(0, leftView, midView, rightView);

        StackPane barStack = new StackPane(background, foreground);
        barStack.setAlignment(Pos.CENTER_LEFT);

        HBox box = new HBox(10, label, barStack);
        box.setAlignment(Pos.CENTER_LEFT);

        return box;
    }

    private Button createButton(String text) {

        Image img = new Image(getClass().getResource("/assets/PNG/buttonLong_beige.png").toExternalForm());
        ImageView view = new ImageView(img);
        view.setFitWidth(180);
        view.setFitHeight(45);

        Button btn = new Button(text, view);
        btn.setContentDisplay(ContentDisplay.CENTER);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");

        return btn;
    }

    private ImageView createAnimated(String path) {

        Image image = new Image(getClass().getResource(path).toExternalForm());
        ImageView view = new ImageView(image);

        view.setFitWidth(500);
        view.setPreserveRatio(true);

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