package com.example.test1;

public class EventType {

    private int id;
    private String label;

    public EventType() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public EventType(String label) {
        this.label = label;
    }

    public EventType(int id, String label) {
        this.id = id;
        this.label = label;
    }
}