package model;

@FunctionalInterface
public interface ObservatorField {
    public void eventOcurred(Field field,EventField event);
}
