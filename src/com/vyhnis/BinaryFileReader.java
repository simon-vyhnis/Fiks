package com.vyhnis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BinaryFileReader {

    public static void main(String[] args) throws IOException {

        File file = new File("D:\\Projects\\input.txt");
        InputStream inputStream;
        try{
            inputStream= new FileInputStream(file);

        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        int b;
        while(( b = inputStream.read()) != -1){
            System.out.println(b);
        }
        System.out.println();
    }
}
