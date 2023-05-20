package model;

import java.util.ArrayList;
import java.util.List;

public class Field {

    private final int line;
    private final int column;

    private boolean uncoverd = false;
    private boolean undermined = false;
    private boolean marked = false;

    private final List<Field> neighbors = new ArrayList<>();
    private final List<ObservatorField> observers = new ArrayList<>();

    Field(int line, int column) {
        this.line = line;
        this.column = column;
    }
    public void observerRegister(ObservatorField observer){
        observers.add(observer);
    }


    private void notifyObservers(EventField event){
        observers.forEach(o -> o.eventOcurred(this,event));
    }

    void addNeighbor(Field neighbor) {
        boolean differentLine = line != neighbor.line;
        boolean differentColumn = column != neighbor.column;
        boolean diagonal = differentLine && differentColumn;

        int deltaLine = Math.abs(line - neighbor.line);
        int deltaColumn = Math.abs(column - neighbor.column);
        int deltaGeneral = deltaLine + deltaColumn;

        if (deltaGeneral == 1 && !diagonal) {
            neighbors.add(neighbor);
        } else if (deltaGeneral == 2 && diagonal) {
            neighbors.add(neighbor);
        }
    }

    public void tickToggle() {
        if (!uncoverd) {
            marked = !marked;
            if(marked){
                notifyObservers(EventField.MARK);
            }else{
                notifyObservers(EventField.UNMARK);
            }
        }
    }

    public void open() {

        if (!uncoverd && !marked) {

            if (undermined) {
                notifyObservers(EventField.EXPLODE);
            }
            setUncoverd(true);

            if (safetyNeighborhood()) {
                neighbors.forEach(n -> n.open());
            }
        }
    }

    public boolean safetyNeighborhood() {
        return neighbors.stream().
                noneMatch(neighbor -> neighbor.undermined);
    }

    void mine() {
        undermined = true;
    }

    public boolean isUndermined() {
        return undermined;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setUncoverd(boolean uncoverd) {
        this.uncoverd = uncoverd;
        if(uncoverd){
            notifyObservers(EventField.OPEN);
        }
    }

    boolean goalAchieved() {
        boolean discovered = !undermined && uncoverd;
        boolean protectd = undermined && marked;
        return discovered || protectd;
    }

    public int minesInTheNeighborhood() {
        return (int)neighbors.stream().
                filter(neighbor -> neighbor.undermined).count();
    }

    void restart() {
        uncoverd = false;
        undermined = false;
        marked = false;
        notifyObservers(EventField.RESTART);
    }

}
