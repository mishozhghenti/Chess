package com.company;

import com.whitehatgaming.UserInputFile;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import static com.company.constant.Constants.*;

public class Main extends Application {
    private Game game;
    private Group root;
    private TextField gameDataFileTexField;
    private Button loadGameDataButton;
    private Label status;


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(GAME_NAME);
        root = new Group();
        drawChessBoard();
        setUpControllerUI();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT + EXTRA_SPACE_FOR_CONTROLLING));
        primaryStage.sizeToScene();
        primaryStage.show();
    }


    private void setUpControllerUI() {
        Label fileNameLabel = new Label("Game Data File Name: ");
        fileNameLabel.setStyle("-fx-font-weight: bold");
        fileNameLabel.setLayoutY(HEIGHT);

        gameDataFileTexField = new TextField();
        gameDataFileTexField.setLayoutY(HEIGHT + 18);

        loadGameDataButton = new Button("Load Game");
        loadGameDataButton.setLayoutY(HEIGHT + 48);
        loadGameDataButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    game = new Game(new UserInputFile(gameDataFileTexField.getText()), root.getChildren());
                    status.setText("File opened");
                    // new Thread (GameThread)
                    new Thread(new GameThread()).start();
                } catch (Exception e) {
                    status.setText("File Reading Error :(");
                }
            }
        });
        Label statusLabel = new Label("Status: ");
        statusLabel.setStyle("-fx-font-weight: bold");
        statusLabel.setLayoutY(HEIGHT + 72);

        status = new Label("Please, load game data...");
        status.setLayoutY(HEIGHT + 72);
        status.setLayoutX(48);

        root.getChildren().addAll(fileNameLabel, gameDataFileTexField, loadGameDataButton, statusLabel, status);
    }


    /**
     * Just draws chess board
     */
    private void drawChessBoard() {
        double squareSize = WIDTH / 8;
        for (int i = 0; i < 8; i++) {
            boolean isBlack = true;
            if (i % 2 == 1) {
                isBlack = false;
            }
            for (int j = 0; j < 8; j++) {
                Rectangle currentCell = new Rectangle(i * squareSize, j * squareSize, squareSize, squareSize);
                Color currentColor;
                if (isBlack) {
                    currentColor = Color.BROWN;
                } else {
                    currentColor = Color.WHEAT;
                }
                currentCell.setFill(currentColor);
                root.getChildren().add(currentCell);
                isBlack = !isBlack;
            }
        }
    }


    private class GameThread implements Runnable {
        public void run() {
            while (!game.isGameFinished()) {
               // game.nextGameMove();
                try {
                    System.out.println("AAA");
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}