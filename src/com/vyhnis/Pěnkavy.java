package com.vyhnis;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Pěnkavy {

    private static final byte A = 0;
    private static final byte C = 1;
    private static final byte G = 2;
    private static final byte T = 3;

    public static void main(String[] args){
        File file = new File("D:\\Projects\\input2.txt");
        try {
            InputStream inputStream;
            inputStream = new FileInputStream(file);


            int numberOfTasks = readNumber(inputStream);

            for (int i = 0; i < numberOfTasks; i++) {
                int numberOfDNAs = readNumber(inputStream);
                int maxDifference = readNumber(inputStream);
                List<String> res = new ArrayList<>();
                DNA[] DNAs = new DNA[numberOfDNAs];
                for (int j = 0; j < numberOfDNAs; j++) {
                    DNAs[j] = new DNA((short) readNumber(inputStream), inputStream, j);
                }
                List<Pair> pairs = new ArrayList<>();
                for (int j = 0, DNAsLength = DNAs.length; j < DNAsLength; j++) {
                    for (int k = j + 1; k < DNAsLength; k++) {
                        if (DNAs[k].isSimilarTo(DNAs[j], maxDifference)) {
                            pairs.add(new Pair(DNAs[k], DNAs[j]));
                        }
                    }
                }

                for (int j = 0, pairsSize = pairs.size(); j < pairsSize; j++) {
                    //System.out.println("Comparing two pairs");
                    Pair p = pairs.get(j);
                    List<Third> possibleThirds = new ArrayList<>();
                    for (int k = j + 1; k < pairsSize; k++) {
                        Pair p2 = pairs.get(k);
                        if (p != p2) {
                            if (p.dna1 == p2.dna1 || p.dna2 == p2.dna1) {
                                boolean wasThere = false;
                                for (Third t : possibleThirds) {
                                    if (t.a == p2.dna2) {
                                        possibleThirds.remove(t);
                                        wasThere = true;
                                        break;
                                    }
                                }
                                if (!wasThere) {
                                    possibleThirds.add(new Third(p2.dna2, p2.dna1));
                                }
                            }
                            if (p.dna1 == p2.dna2 || p.dna2 == p2.dna2) {
                                boolean wasThere = false;
                                for (Third t : possibleThirds) {
                                    if (t.a == p2.dna1) {
                                        possibleThirds.remove(t);
                                        wasThere = true;
                                        break;
                                    }
                                }
                                if (!wasThere) {
                                    possibleThirds.add(new Third(p2.dna1, p2.dna2));
                                }
                            }
                        }
                    }

                    for (Third third : possibleThirds) {
                        DNA notB;
                        if (p.dna1 == third.b) {
                            notB = p.dna2;
                        } else {
                            notB = p.dna1;
                        }
                        boolean valid = true;
                        for (Pair p3 : pairs) {
                            if (p3.dna1 == notB && p3.dna2 == third.a || p3.dna2 == notB && p3.dna1 == third.a) {
                                valid = false;
                                break;
                            }
                        }
                        if (valid) {
                            if (third.a.index < notB.index) {
                                res.add(third.a.index + " " + third.b.index + " " + notB.index);
                            } else {
                                res.add(notB.index + " " + third.b.index + " " + third.a.index);
                            }
                        }
                    }
                }
                System.out.println(res.size());
                for (String s : res) {
                    System.out.println(s);
                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static int readNumber(InputStream stream) {
        int res = 0;
        Stack<Byte> digits = new Stack<>();
        while(true) {
            //System.out.println("Reading number...");
            try {
                int read = stream.read();
                if (read > 47 && read < 58) {
                    digits.add((byte) (read-48));
                } else if (read == 10 || read == 32 || read == -1) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }
        int counter2 = 0;
        while(!digits.empty()){
            //System.out.println("Reading number...");
            res+= digits.pop()*Math.pow(10, counter2);
            counter2++;
        }
        return res;
    }

    public static class DNA{
        byte[] value;
        int index;

        DNA(short length, InputStream stream, int index) throws IOException {
            this.index = index;

            value = new byte[length];
            int maxLengthCounter = 0;
            while (maxLengthCounter < length) {
                int c = stream.read();
                char rc = (char) c;
                //System.out.println("Reading binary "+rc);
                String binaryString = String.format("%8s", Integer.toBinaryString(c)).replaceAll(" ", "0");
                if (maxLengthCounter < length) {
                    value[maxLengthCounter] = binaryPairToByte(binaryString.substring(0, 2));
                    maxLengthCounter++;
                }
                if (maxLengthCounter < length) {
                    value[maxLengthCounter] = binaryPairToByte(binaryString.substring(2, 4));
                    maxLengthCounter++;
                }
                if (maxLengthCounter < length) {
                    value[maxLengthCounter] = binaryPairToByte(binaryString.substring(4, 6));
                    maxLengthCounter++;
                }
                if (maxLengthCounter < length) {
                    value[maxLengthCounter] = binaryPairToByte(binaryString.substring(6, 8));
                    maxLengthCounter++;
                }
            }
            while(stream.read()!=10){
                //System.out.println("Clearing rest and spaces...");
            }
        }
        private byte binaryPairToByte(String pair){
            if ("00".equals(pair)) {
                return A;
            } else if ("01".equals(pair)) {
                return C;
            } else if ("10".equals(pair)) {
                return G;
            } else if ("11".equals(pair)) {
                return T;
            }
            //System.out.println("Wrong pair!!");
            return 4;
        }

        public boolean isSimilarTo(DNA dna, int maxOperations){
            byte[] longerOne;
            byte[] shorterOne;
            if(dna.value.length > this.value.length){
                longerOne = dna.value;
                shorterOne = this.value;
            }else{
                shorterOne = dna.value;
                longerOne = this.value;
            }

            int operationsCounter = longerOne.length - shorterOne.length;
            if (operationsCounter > maxOperations) {
                return false;
            }
            operationsCounter = 0;

            for (int i = 0, longerOneLength = longerOne.length; i < longerOneLength; i++) {
                if(i >= shorterOne.length){
                    operationsCounter++;
                    if(operationsCounter > maxOperations){
                        return false;
                    }
                }else {
                    byte b = shorterOne[i];
                    if (b != longerOne[i]) {
                        operationsCounter++;
                        if (operationsCounter > maxOperations) {
                            return false;
                        }
                        if (shorterOne.length + 1 <= longerOne.length) {
                            if (areArraysSimilar(copyArray(shorterOne, longerOne[i], i), longerOne, operationsCounter, maxOperations)) {
                                return true;
                            }
                        } else {
                            if (areArraysSimilar(longerOne, copyArray(shorterOne, longerOne[i], i), operationsCounter, maxOperations)) {
                                return true;
                            }
                        }
                    }
                }
            }
            return true;
        }

        private byte[] copyArray(byte[] originalArray, byte itemToAdd, int pos){
            byte[] res = new byte[originalArray.length+1];
            for (int i = 0; i < pos; i++){
                res[i] = originalArray[i];
            }
            res[pos] = itemToAdd;
            for(int i = pos; i < originalArray.length; i++){
                res[i+1] = originalArray[i];
            }
            return res;
        }

        private boolean areArraysSimilar(byte [] shorterOne, byte[] longerOne, int operationsCounter, int maxOperations){
            //System.out.println("Checking similarity...");
            for (int i = 0, shorterOneLength = shorterOne.length; i < shorterOneLength; i++) {
                byte b = shorterOne[i];
                if (b != longerOne[i]){
                    operationsCounter++;
                    if(operationsCounter > maxOperations){
                        return false;
                    }
                    if(shorterOneLength+1 <= longerOne.length){
                        if(areArraysSimilar(copyArray(shorterOne, longerOne[i], i), longerOne, operationsCounter, maxOperations)){
                            return true;
                        }
                    }else{
                        if(areArraysSimilar(longerOne, copyArray(shorterOne, longerOne[i], i), operationsCounter, maxOperations)){
                            return true;
                        }
                    }
                }
            }
            return true;
        }
    }

    public static class Pair{
        DNA dna1;
        DNA dna2;

        public Pair(DNA dna1, DNA dna2) {
            this.dna1 = dna1;
            this.dna2 = dna2;
        }
    }

    public static class Third{
        DNA a;
        DNA b;

        public Third(DNA a, DNA b) {
            this.a = a;
            this.b = b;
        }
    }
}
