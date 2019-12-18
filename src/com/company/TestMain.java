package com.company;

import com.company.constant.Constants;
import com.company.model.Piece;
import com.whitehatgaming.UserInputFile;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import static com.company.constant.Constants.*;

public class TestMain extends JPanel {
    private JFrame frame;
    private Game game;
    private JLabel statusLabel;
    private JLabel[][] boardLabels;

    public TestMain() {
        frame = new JFrame("Chess");
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

        size.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"))); // TODO check on linux
                int result = fileChooser.showOpenDialog(TestMain.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    String selectedFileName = selectedFile.getAbsolutePath();
                    initGame(selectedFileName);
//                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                }
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
        statusLabel.setText("The Game had been started");
        updateLabel(game.getBoard());
        new Thread(new GameThread()).start();
    }


    private class GameThread implements Runnable {
        public void run() {
            while (!game.isGameFinished()) {
                game.nextGameMove();
                try {
                    updateLabel(game.getBoard());
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }

            statusLabel.setText("Game Finished: ");

            Thread.currentThread().interrupt();
        }

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
        statusLabel = new JLabel("Status...");
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

    private static void createAndShowGui() {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TestMain();
            }
        });
    }
}