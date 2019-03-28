
package minesweeper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;


public class Board extends javax.swing.JFrame {

    int rows = 9, cols = 9;
    //ArrayList<JButton> buttons;
    //ArrayList<Cell> buttons;

    Cell cells[][] = new Cell[rows][cols];

    Icon flagIcon;
    Icon bombIcon;

    Timer timer;
    int t = 0;
    int bombCount = 10;

    String bombs[][] = new String[rows][cols];
    boolean gameover = false;
    
    /**
     * Creates new form Board
     */
    ActionListener timerActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            jLabel2.setText("" + t);
            t++;
        }
    };

    public String[][] getMines()
    {
        return bombs;
    }
    
    public Cell[][] getCells()
    {
        return cells;
    }
    
    public void loadSavedGame(int ROWS,int COLS,String [][] MINES,Cell[][] CELLS)
    {
        rows=ROWS;
        cols=COLS;
        bombs=MINES;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j].r=CELLS[i][j].r;
                cells[i][j].c=CELLS[i][j].c;
                cells[i][j].value=CELLS[i][j].value;
                cells[i][j].hasBomb=CELLS[i][j].hasBomb;
                
                if(CELLS[i][j].hasFlag())
                {
                    cells[i][j].setFlag(true);
                }
                if(CELLS[i][j].isOpened())
                {
                    cells[i][j].setOpened();
                }
            }
        }
        
    }
    public Board() {
        initComponents();

        jLabel1.setText("Clock:");
        jLabel2.setText("0");

        timer = new Timer(1000, timerActionListener);
        timer.start();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                Cell c = new Cell();
                c.setR(i);
                c.setC(j);
                c.addMouseListener(mouseListener);

                cells[i][j] = c;
                jPanel1.add(c);
            }
        }
        
        ImageIcon icon1 = new ImageIcon(getClass().getResource("bomb.png"));
        Image img1 = icon1.getImage();
        Image newimg1 = img1.getScaledInstance(cells[0][0].getWidth() - 12, cells[0][0].getHeight() - 12, java.awt.Image.SCALE_SMOOTH);
        bombIcon = new ImageIcon(newimg1);

        jLabel3.setIcon(bombIcon);
        jLabel3.setSize(50, 50);
        jLabel3.setVisible(true);
        jLabel4.setText(": " + bombCount);

        initialzeMines();
      
    }
    
    

    public void initialzeMines()
    {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                bombs[i][j] = "e";
            }
        }

        Random rand = new Random();
        for (int i = 0; i < 10;) {
            int r = rand.nextInt(rows - 1);
            int c = rand.nextInt(cols - 1);
            if (bombs[r][c] == "e") {
                bombs[r][c] = "b";
                i++;
            }
        }

        setBombHints(bombs, rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                if (bombs[i][j] == "b") {
                    cells[i][j].setValue(0);
                    cells[i][j].setBomb();
                } else if (bombs[i][j] == "e") {
                    cells[i][j].setValue(0);
                } else {
                    cells[i][j].setValue(Integer.parseInt(bombs[i][j]));
                }
            }
        }
    }
    MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            //    super.mouseClicked(e); //To change body of generated methods, choose Tools | Templates.
            if (gameover == true) {
                return;
            }
            if (SwingUtilities.isRightMouseButton(e)) {
                System.out.println("R");
                Cell c = (Cell) e.getSource();

                if (!c.isOpened() && !c.hasFlag()) {
                    c.setFlag(true);
                    bombCount--;
                    jLabel4.setText(": " + bombCount);
                    if (bombCount == 0) {
                        boolean iswon = false;
                        for (int i = 0; i < rows && !iswon; i++) {
                            for (int j = 0; j < cols; j++) {
                                if (cells[i][j].hasFlag() && cells[i][j].hasBomb()) {
                                    timer.stop();
                                    iswon = true;
                                    gameover=true;
                                    JOptionPane.showMessageDialog(null, "YOU WON!!!");
                                    break;
                                }

                            }

                        }
                    }

                } else if (!c.isOpened() && c.hasFlag()) {
                    c.setFlag(false);
                    bombCount++;
                    jLabel4.setText(": " + bombCount);
                }
            } else if (SwingUtilities.isLeftMouseButton(e)) {
                System.out.println("L");
                Cell c = (Cell) e.getSource();
                if (!c.isOpened() && !c.hasFlag()) {
                    if (c.hasBomb()) {
                        for (int i = 0; i < rows; i++) {
                            for (int j = 0; j < cols; j++) {
                                cells[i][j].setOpened();
                            }
                        }
                        gameover = true;
                        timer.stop();
                        JOptionPane.showMessageDialog(null, "Ops!! YOU LOST!!");
                    } else {
                        if (c.getValue() > 0) {
                            c.setOpened();
                        } else {
                            int currX = c.getR();
                            int currY = c.getC();

                            for (int i = currX; i < rows && i >= 0; i--) {
                                for (int j = currY; j >= 0 && j < cols; j--) {
                                    if (cells[i][j].hasFlag() || cells[i][j].hasBomb()) {
                                        break;
                                    } else if (cells[i][j].getValue() >= 0) {
                                        cells[i][j].setOpened();

                                    } else {
                                        break;
                                    }
                                }
                                for (int j = currY; j >= 0 && j < cols; j++) {
                                    if (cells[i][j].hasFlag() || cells[i][j].hasBomb()) {
                                        break;
                                    } else if (cells[i][j].getValue() >= 0) {
                                        cells[i][j].setOpened();

                                    } else {
                                        break;
                                    }
                                }

                            }
                            for (int i = currX; i < rows && i >= 0; i++) {
                                for (int j = currY; j >= 0 && j < cols; j--) {
                                    if (cells[i][j].hasFlag() || cells[i][j].hasBomb()) {
                                        break;
                                    } else if (cells[i][j].getValue() >= 0) {
                                        cells[i][j].setOpened();

                                    } else {
                                        break;
                                    }
                                }
                                for (int j = currY; j >= 0 && j < cols; j++) {
                                    if (cells[i][j].hasFlag() || cells[i][j].hasBomb()) {
                                        break;
                                    } else if (cells[i][j].getValue() >= 0) {
                                        cells[i][j].setOpened();

                                    } else {
                                        break;
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }

    };

    public void setBombHints(String s[][], int Rows, int Cols) {
        int r = Rows, c = Cols;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (s[i][j] == "e") {
                    int top = i - 1;
                    int left = j - 1;
                    int bottom = i + 1;
                    int right = j + 1;

                    int count = 0;

                    if (top >= 0 && left >= 0 && bombs[top][left] == "b") {
                        count++;
                    }
                    if (top >= 0 && bombs[top][j] == "b") {
                        count++;
                    }
                    if (top >= 0 && right < c && bombs[top][right] == "b") {
                        count++;
                    }
                    if (left >= 0 && bombs[i][left] == "b") {
                        count++;
                    }
                    if (right < c && bombs[i][right] == "b") {
                        count++;
                    }
                    if (bottom < r && left >= 0 && bombs[bottom][left] == "b") {
                        count++;
                    }
                    if (bottom < r && bombs[bottom][j] == "b") {
                        count++;
                    }
                    if (bottom < r && right < c && bombs[bottom][right] == "b") {
                        count++;
                    }
                    if (count > 0) {
                        bombs[i][j] = "" + count;
                    }
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("jLabel1");

        jLabel2.setText("jLabel1");

        jLabel4.setText("jLabel1");

        jPanel1.setLayout(new java.awt.GridLayout(9, 9));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 471, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)))
                .addGap(36, 36, 36))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(42, 42, 42))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Board().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
