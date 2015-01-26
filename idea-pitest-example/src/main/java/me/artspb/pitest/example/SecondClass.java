package me.artspb.pitest.example;

/**
 * @author Artem Khvastunov &lt;contact@artspb.me&gt;
 */
public class SecondClass {

    public int uncoveredMethod() {
        int i = 42;
        return Math.abs(i);
    }

    public int partiallyCoveredMethod(int input) {
        if (input >= 0 || input % 2 == 0) {
            return 42;
        }
        return 0;
    }
}
