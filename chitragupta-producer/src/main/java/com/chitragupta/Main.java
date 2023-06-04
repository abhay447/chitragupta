package com.chitragupta;

public class Main {
    public static void main(String[] args) {
        final MockDataManager mockDataManager = new MockDataManager();
        while(true) {
            System.out.println(mockDataManager.generateEvent());
        }
    }
}