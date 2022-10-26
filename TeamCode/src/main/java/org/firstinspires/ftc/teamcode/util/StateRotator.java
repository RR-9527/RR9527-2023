package org.firstinspires.ftc.teamcode.util;

@SuppressWarnings("unchecked")
public class StateRotator<T> {
    T[] states;
    int currentStatePos;

    public StateRotator(T ...states) {
        this.states = states;
        this.currentStatePos = 0;

        ensureStatesValidity();
    }

    public StateRotator(Iterable<? extends T> states) {
        int totalStates = 0;
        for(T ignored : states)
            totalStates++;

        this.states = (T[]) new Object[totalStates];
        for(T ignored : states)
            this.states[--totalStates] = ignored;

        this.currentStatePos = 0;

        ensureStatesValidity();
    }

    public T next() {
        return states[currentStatePos = ++currentStatePos % states.length];
    }

    public T prev() {
        return states[currentStatePos = --currentStatePos % states.length];
    }

    public T get() {
        return states[currentStatePos];
    }

    public void setCurrentStatePos(int newState) {
        currentStatePos = newState;
    }

    public int length() {
        return states.length;
    }

    private void ensureStatesValidity() {
        if (currentStatePos >= states.length) {
            throw new IllegalArgumentException("Current state is out of bounds");
        }

        if (states.length == 0) {
            throw new IllegalArgumentException("States array is empty");
        }
    }
}
