package org.firstinspires.ftc.teamcode.util;

@SuppressWarnings("unchecked")
public class StateRotator<T> {
    T[] states;
    int currentState;

    public StateRotator(T ...states) {
        this(0, states);
    }

    public StateRotator(int currentState, T ...states) {
        this.states = states;
        this.currentState = currentState;

        ensureStatesValidity();
    }

    public StateRotator(Iterable<? extends T> states) {
        this(states, 0);
    }

    public StateRotator(Iterable<? extends T> states, int currentState) {
        int totalStates = 0;
        for(T ignored : states)
            totalStates++;

        this.states = (T[]) new Object[totalStates];
        for(T ignored : states)
            this.states[--totalStates] = ignored;

        this.currentState = currentState;

        ensureStatesValidity();
    }

    public T next() {
        return states[currentState = ++currentState % states.length];
    }

    public T prev() {
        return states[currentState = --currentState % states.length];
    }

    public T get() {
        return states[currentState];
    }

    public int getIndex() {
        return currentState;
    }

    public int length() {
        return states.length;
    }

    private void ensureStatesValidity() {
        if (currentState >= states.length) {
            throw new IllegalArgumentException("Current state is out of bounds");
        }

        if (states.length == 0) {
            throw new IllegalArgumentException("States array is empty");
        }
    }
}
