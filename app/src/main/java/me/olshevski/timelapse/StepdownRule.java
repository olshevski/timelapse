package me.olshevski.timelapse;

public class StepdownRule {
    public void publicMethod() {
        firstPrivateMethod();
        secondPrivateMethod();
    }

    private void firstPrivateMethod() {
        nestedPrivateMethod();
    }

    private void nestedPrivateMethod() {
    }

    private void secondPrivateMethod() {
    }
}