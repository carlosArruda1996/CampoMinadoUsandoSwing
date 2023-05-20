package view;

import model.Board;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    public BoardPanel(Board board){

        setLayout(new GridLayout(board.getLines(),board.getColumns()));

        board.forEach(c -> add(new CampButton(c)));

        board.observerRegister(e -> {
            SwingUtilities.invokeLater(() -> {
                if(e.isWin()){
                    JOptionPane.showMessageDialog(this,"ganhou");
                }else {
                    JOptionPane.showMessageDialog(this,"perdeu meu parça");
                }
                board.restart();
            });
        });
    }
}
