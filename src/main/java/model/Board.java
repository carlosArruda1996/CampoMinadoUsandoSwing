package model;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Board implements ObservatorField {

    private final int lines;
    private final int columns;
    private final int mines;

    private final List<Field> fields = new ArrayList<>();
    private final List<Consumer<EventResult>> observers = new ArrayList<>();

    public Board() {
        String strLines = JOptionPane.showInputDialog(null,"Digite o numero de linhas: ");
        int lines = Integer.parseInt(strLines);
        String strColumns = JOptionPane.showInputDialog(null,"Digite o numero de linhas: ");
        int columns = Integer.parseInt(strColumns);
        String strMines = JOptionPane.showInputDialog(null,"Digite o numero de linhas: ");
        int mines = Integer.parseInt(strMines);
        this.lines = lines;
        this.columns = columns;
        this.mines = mines;

        generateFields();
        associateNeighbors();
        sortMines();
    }

    public void observerRegister(Consumer<EventResult> observer){
        observers.add(observer);
    }

    private void notifyObservers(boolean result){

        observers.forEach(o -> o.accept(new EventResult(result)));
    }

    private void showMines(){
        fields.stream().filter(f -> f.isUndermined()).filter(f -> !f.isMarked())
                .forEach(f -> f.setUncoverd(true));
    }

    private void generateFields() {
        for (int line = 0; line < lines; line++) {
            for (int column = 0; column < columns; column++) {
                Field field = new Field(line,column);
                field.observerRegister(this);
                fields.add(field);
            }
        }
    }

    private void associateNeighbors() {
        for (Field f1: fields) {
            for (Field f2: fields) {
                f1.addNeighbor(f2);
            }
        }
    }

    private void sortMines() {
        long armedMines;
        Predicate<Field> undermined =
                field -> field.isUndermined();

        do {
            int random = (int) (Math.random() * fields.size());
            fields.get(random).mine();
            armedMines = fields.stream().filter(undermined).count();
        } while(armedMines < mines);
    }

    public boolean goalAchieved() {
        return fields.stream().allMatch(f -> f.goalAchieved());
    }

    public void restart() {
        String sair = JOptionPane.showInputDialog(null,"Quer jogar novamente: (sim ou nao)");
        if ("sim".equalsIgnoreCase(sair)) {
            fields.forEach(f -> f.restart());
            sortMines();
        }else if ("nao".equalsIgnoreCase(sair)){
            JOptionPane.showMessageDialog(null,"Obrigado por jogar! S2");
        }else {
            JOptionPane.showMessageDialog(null,"Voce digitou algo invalido!");
        }

    }

    public int getLines() {
        return lines;
    }


    public int getColumns() {
        return columns;
    }

    public void forEach(Consumer<Field> function){
        fields.forEach(function);
    }

    @Override
    public void eventOcurred(Field field, EventField event) {
        if(event == EventField.EXPLODE){
            showMines();
            notifyObservers(false);
        }else if(goalAchieved()){
            notifyObservers(true);
        }
    }
}
