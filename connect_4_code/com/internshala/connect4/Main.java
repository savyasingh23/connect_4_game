package com.internshala.connect4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

   private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
    GridPane rootGridPane = loader.load();

    controller = loader.getController();
    controller.createPlayground();

    MenuBar m = createMenu();
    m.prefWidthProperty().bind(primaryStage.widthProperty());
    Pane menuPane = (Pane) rootGridPane.getChildren().get(0);
    menuPane.getChildren().add(m);
    Scene scene = new Scene(rootGridPane);

    primaryStage.setScene(scene);
    primaryStage.setTitle("Connect Four game");
    primaryStage.setResizable(false);
    primaryStage.show();
    }
    private MenuBar createMenu() {
     Menu file = new Menu("File");

     MenuItem newgame = new MenuItem("New game");
     newgame.setOnAction(actionEvent -> controller.resetGame());

     MenuItem resetgame = new MenuItem("Reset game");
     resetgame.setOnAction(actionEvent -> controller.resetGame());

     SeparatorMenuItem s = new SeparatorMenuItem();

     MenuItem exitgame = new MenuItem("Exit game");
     exitgame.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
       exitGame();
      }
     });
     file.getItems().addAll(newgame,resetgame,s,exitgame);

     Menu help = new Menu("Help");

     MenuItem aboutgame = new MenuItem("About connect4");
     aboutgame.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
       aboutconnect4();
      }
     });

     SeparatorMenuItem s1 = new SeparatorMenuItem();
     MenuItem aboutme = new MenuItem("About Me");
     aboutme.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
       aboutMe();
      }
     });
     help.getItems().addAll(aboutgame,s1,aboutme);


     MenuBar mb = new MenuBar();
     mb.getMenus().addAll(file,help);
     return mb;
    }

 private void aboutMe() {
  Alert alert = new Alert(Alert.AlertType.INFORMATION);
  alert.setTitle("About the Developer");
  alert.setHeaderText("Savya Singh");
  alert.setContentText("I loved to play Connect 4 game when i was a child." +
          "I developed this game when I was 19 years old" +
          " and was pursuing my BTECH degree.");
  alert.show();

 }

 private void aboutconnect4() {
     Alert alert = new Alert(Alert.AlertType.INFORMATION);
     alert.setTitle("About Connect four Game");
     alert.setHeaderText("How to Play?");
     alert.setContentText("Connect Four is a two-player connection game in which the players first choose a " +
             "color and then take turns dropping colored discs from the top in" +
             "to a seven-column, six-row vertically suspended grid. " +
             "The pieces fall straight down, occupying the next available space within " +
             "the column. The objective of the game is to be the first to form a " +
             "horizontal, vertical, or diagonal line of four of one's own discs. " +
             "Connect Four is a solved game. The first player can always win by playing " +
             "the right moves.");
     alert.show();
 }

 private void exitGame() {
     Platform.exit();
     System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
    }
