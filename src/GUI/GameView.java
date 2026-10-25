package GUI;

import GameControl.GameController;
import Cards.Card;
import Characters.Thief;
import javafx.application.Platform;
import GameControl.GameState;
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
import java.util.LinkedList;

public class GameView {
    /**
     * Main game screen showing player, enemy and cards.
     * Enemy turn is executed on a background thread with a 1-second delay
     * to satisfy the multithreading requirement, then results are applied
     * back on the JavaFX Application Thread via Platform.runLater().
     */
    private Stage stage;
    private GameController controller;

    private HBox cardBox;
    private VBox playerStats;
    private VBox enemyStats;
    private VBox mainBox;
    private VBox enemyLog;
    private LinkedList<String> lastMoves = new LinkedList<>();

    /** Prevents the player from clicking cards while the enemy is "thinking". */
    private boolean enemyThinking = false;

    public GameView(Stage stage, GameController controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public void show() {

        Image bgImage = new Image(getClass().getResource("/assets/PNG/panel_beigeLight.png").toExternalForm());
        ImageView bgView = new ImageView(bgImage);
        bgView.fitWidthProperty().bind(stage.widthProperty());
        bgView.fitHeightProperty().bind(stage.heightProperty());

        ImageView playerView = createAnimated("/assets/JPG/" + controller.getPlayerType() + "1.png");
        ImageView enemyView  = createAnimated("/assets/JPG/" + controller.getEnemyType()  + "1.png");

        HBox characters = new HBox(150, playerView, enemyView);
        characters.setAlignment(Pos.CENTER);

        playerStats = new VBox(5);
        enemyStats  = new VBox(5);

        HBox stats = new HBox(100, playerStats, enemyStats);
        stats.setAlignment(Pos.CENTER);

        cardBox = new HBox(20);
        cardBox.setAlignment(Pos.CENTER);

        mainBox = new VBox(30, characters, stats, cardBox);
        mainBox.setAlignment(Pos.CENTER);

        enemyLog = new VBox(5);
        enemyLog.setAlignment(Pos.TOP_RIGHT);
        enemyLog.setPadding(new Insets(20, 20, 0, 0));
        enemyLog.setMaxWidth(250);

        Label logTitle = new Label("Enemy moves:");
        logTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        enemyLog.getChildren().add(logTitle);

        StackPane root = new StackPane(bgView, mainBox);
        StackPane.setAlignment(enemyLog, Pos.TOP_RIGHT);
        root.getChildren().add(enemyLog);

        controller.getPlayer().drawCard(3);
        controller.getEnemy().drawCard(3);

        update();

        Scene scene = new Scene(root, 800, 600);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
    }

    // -------------------------------------------------------------------------
    // Threaded enemy turn
    // -------------------------------------------------------------------------

    /**
     * Runs the enemy turn on a background thread after a 1-second delay,
     * then marshals the result back to the JavaFX thread.
     *
     * @param afterEnemy callback executed on the FX thread once the enemy turn is done
     */
    private void scheduleEnemyTurn(Runnable afterEnemy) {
        enemyThinking = true;
        updateCardInteractivity(false);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000); // 1-second delay before enemy acts
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }

            String enemyCardName = controller.enemyTurn();

