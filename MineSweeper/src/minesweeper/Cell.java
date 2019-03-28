
package minesweeper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Cell extends JButton {
    //  JButton button;

    boolean hasBomb;
    boolean hasFlag;
    boolean isOpened;
    int value;
    Icon flagIcon, bombIcon;
    int r, c;

    
    public Cell() {
        //   button=new JButton();

        setText("");
        setBackground(Color.blue);
        setSize(50, 50);
        setFocusable(false);

        ImageIcon icon = new ImageIcon(getClass().getResource("flag.png"));
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(25 , 25, java.awt.Image.SCALE_SMOOTH);
        flagIcon = new ImageIcon(newimg);

        ImageIcon icon1 = new ImageIcon(getClass().getResource("bomb.png"));
        Image img1 = icon1.getImage();
        Image newimg1 = img1.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
        bombIcon = new ImageIcon(newimg1);

        hasBomb = false;
        hasFlag = false;
        isOpened = false;
        value = 0;
        r = 0;
        c = 0;
    }

    
    public void loadCell(int row,int  col,int val,boolean ISopened,boolean HasBomb,boolean HasFlag)
    {
        r=row;
        c=col;
        value=val;
        isOpened=ISopened;
        hasFlag=HasFlag;
        hasBomb=HasBomb;               
    }
    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public boolean hasBomb() {
        return hasBomb;
    }

    public void setBomb() {
        this.hasBomb = true;
    }

    public boolean hasFlag() {
        return hasFlag;
    }

    public void setFlag(boolean hasFlag) {
        this.hasFlag = hasFlag;
        if (hasFlag) {
            setIcon(flagIcon);

        } else {
            setIcon(null);
        }
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened() {
        this.isOpened = true;
        if (hasBomb) {
            if (hasFlag) {
                ImageIcon icon1 = new ImageIcon(getClass().getResource("hiddenBomb.png"));
                Image img1 = icon1.getImage();
                Image newimg1 = img1.getScaledInstance(25,25, java.awt.Image.SCALE_SMOOTH);
                bombIcon = new ImageIcon(newimg1);
            }
            setBackground(null);
            setIcon(bombIcon);
        } else {
            setBackground(null);
            setIcon(null);
            if (value > 0) {
                setText("" + value);
            } else {
                setText("");
            }
        }
        // this.setEnabled(false);

    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void load(int row,int col,int val,boolean ISopened,boolean HasBomb,boolean HasFlag)
    {
        setR(row);
        setC(col);
        setValue(val);
        setFlag(HasFlag);
        if(HasBomb)
            setBomb();
        if(ISopened)
            setOpened();
    }
}
