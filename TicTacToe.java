import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.io.*;
import java.util.Scanner;

public class TicTacToe extends JPanel implements ActionListener {

    // Logic variables
    boolean playerX = true;
    boolean gameDone = false;
    int winner = -1;
    int player1wins = 0, player2wins = 0;
    int[][] board = new int[3][3];

    // Paint variables
    int lineWidth = 5;
    int lineLength = 270;
    int x = 15, y = 100; // location of the first line
    int offset = 95; // square width
    int selX = -1, selY = -1;

    // Colors
    Color pink = new Color(0xffc0cb);
    Color blue = new Color(0xdaf0ff);
    Color black = new Color(0x000000);

    //Components
    JButton jButton, resetButton;

    public TicTacToe() {
        Dimension size = new Dimension(420, 300);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        jButton = new JButton("Play Again");
        jButton.addActionListener(this);
        add(jButton);
        jButton.setVisible(false);

        resetButton = new JButton("Reset Scoreboard");
        resetButton.addActionListener(e -> resetScoreboard());
        add(resetButton);

        addMouseListener(new XOListener());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Naru's Treats (Tic Tac Toe)");
        TicTacToe tPanel = new TicTacToe();
        frame.add(tPanel);

        frame.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                try {
                    File file = new File("score.txt");
                    Scanner sc = new Scanner(file);
                    tPanel.setPlayer1Wins(Integer.parseInt(sc.nextLine()));
                    tPanel.setPlayer2Wins(Integer.parseInt(sc.nextLine()));
                    sc.close();
                } catch (IOException io) {
                 
                }
            }

            public void windowClosing(WindowEvent e) {
                try {
                    PrintWriter pw = new PrintWriter("score.txt");
                    pw.write(tPanel.player1wins + "\n");
                    pw.write(tPanel.player2wins + "\n");
                    pw.close();
                } catch (FileNotFoundException fe) {
                    
                }
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        resetGame();
    }

