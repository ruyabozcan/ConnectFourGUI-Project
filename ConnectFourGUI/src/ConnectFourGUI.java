package com.company;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


// RUYA BOZCAN
// COMPUTER ENGINEERING
// ABU


public class ConnectFourGUI {

    public static final int ROWS = ConnectFour.ROWS;
    public static final int COLUMNS = ConnectFour.COLUMNS;
    public static final Color COMPUTER = ConnectFour.COMPUTER;
    public static final Color HUMAN = ConnectFour.HUMAN;
    public static final Color NONE = ConnectFour.NONE;
    public static final Color BOARD_COLOR = Color.BLUE;

    public static void showGUI(final Color[][] board,
                               final Color firstPlayer,
                               final int depth) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ConnectFourGUI gui =
                        new ConnectFourGUI(board, firstPlayer, depth);
                gui.startGame();
            }
        });
    }

    private class BoardPanel extends JPanel {

        public BoardPanel() {
            setBackground(BOARD_COLOR);
        }

        public int getRowHeight() {
            return getHeight() / ConnectFour.ROWS;
        }

        public int getColumnWidth() {
            return getWidth() / ConnectFour.COLUMNS;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            int rowHeight = getRowHeight();
            int colWidth = getColumnWidth();
            int rowOffset = rowHeight / 8;
            int colOffset = colWidth / 8;

            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLUMNS; col++) {
                    g.setColor(board[ROWS - row - 1][col]);
                    g.fillOval(col * colWidth + colOffset,
                            row * rowHeight + rowOffset,
                            colWidth - 2 * colOffset,
                            rowHeight - 2 * rowOffset);
                }
            }
        }
    }

    private Color[][] board;
    private Color currentPlayer;
    private int depth;

    private final JFrame boardFrame;
    private final BoardPanel boardPanel;
    private final JLabel statusLabel;

    private void doMouseClick(int x, int y) {

        int column = x / boardPanel.getColumnWidth();

        if (currentPlayer == HUMAN) {
            if (ConnectFour.legal(board, column)) {
                dropPiece(column);
            } else {
                System.out.println("Human attempted illegal move at column " + column);
            }
        } else {
            System.out.println("Ignoring click on computer's turn");
        }

    }

    private void dropPiece(int column) {

        ConnectFour.drop(board, currentPlayer, column);
        currentPlayer = ConnectFour.opposite(currentPlayer);
        boardFrame.repaint();

        checkForWin();
    }

    private void computerTurn() {

        if (currentPlayer == COMPUTER) {

            SwingWorker<Integer, ?> worker = new SwingWorker<Integer, Object>() {
                @Override
                public Integer doInBackground() {

                    Color[][] boardCopy = new Color[ROWS][COLUMNS];
                    for (int i = 0; i < board.length; i++) {
                        boardCopy[i] = java.util.Arrays.copyOf(board[i], board[i].length);
                    }
                    return ConnectFour.bestMoveForComputer(boardCopy, depth);
                }

                @Override
                protected void done() {
                    try {
                        int column = get();
                        if (ConnectFour.legal(board, column)) {
                            dropPiece(column);
                        } else {
                            System.out.println("Computer attempted illegal move at column " + column);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            worker.execute();
        } else {
            System.out.println("Ignoring attempted computer play on human's turn.");
        }
    }

    private void checkForWin() {

        statusLabel.setText("Checking for win...");

        SwingWorker<Color, ?> worker = new SwingWorker<Color, Object>() {
            @Override
            public Color doInBackground() {
                return ConnectFour.winner(board);
            }

            @Override
            protected void done() {
                Color winner = null;
                try {
                    winner = get();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                boolean gameOver = true;
                if (winner == HUMAN) {
                    statusLabel.setText("Game over: Human Wins!");
                    JOptionPane.showMessageDialog(null, "Human Wins!", "Game Over",
                            JOptionPane.INFORMATION_MESSAGE);
                } else if (winner == COMPUTER) {
                    statusLabel.setText("Game over: Computer Wins!");
                    JOptionPane.showMessageDialog(null, "Computer Wins!", "Game Over",
                            JOptionPane.INFORMATION_MESSAGE);
                } else if (ConnectFour.full(board)) {
                    statusLabel.setText("Game over: Draw");
                    JOptionPane.showMessageDialog(null, "Draw Game!", "Game Over",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    gameOver = false;
                    updateStatusLabel();
                }

                if (gameOver) System.exit(0);
                else if (currentPlayer == COMPUTER) computerTurn();
            }
        };
        worker.execute();
    }

    private void updateStatusLabel() {
        if (currentPlayer == HUMAN) {
            statusLabel.setText("Human player's turn");
        } else if (currentPlayer == COMPUTER) {
            statusLabel.setText("Computer player's turn");
        } else {
            statusLabel.setText("UNKNOWN STATUS");
        }
    }

    private ConnectFourGUI(Color[][] board, Color player, int depth) {
        this.board = board;
        this.currentPlayer = player;
        this.depth = depth;
        boardFrame = new JFrame();
        boardFrame.setTitle("Connect Four");

        boardPanel = new BoardPanel();
        boardPanel.setPreferredSize(new Dimension(700, 600));
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                doMouseClick(x, y);
            }
        });

        JPanel statusPanel = new JPanel();
        statusLabel = new JLabel();
        statusPanel.add(statusLabel);
        updateStatusLabel();

        boardFrame.add(boardPanel, BorderLayout.CENTER);
        boardFrame.add(statusPanel, BorderLayout.PAGE_END);

        boardFrame.pack();
        boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        boardFrame.setLocationRelativeTo(null);
        boardFrame.setVisible(true);
    }
    private void startGame() {
        checkForWin();
    }
}


// RUYA BOZCAN
// COMPUTER ENGINEERING
// ABU
