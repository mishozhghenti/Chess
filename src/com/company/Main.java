package com.company;

import com.company.model.Piece;
import com.whitehatgaming.UserInputFile;
import javafx.application.Application;
import javafx.application.Platform;
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
    private Label[][] boardLabels = new Label[8][8];


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(GAME_NAME);
        root = new Group();


        drawChessBoard();
        setUpControllerUI();
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    private void updateUICHESS(Piece[][] kk) {
//        status.setText(String.valueOf(System.currentTimeMillis()));
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAa");
//        boardLabels[4][4].setText("ASDSAD");
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
                    game = new Game(new UserInputFile(gameDataFileTexField.getText()));
                    status.setText("File opened");

                    Piece[][] board = game.getBoard();
                    displayPieces(board);
//                     new Thread (GameThread)
//                    Thread.sleep(1000);
//                    new Thread(new GameThread()).start();

                    Platform.runLater(new Runnable() {
                                          @Override
                                          public void run() {
                                              while (!game.isGameFinished()) {
                                                  game.nextGameMove();
                                                  try {
                                                      Piece[][] board = game.getBoard();
//                    updatePiecesDisplay(board);
                                                      //       status.setText(String.valueOf(System.currentTimeMillis()));

                                                      Thread.sleep(1000);
                                                  } catch (InterruptedException e) {
                                                      e.printStackTrace();
                                                  }
                                              }
                                              Thread.currentThread().interrupt();
                                          }
                                      }
                    );
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

    private void displayPieces(Piece[][] board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece current = board[i][j];
                locateLabel((current == null) ? " " : current.getPieceText(), i, j);
//                System.out.print(((current == null) ? " " : current.getPieceText()) + " ");
            }
//            System.out.println("");
        }
    }

    private void locateLabel(String s, int i, int j) {
        Label currentLabel = new Label(s);
        currentLabel.setLayoutX(i * WIDTH / 8 + 24);
        currentLabel.setLayoutY(j * HEIGHT / 8 + 24);
        boardLabels[i][j] = currentLabel;
        this.root.getChildren().add(currentLabel);
    }


    /**
     * Just draws chess board
     */
    private void drawChessBoard() {
        double squareSize = (double) WIDTH / 8;
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
                game.nextGameMove();
                try {
                    Piece[][] board = game.getBoard();
//                    updatePiecesDisplay(board);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Thread.currentThread().interrupt();
        }

        private void updatePiecesDisplay(Piece[][] board) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Piece current = board[i][j];
                    updateLabel((current == null) ? " " : current.getPieceText(), i, j);
                }
            }
        }

        private void updateLabel(String s, int i, int j) {
            boardLabels[i][j].setText(s);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}