    public void resetGame() {
        playerX = true;
        winner = -1;
        gameDone = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = 0;
            }
        }
        jButton.setVisible(false);
        repaint();
    }

    public void resetScoreboard() {
        player1wins = 0;
        player2wins = 0;
        try {
            PrintWriter pw = new PrintWriter("score.txt");
            pw.write("0\n0\n");
            pw.close();
        } catch (FileNotFoundException e) {
            
        }
        repaint();
    }

    public void setPlayer1Wins(int a) {
        player1wins = a;
    }

    public void setPlayer2Wins(int a) {
        player2wins = a;
    }

    public void paintComponent(Graphics page) {
        super.paintComponent(page);
        drawBoard(page);
        drawUI(page);
        drawGame(page);
    }

    public void drawBoard(Graphics page) {
        setBackground(blue);
        page.setColor(black);
        page.fillRoundRect(x, y, lineLength, lineWidth, 5, 30);
        page.fillRoundRect(x, y + offset, lineLength, lineWidth, 5, 30);
        page.fillRoundRect(y, x, lineWidth, lineLength, 30, 5);
        page.fillRoundRect(y + offset, x, lineWidth, lineLength, 30, 5);
    }

    public void drawUI(Graphics page) {
        //Color and font
        page.setColor(pink);
        page.fillRect(300, 0, 120, 300);
        Font font = new Font("Serif", Font.BOLD, 20);
        page.setFont(font);
    
        //Scoreboard
        page.setColor(black);
        page.drawString("Scoreboard", 312, 110);
        page.drawString(": " + player1wins, 365, 140);  
        page.drawString(": " + player2wins, 365, 175);  
    
        //X score
        ImageIcon xIcon = new ImageIcon("pretzel40.png");
        Image xImage = xIcon.getImage();
        page.drawImage(xImage, 320, 115 , null);  
    
        //O score
        ImageIcon oIcon = new ImageIcon("donutwc.png"); 
        Image oImage = oIcon.getImage();
        page.drawImage(oImage, 312, 142 , null);  
    
        //Draw winner or player's turn
        page.setColor(black);
        Font font1 = new Font("Courier", Font.BOLD, 14);
        page.setFont(font1);
    
        if (gameDone) {
            //Show winner
            if (winner == 1) {
                page.drawString("The winner is", 310, 220);  
                page.drawImage(xImage, 340, 230, null);
            } else if (winner == 2) {
                page.drawString("The winner is", 310, 220);  
                page.drawImage(oImage, 332, 220, null);  
            } else if (winner == 3) {
                //Draw
                page.drawString("DRAW!", 345, 240);  
            }
        } else {
            //Show player's turn
            Font font2 = new Font("Courier", Font.BOLD, 16);
            page.setFont(font2);
            page.drawString("It is", 340, 220);  
            if (playerX) {
                page.drawString("X turn", 335, 240); 
            } else {
                page.drawString("O turn", 335, 240);  
            }
        }
    
        //Draw logo 
        Image strawberry = Toolkit.getDefaultToolkit().getImage("strawb.png");
        page.drawImage(strawberry, 325, 5, 75, 65, this);  
        Font c = new Font("Serif", Font.BOLD + Font.ITALIC, 17);
        page.setFont(c);
        page.drawString("Naru's Treats", 313, 80);  
    }
    
    public void drawGame(Graphics page) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 1) {
                    //Draw X (pretzel)
                    ImageIcon xIcon = new ImageIcon("pretzel.png");
                    Image xImage = xIcon.getImage();
                    int imgWidth = xIcon.getIconWidth();
                    int imgHeight = xIcon.getIconHeight();
                    int centeredX = 13 + offset * i + (offset - imgWidth) / 2;
                    int centeredY = 13 + offset * j + (offset - imgHeight) / 2;
                    page.drawImage(xImage, centeredX, centeredY, null);
                } else if (board[i][j] == 2) {
                    //Draw O (donut)
                    ImageIcon oIcon = new ImageIcon("donut150.png");
                    Image oImage = oIcon.getImage();
                    int imgWidth = oIcon.getIconWidth();
                    int imgHeight = oIcon.getIconHeight();
                    
                    int centeredX = 13 + offset * i + (offset - imgWidth) / 2; 
                    int centeredY = 15 + offset * j + (offset - imgHeight) / 2; 
                    page.drawImage(oImage, centeredX, centeredY, null);
                }
            }
        }
    }
      
    
    class XOListener implements MouseListener {
        @Override
        public void mousePressed(MouseEvent e) {
            if (gameDone) return;

            selX = (e.getX() - 25) / offset;
            selY = (e.getY() - 25) / offset;

            if (selX >= 0 && selX <= 2 && selY >= 0 && selY <= 2) {
                if (board[selX][selY] == 0) {
                    board[selX][selY] = playerX ? 1 : 2;
                    playerX = !playerX;
                    checkWinner();
                    repaint();
                }
            }
        }

        public void checkWinner() {
            for (int i = 0; i < 3; i++) {
                //Check rows
                if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != 0) {
                    winner = board[i][0];
                    gameDone = true;
                    updateScore();
                    break;
                }
                //Check columns
                if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != 0) {
                    winner = board[0][i];
                    gameDone = true;
                    updateScore();
                    break;
                }
            }

            //Check diagonals
            if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != 0) {
                winner = board[0][0];
                gameDone = true;
                updateScore();
            } else if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != 0) {
                winner = board[0][2];
                gameDone = true;
                updateScore();
            }

            //Check for a tie
            boolean tie = true;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == 0) {
                        tie = false;
                        break;
                    }
                }
            }
            if (tie && !gameDone) {
                winner = 3; // Tie indicator
                gameDone = true;
            }

            if (gameDone) {
                jButton.setVisible(true);
            }
        }

        public void updateScore() {
            if (winner == 1) player1wins++;
            else if (winner == 2) player2wins++;
        }

        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseClicked(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
    }
}