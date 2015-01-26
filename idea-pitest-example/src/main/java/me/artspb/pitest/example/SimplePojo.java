package me.artspb.pitest.example;

public class SimplePojo {

    private String field;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
        doSomething();
        new SecondClass();
    }

    private void doSomething() {
        this.field = "dleif";
    }

    public static void main(String[] args) {

    }
}
