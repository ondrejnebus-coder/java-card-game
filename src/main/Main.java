
package main;

import GUI.MainMenuView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainMenuView menu = new MainMenuView(primaryStage);
        menu.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}