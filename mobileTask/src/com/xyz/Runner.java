package com.xyz;

import com.xyz.exceptions.IllegalParamsException;
import com.xyz.exceptions.UnknownParamException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Runner {
    private static boolean wFlag;
    private static boolean mFlag;
    private static boolean xFlag;
    private static boolean isError;
    private static String filePath;
    private static ArrayList<String> output = new ArrayList<>();

    public static void run(String[] args) {
        analyzeParams(args);
        if (isError) {
            System.err.println("Critical error, we are sorry");
        } else {
            if (xFlag) {
                if(wFlag){
                    detailedWordAnalyze();
                }
                if (mFlag){
                    detailedCharAnalyze();
                }
            }
            else {
                if(wFlag){
                    wordAnalyze();
                }
                if(mFlag){
                    charAnalyze();
                }
            }
        }
        for(String info : output){
            System.out.println(info);
        }
    }

    private static void analyzeParams(String[] args) {
        try {
            if (args.length > 2 || args.length < 1)
                throw new IllegalParamsException("Illegal num of params", args.length);
            if (args.length == 1) {
                mFlag = true;
                wFlag = true;
                filePath = args[0];
                Path file = Paths.get(filePath);
                if (!file.isAbsolute() || !Files.exists(file)) {
                    System.err.println("File path incorrect");
                    isError = true;
                }
            } else {
                char[] charParams = args[0].substring(1).toCharArray();
                for (char x : charParams) {
                    if (x == 'w') {
                        wFlag = true;
                        continue;
                    }
                    if (x == 'm') {
                        mFlag = true;
                        continue;
                    }
                    if (x == 'X') {
                        xFlag = true;
                    } else {
                        throw new UnknownParamException("Param is not supported", x);
                    }
                }
                filePath = args[1];
                Path file = Paths.get(filePath);
                if (!file.isAbsolute() || !Files.exists(file)) {
                    System.err.println("File path incorrect");
                    isError = true;
                }
            }
        } catch (IllegalParamsException exception) {
            System.out.println(exception.getMessage());
            System.out.println("Number of params = " + exception.getParamNum() + " that more than 2 and less than 1");
            isError = true;
        } catch (UnknownParamException exception) {
            System.out.println(exception.getMessage());
            System.out.println(exception.getUnknownParam() + " now is not supported, sorry");
            isError = true;
        }
    }

        private static void detailedCharAnalyze() {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            long totalCharNum = 0;
            HashMap<Character, Integer> numOfRepeat = new HashMap<>();
            String line;
            char[] charLine;
            int value;
            while ((line = bufferedReader.readLine()) != null){
                charLine = line.toCharArray();
                for(char symbol : charLine){
                    if(numOfRepeat.get(symbol) != null){
                        value = numOfRepeat.get(symbol) + 1;
                        numOfRepeat.put(symbol, value);
                    }
                    else {
                        numOfRepeat.put(symbol, 1);
                    }
                }
                totalCharNum += line.length();
            }
            numOfRepeat.entrySet().stream()
                    .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed())
                    .limit(10)
                    .forEach( info -> output.add("Char " + info.getKey() + " entered " + info.getValue() + " times"));
            output.add("Total number of chars in file = " + totalCharNum);
        }catch (IOException exception){
            System.err.println("File don't exist");
        }
    }

    private static void charAnalyze() {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            long totalCharNum = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null){
                totalCharNum += line.length();
            }
            output.add("Total number of chars in file = " + totalCharNum);
        }catch (IOException exception){
            System.err.println("File don't exist");
        }
    }

    private static void detailedWordAnalyze() {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            long totalWordNum = 0;
            boolean isWord;
            HashMap<String, Integer> numOfRepeat = new HashMap<>();
            StringBuilder word = new StringBuilder();
            String line;
            char[] charLine;
            while ((line = bufferedReader.readLine()) != null){
                charLine = line.toCharArray();
                for(int i = 0; i < charLine.length; i++){
                    isWord = !Character.isWhitespace(charLine[i]);
                    if(isWord){
                        word.append(charLine[i]);
                    }
                    if(!Character.isWhitespace(charLine[i]) && i == charLine.length-1){
                        totalWordNum++;
                        continue;
                    }
                    if(!Character.isWhitespace(charLine[i]) && Character.isWhitespace(charLine[i+1])){
                        numOfRepeat.merge(word.toString(), 1, Integer::sum);
                        word.setLength(0);
                        totalWordNum++;
                    }
                }
            }
            numOfRepeat.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(10)
                    .forEach( info -> output.add("Word " + info.getKey() + " entered " + info.getValue() + " times"));
            output.add("Total number of words in file = " + totalWordNum);
        }catch (IOException exception){
            System.err.println("File don't exist");
        }
    }

    private static void wordAnalyze() {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            long totalWordNum = 0;
            String line;
            char[] charLine;
            while ((line = bufferedReader.readLine()) != null){
                charLine = line.toCharArray();
                for(int i = 0; i < charLine.length; i++){
                    if(!Character.isWhitespace(charLine[i]) && i == charLine.length-1){
                        totalWordNum++;
                        continue;
                    }
                    if(!Character.isWhitespace(charLine[i]) && Character.isWhitespace(charLine[i+1])){
                        totalWordNum++;
                    }
                }
            }
            output.add("Total number of words in file = " + totalWordNum);
        }catch (IOException exception){
            System.err.println("File don't exist");
        }
    }
}
