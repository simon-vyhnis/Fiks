package com.vyhnis;

import sun.dc.pr.PathStroker;

import javax.sql.rowset.spi.TransactionalWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Delnici {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numberOfTasks = scanner.nextInt();

        for(int i = 0; i < numberOfTasks; i++){
            int numberOfLanguages = scanner.nextInt();

            String[] languages = new String[numberOfLanguages];
            for(int j = 0; j < languages.length; j++){
                languages[j] = scanner.next();
            }

            Translator[] translators = new Translator[scanner.nextInt()];
            for (int j = 0; j < translators.length; j++) {
                translators[j] = new Translator(scanner.nextInt(), scanner.nextInt());
                translators[j].loadLanguages(scanner);
            }

            String language1 = scanner.next();
            String language2 = scanner.next();

            List<Path> paths = new ArrayList<>();
            for (Translator t : translators) {
                if (t.hasLanguage(language1)) {
                    paths.add(new Path(t));
                }
            }
        }
    }

    private void continuePath(List<List<Translator>> paths){

    }

    static class Translator {
        String [] languages;
        int price;

        public Translator(int numberOfLanguages, int price){
            languages = new String[numberOfLanguages];
            this.price = price;
        }

        public void loadLanguages(Scanner s){
            for(int j = 0; j < languages.length; j++){
                languages[j] = s.next();
            }
        }

        public boolean hasLanguage(String language){
            for (String l : languages) {
                if(l.equals(language)){
                    return true;
                }
            }
            return false;
        }
    }

    static class Path {
        List<Translator> translators;
        int price = 0;
        boolean finished = false;

        public Path(Translator translator){
            translators = new ArrayList<>();
            translators.add(translator);
        }

        public boolean addTranslator(Translator translator, String language2){
            boolean res = false;
            for(String l : translators.get(translators.size()-1).languages){
                if(translator.hasLanguage(l)){
                    res = true;
                }
            }
            if(!translators.contains(translator) && res){
                translators.add(translator);
                price += translator.price;
                if (translator.hasLanguage(language2)){
                    finished = true;
                }
                return true;
            }else{
                return false;
            }

        }
    }
}
