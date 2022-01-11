package com.vyhnis;

import java.util.Scanner;

public class Pozemky {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numberOfTasks = scanner.nextInt();

        for(int i = 0; i < numberOfTasks; i++){
            int numberOfSpecies = scanner.nextInt();
            int totalArea = 0;
            for(int j = 0; j < numberOfSpecies; j++) {
                scanner.next();
                totalArea += scanner.nextInt() * scanner.nextInt();
            }
            System.out.println(totalArea);
        }
    }
}
