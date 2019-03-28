
package minesweeper;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MineSweeper {

    public static void main(String[] args) {

        int noOfRows=9,noOfCols=9;
        Board b = new Board();
        b.setVisible(true);
        //b.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        b.pack();
        String [][] Mines;
        Cell [][] cells;
        
        DBConnection connection=new DBConnection();
        if(connection.isSaved())
        {
            String ObjButtons[] = {"Yes", "No"};
                    int PromptResult = JOptionPane.showOptionDialog(null,
                            "You want to Load saved game?",
                            "MineSweeper",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null,
                            ObjButtons,
                            ObjButtons[1]);
                    if (PromptResult == JOptionPane.YES_OPTION) {

                         Mines=new String[noOfRows][noOfCols];
                         cells=new Cell[noOfRows][noOfCols];
                        
                        connection.getMines(Mines, noOfRows, noOfCols);
                        connection.getCells(cells, noOfRows, noOfCols);
                        
                        b.loadSavedGame(noOfRows,noOfCols,Mines,cells);

                    } 
        }
        

        b.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        b.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                if (!b.gameover) {
                    String ObjButtons[] = {"Yes", "No"};
                    int PromptResult = JOptionPane.showOptionDialog(null,
                            "You want to save game?",
                            "MineSweeper",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null,
                            ObjButtons,
                            ObjButtons[1]);
                    if (PromptResult == JOptionPane.YES_OPTION) {

                        DBConnection con = new DBConnection();
                        con.setMines(b.getMines(), b.rows, b.cols);
                        con.setCells(b.getCells(), b.rows,b.cols);

                        System.exit(0);
                    } else {
                        DBConnection con = new DBConnection();
                        String mark[][] = new String[1][1];
                        mark[0][0] = "x";
                        con.setMines(mark, 1, 1);
                        System.exit(0);
                    }
                } else {
                    DBConnection con = new DBConnection();
                    String mark[][] = new String[1][1];
                    mark[0][0] = "x";
                    con.setMines(mark, 1, 1);
                    System.exit(0);
                }
            }
        });

    }

}
