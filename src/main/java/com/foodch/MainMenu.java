package com.foodch;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Spinner;

import java.io.IOException;



public class MainMenu extends Application {
    @FXML
    private Spinner<Integer> WorldX;
    @FXML
    private Spinner<Integer> WorldY;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainMenu.class.getResource("menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),300,640);
        stage.setMaxHeight(640);
        stage.setMaxWidth(320);
        stage.setTitle("Menu");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}