            // Marshal back to JavaFX Application Thread
            Platform.runLater(() -> {
                enemyThinking = false;
                if (enemyCardName != null) {
                    addEnemyMove(enemyCardName);
                }
                afterEnemy.run();
            });
        });

        thread.setDaemon(true); // don't block JVM shutdown
        thread.setName("EnemyTurn-Thread");
        thread.start();
    }


    private void update() {

        playerStats.getChildren().setAll(
                createBar("HP",   controller.getPlayer().getHealth(), controller.getPlayer().getMaxHealth(), "green"),
                createBar("Mana", controller.getPlayer().getMana(),   controller.getPlayer().getMaxMana(),   "blue"),
                new Label("Gold: " + controller.getPlayer().getMoney())
        );

        enemyStats.getChildren().setAll(
                createBar("HP", controller.getEnemy().getHealth(), controller.getEnemy().getMaxHealth(), "green")
        );

        cardBox.getChildren().clear();

        Image btnImg = new Image(getClass().getResource("/assets/PNG/buttonLong_beige.png").toExternalForm());

        for (int i = 0; i < controller.getPlayer().getHand().size(); i++) {

            Card card = controller.getPlayer().getHand().get(i);

            ImageView view = new ImageView(btnImg);
            view.setFitWidth(220);
            view.setFitHeight(120);

            Label nameLabel = new Label(card.getName());
            nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: black;");

            Label infoLabel = new Label("lvl " + card.getLevel() + "  |  mana " + card.getManaCost());
            infoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: black;");

            VBox cardContent = new VBox(8, nameLabel, infoLabel);
            cardContent.setAlignment(Pos.CENTER);

            StackPane cardPane = new StackPane(view, cardContent);
            cardPane.setStyle("-fx-cursor: hand;");

            int index = i;

            cardPane.setOnMouseClicked(e -> {
                if (enemyThinking) return; // block input during enemy turn

                // 1. Player plays card
                controller.playPlayerCard(index);

                // 2. Check if enemy died immediately
                if (controller.isEnemyDead()) {
                    controller.rewardPlayer();
                    controller.saveState();
                    if (controller.isFinalEnemy()) {
                        showEndScreen(true);
                        return;
                    }
                    new ShopView(stage, controller).show();
                    return;
                }

                update(); // refresh UI after player's action

                // 3. Enemy responds after 1-second delay (background thread)
                scheduleEnemyTurn(() -> {
                    if (controller.isEnemyDead()) {
                        controller.rewardPlayer();
                        controller.saveState();
                        if (controller.isFinalEnemy()) {
                            showEndScreen(true);
                            return;
                        }
                        new ShopView(stage, controller).show();
                        return;
                    }

                    if (!controller.getPlayer().isAlive()) {
                        showEndScreen(false);
                        return;
                    }

                    update();
                    updateCardInteractivity(true);
                });
            });

            cardBox.getChildren().add(cardPane);
        }

        // --- Special ability button ---
        ImageView specialView = new ImageView(btnImg);
        specialView.setFitWidth(200);
        specialView.setFitHeight(50);

        String specialLabel;
        if (controller.isThief()) {
            specialLabel = "Special (passive - dodge: " + controller.getPlayer().getDodgeCharges() + ")";
        } else {
            specialLabel = "Special (" + controller.getSpecialCharges() + " left)";
        }

        Button specialBtn = new Button(specialLabel, specialView);
        specialBtn.setContentDisplay(ContentDisplay.CENTER);
        specialBtn.setDisable(controller.isThief() || controller.getSpecialCharges() == 0);
        specialBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
        specialBtn.setOnAction(e -> {
            if (enemyThinking) return;

            controller.useSpecial();

            if (controller.isEnemyDead()) {
                controller.rewardPlayer();
                controller.saveState();
                if (controller.isFinalEnemy()) {
                    showEndScreen(true);
                    return;
                }
                new ShopView(stage, controller).show();
                return;
            }

            update();

            // Enemy responds after delay
            scheduleEnemyTurn(() -> {
                if (controller.isEnemyDead()) {
                    controller.rewardPlayer();
                    controller.saveState();
                    if (controller.isFinalEnemy()) {
                        showEndScreen(true);
                        return;
                    }
                    new ShopView(stage, controller).show();
                    return;
                }

                if (!controller.getPlayer().isAlive()) {
                    showEndScreen(false);
                    return;
                }

                update();
                updateCardInteractivity(true);
            });
        });

        // --- Draw card button ---
        ImageView drawView = new ImageView(btnImg);
        drawView.setFitWidth(160);
        drawView.setFitHeight(50);

        Button drawBtn = new Button("Draw Card", drawView);
        drawBtn.setContentDisplay(ContentDisplay.CENTER);
        drawBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
        drawBtn.setOnAction(e -> {
            if (enemyThinking) return;
            controller.getPlayer().drawCard();
            update();
        });

        // --- Skip turn button ---
        ImageView skipView = new ImageView(btnImg);
        skipView.setFitWidth(160);
        skipView.setFitHeight(50);

        Button skipBtn = new Button("Skip Turn", skipView);
        skipBtn.setContentDisplay(ContentDisplay.CENTER);
        skipBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
        skipBtn.setOnAction(e -> {
            if (enemyThinking) return;

            scheduleEnemyTurn(() -> {
                if (!controller.getPlayer().isAlive()) {
                    showEndScreen(false);
                    return;
                }
                update();
                updateCardInteractivity(true);
            });
        });

        HBox actionButtons = new HBox(20, specialBtn, drawBtn, skipBtn);
        actionButtons.setAlignment(Pos.CENTER);

        mainBox.getChildren().setAll(
                mainBox.getChildren().get(0),
                mainBox.getChildren().get(1),
                cardBox,
                actionButtons
        );
    }

    /**
     * Enables or disables click interaction on all card panes currently in cardBox.
     * Used to block input while the enemy is thinking.
     */
    private void updateCardInteractivity(boolean enabled) {
        cardBox.getChildren().forEach(node -> node.setDisable(!enabled));
    }


    private void addEnemyMove(String cardName) {
        lastMoves.addFirst(cardName);
        if (lastMoves.size() > 3) lastMoves.removeLast();

        Label logTitle = new Label("Enemy moves:");
        logTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        enemyLog.getChildren().setAll(logTitle);

        for (int i = 0; i < lastMoves.size(); i++) {
            Label l = new Label((i + 1) + ". " + lastMoves.get(i));
            l.setStyle("-fx-font-size: 12px;");
            enemyLog.getChildren().add(l);
        }
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
        double endCap     = 12.0;
        double midWidth   = Math.max(0, (value / (double) max) * (totalWidth - endCap * 2));

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

    private ImageView createAnimated(String path) {

        Image image = new Image(getClass().getResource(path).toExternalForm());
        ImageView view = new ImageView(image);

        view.setFitWidth(180);
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

    private void showEndScreen(boolean win) {
        String message = win ? "Vyhral si!" : "Prehral si!";

        if (!win) {
            GameState.deleteSave(); // wipe save on loss
        }

        javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);

        alert.setTitle("Koniec hry");
        alert.setHeaderText(message);
        alert.setContentText("Chceš hrať znova?");

        javafx.scene.control.ButtonType yes =
                new javafx.scene.control.ButtonType("Hrať znovu");
        javafx.scene.control.ButtonType no =
                new javafx.scene.control.ButtonType("Ukončiť");

        alert.getButtonTypes().setAll(yes, no);

        alert.showAndWait().ifPresent(response -> {
            if (response == yes) {
                new MainMenuView(stage).show();
            } else {
                stage.close();
            }
        });
    }
}