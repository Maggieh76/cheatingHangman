import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

    public class Hangman {

        private static int t = 1;
        private static int guessCount = 0;
        private static Character finalGuess ;



        public static Set selectSize(Set<String> words, int largeWord) {
            Set<String> toReturn = new HashSet<>();
            Scanner console = new Scanner(System.in);
            System.out.print("What should be the length of the word? ");
            t = console.nextInt();
            while (t <= 0 || t >= largeWord) {
                System.out.print("Uh oh, that length won't work. Please stay between 1 and 28. Enter a new length: ");
                t = console.nextInt();
            }
            if (t > 0 && t <= largeWord) {
                for (String word : words) {
                    if (word.length() == t) {
                        toReturn.add(word);
                    }
                }
            }

            return toReturn;

        }

        public static void playGame(Set<String> words, int guesses) {
            List<String> wordList = new ArrayList<>();
            Map<String, List<String>> wordFams = new HashMap<>();
            for (String current : words) { //each word within words
                wordList.add(current);
            }
            Scanner scanner = new Scanner(System.in);
            Set<Character> usedletters = new HashSet<>();
            String board = "";
            System.out.println("");
            System.out.println("");
            for (int i = 0; i < t; i++) {
                board += "_";
            }
            int roundCount=1;
            while (guessCount > 0 && !(win(wordList,board))) {
                String prevBoard = board;
                System.out.println();
                System.out.println("New Round! Welcome to Round " + roundCount);
                roundCount++;
                System.out.println("Number of guesses left: " + guessCount);
                System.out.println("Already guessed letters: " +usedletters);
                System.out.println("Here is your Hangman board: " + board);
                System.out.print("Please guess a letter: ");
                Character g = scanner.next().charAt(0);
                while (usedletters.contains(g)) {
                    System.out.println("Oops you already guessed that letter. Please try again ");
                    g = scanner.next().charAt(0);
                }
                usedletters.add(g);
                wordFams = genFamilies(wordList,usedletters); //creates map
                String familyChoice = getBest(wordFams); //creates new board
                wordList = wordFams.get(familyChoice); //get list of best words
                //System.out.println(wordList);
                if (board.equals(familyChoice)) {
                    guessCount--;
                    if(guessCount == 0){
                        finalGuess = g;
                    }
                }
                board = familyChoice;
                if(!board.equals(prevBoard)){ //if it changes then
                    System.out.println("Woah! You got that letter right!");
                }else{
                    System.out.println("Uh oh, that was not a good guess");
                }
            }
            if((win(wordList,board))){
                System.out.println();
                System.out.println("Congrats, you won!!");
                System.out.println("The word was: " + wordList.get(0)); //print only thing left - answer
            } else {
                System.out.println("Sorry you lost the word was: " + revealWord(wordList,finalGuess));
            }

        }

        public static Map<String, List<String>> genFamilies(List<String> wordList, Set<Character> guessedLetters) {
            Map<String, List<String>> families = new HashMap<>();
            //List<String> list = new ArrayList<>();
            for (String word : wordList) {
                String boardTest = "";
                for (int i = 0; i < word.length(); i++) {
                    if(guessedLetters.contains(word.charAt(i))){
                        boardTest+=word.charAt(i);
                    } else {
                        boardTest += "_";
                    }
                }
                if (families.containsKey(boardTest)) {
                    List<String> wordMatchList = families.get(boardTest);
                    wordMatchList.add(word);
                    families.put(boardTest, wordMatchList);
                } else {
                    List<String> wordMatchList = new ArrayList<>();
                    wordMatchList.add(word);
                    families.put(boardTest, wordMatchList);
                }

            }

            return families;
        }

        public static String getBest(Map<String, List<String>> families) {
            int size = 0;
            String toReturn = "";
            for (Map.Entry<String, List<String>> entry : families.entrySet()) { //finds key with biggest list value (most possibilities)
                if (entry.getValue().size() > size) {
                    size = entry.getValue().size();
                    toReturn = entry.getKey();
                }
            }
            return toReturn;

        }

        public static boolean win(List<String> list, String board){
            if(list.size() == 1 && list.get(0).equals(board)){
                return true;
            }else{
                return false;
            }
        }
        public static String revealWord(List<String> list, Character h){
            for(String word : list){
                int counter = 0;
                for (int i = 0; i < word.length(); i++) {
                    if(word.charAt(i) != h){
                        counter++;
                    }
                }
                if(counter == word.length()){
                    return word;
                }
            }
            return list.get(0);
        }
        public static void main(String[] args) {
            Set<String> allWords = new HashSet<>();
            String fileName = "words.txt";
            int largestWord = 0;
            try {
                Scanner scanner = new Scanner(new File(fileName));
                while (scanner.hasNext()) { //gets length of longest word
                    String g = scanner.next();
                    int h = g.length();
                    if (h > largestWord) {
                        largestWord = h;
                    }
                    allWords.add(g);
                }
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            System.out.println("Welcome to Hangman!!!");
            System.out.println("Please play politely and with dignity. AKA: absolutely NO cheating");
            allWords = selectSize(allWords, largestWord);
            Scanner g = new Scanner(System.in);
            System.out.print("How many guesses would you like to receive? ");
            guessCount = g.nextInt();
            playGame(allWords, guessCount);

        }

    }

