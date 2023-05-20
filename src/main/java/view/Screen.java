package view;

import model.Board;

import javax.swing.*;

public class Screen extends JFrame {

    public Screen(){
        Board board = new Board();

        add(new BoardPanel(board));

        setTitle("MinedField");
        setSize(690, 438);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    public static void main(String[] args) {
        new Screen();
    }
}
