package com.example.springjpa.enums;

public enum Sort {
    ASC("asc"),DESC("desc");

    private final String name;

    Sort(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
