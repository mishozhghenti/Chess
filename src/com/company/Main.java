package com.company;

import com.company.constant.Constants;
import com.company.model.Piece;
import com.whitehatgaming.UserInputFile;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;

import static com.company.constant.Constants.*;

public class Main extends JPanel {
    private JFrame frame;
    private Game game;
    private JLabel statusLabel;
    private JLabel[][] boardLabels;
    private Thread currentGameThread;

    public Main() {
        frame = new JFrame(GAME_TITLE);
        boardLabels = new JLabel[8][8];
        setUpSize();
        createStatusBar();
        addMenuBar();
        setUpPieces();
        setUpFrame();
    }

    private void setUpPieces() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardLabels[i][j] = new JLabel(" ");
                boardLabels[i][j].setBounds(i * SQUARE_SIZE + 23, j * SQUARE_SIZE + 23, 20, 20);
                this.frame.add(boardLabels[i][j]);
            }
        }
    }

    private void updateLabel(Piece[][] pieces) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece current = pieces[i][j];
                boardLabels[i][j].setText((current != null) ? current.getPieceText() : " ");
            }
        }
    }

    private void setUpFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.setResizable(false);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setVisible(true);
    }

    private void addMenuBar() {
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem size = new JMenuItem("Open File");

        size.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(Main.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                String selectedFileName = selectedFile.getAbsolutePath();
                initGame(selectedFileName);
            }
        });
        menu.add(size);
        menubar.add(menu);
        frame.setJMenuBar(menubar);
    }

    private void initGame(String selectedFileName) {
        try {
            game = new Game(new UserInputFile(selectedFileName));
            startGame();
        } catch (FileNotFoundException e) {
            statusLabel.setText("File Not Found...");
        } catch (Exception e) {
            statusLabel.setText("Error");
        }
    }

    private void startGame() {
        statusLabel.setText("The Game has been started");
        updateLabel(game.getBoard());

        try{
            if (currentGameThread!=null){
                currentGameThread.interrupt();
            }
        }catch (Exception ignored){
        }

        currentGameThread = new Thread(new GameThread());
        currentGameThread.start();
    }

    private void setUpSize() {
        this.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        this.setMinimumSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        this.setMaximumSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
    }

    private void createStatusBar() {
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        frame.add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 16));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel = new JLabel("Please, Load The Game Data File...");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawChessBoard(g);
    }

    private void drawChessBoard(Graphics g) {
        for (int i = 0; i < 8; i++) {
            boolean isBlack = true;
            if (i % 2 == 1) {
                isBlack = false;
            }
            for (int j = 0; j < 8; j++) {
                if (!isBlack) {
                    g.setColor(Color.decode(WHITE_HEX));
                } else {
                    g.setColor(Color.decode(BLACK_HEX));
                }
                g.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                isBlack = !isBlack;
            }
        }
    }

    private class GameThread implements Runnable {
        public void run() {
            while (!game.isGameFinished()) {
                game.nextGameMove();
                try {
                    Thread.sleep(DELAY);
                    updateLabel(game.getBoard());
                } catch (Exception e) {
                   // e.printStackTrace();
                    break;
                }
            }
            statusLabel.setText("Game Finished: " + game.getGameStatus());
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}
