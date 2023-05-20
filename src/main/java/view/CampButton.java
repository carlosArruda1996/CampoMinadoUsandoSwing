package view;

import model.EventField;
import model.Field;
import model.ObservatorField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CampButton extends JButton implements ObservatorField, MouseListener {
    private Field fields;
    private final Color BG_DEFALT = new Color(184,184,184);
    private final Color BG_MARKED = new Color(8,17,247);
    private final Color BG_EXPLOSION = new Color(189,66,68);
    private final Color TEXT = new Color(0,100,0);
    public CampButton(Field field){

        this.fields = field;
        setBackground(BG_DEFALT);
        setBorder(BorderFactory.createBevelBorder(0));
        field.observerRegister(this);
        setOpaque(true);

        addMouseListener(this);
    }

    @Override
    public void eventOcurred(Field field, EventField event) {
        switch (event){
            case OPEN:
                applyOpenStyle();
                break;
            case MARK:
                applyMarkStyle();
                break;
            case EXPLODE:
                applyExplosionStyle();
                break;
            default:
                applyDefaultStyle();
        }
    }

    private void applyDefaultStyle() {

        setBackground(BG_DEFALT);
        setBorder(BorderFactory.createBevelBorder(0));
        setText("");
    }

    private void applyExplosionStyle() {
        setBackground(BG_EXPLOSION);
        setForeground(Color.WHITE);
        setText("*");
    }

    private void applyMarkStyle() {
        setBackground(BG_MARKED);
        setText("X");
    }
    private void applyOpenStyle() {
        if(fields.isUndermined()){
            setBackground(BG_EXPLOSION);
            return;
        }
        setBackground(BG_DEFALT);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        switch (fields.minesInTheNeighborhood()){
            case 1:
                setForeground(Color.BLUE);
                break;
            case 2:
                setForeground(Color.GREEN);
                break;
            case 3:
                setForeground(Color.RED);
                break;
            case 4:
                setForeground(Color.BLACK);
                break;
            default:
                setForeground(Color.CYAN);
        }
        String value = !fields.safetyNeighborhood() ? fields.minesInTheNeighborhood() + "" : "";
        setText(value);
    }
    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
    if (e.getButton() == 1){
        fields.open();
    }else {
        fields.tickToggle();
        }

    }
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
