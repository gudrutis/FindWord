/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.util.ArrayList;

/**
 * This class should is for finding specified words in the letter matrix.
 * 
 * @author Zygis
 */
public class FindWord {

    
    public static ArrayList<OcrChar[]> findWords(ArrayList<ArrayList<OcrChar>> matrix,
                                        ArrayList<String> lookupWords){
        /**
         * list containaig OrcChar arrays so we can calculate coordinates of found
         * words to cross
         */
//        System.err.println(matrix);
        
        ArrayList<OcrChar[]> crossed_words = new ArrayList<OcrChar[]>()  ; 
        int longestWord = 0 ;
        for (String word:lookupWords){
            if(word.length()>longestWord){
                longestWord = word.length();
            }
//            System.out.println(word);
        }
//        System.out.println(lookupWords.contains('a'+""));
        System.out.println(longestWord);
        
        /* select one letter in the matrix
         */
        for (int i = 0;i < matrix.size(); i++){
            ArrayList<OcrChar> line = matrix.get(i) ;
            for (int j = 0;j < line.size(); j++){
                
                /* here we will travers matrix, build string 1 char at a time
                   witch we will compare to lookup words untill we find one or run 
                   out or space or string is bigger then known word.
                */
                OcrChar startChar =  line.get(j);
                OcrChar endChar =  line.get(j);
                String compare = startChar.toString();
                
                /* check if we are looking just for 1 letter word*/
                if (lookupWords.contains(compare)) {
                    crossed_words.add(new OcrChar[] {startChar,endChar});
                }
                
//                /* look for word in lines*/
                for ( int k = j+1 ; (k < line.size()) && 
                        (compare.length() <= longestWord)  ; k++) {
                    endChar = line.get(k);
                    compare = compare + endChar.toString();
                    if (lookupWords.contains(compare)) {
//                        System.out.println(compare+" : in line | start :" + j+" " +i);
                        crossed_words.add(new OcrChar[]{startChar, endChar});
                    }
                }

//                /* look for word in colums*/
                compare = startChar.toString();
                for (int k = i + 1; (k < matrix.size())
                        && (compare.length() <= longestWord); k++) {
                    endChar = matrix.get(k).get(j); //line.get(k);
                    compare = compare + endChar.toString();
//                    System.out.println(compare);
                    if (lookupWords.contains(compare)) {
//                        System.err.println(startChar.getCenterX()+" "+startChar.getCenterY());
//                        System.err.println(endChar.getCenterX()+" "+endChar.getCenterY());
//                        System.out.println(compare+" : in colums start :" + j+" " +i);
                        crossed_words.add(new OcrChar[]{startChar, endChar});
                    }
                }
                
                /* look for word in diagnols towards right bottom*/
                compare = startChar.toString();
                for (int k = 1; (i + k < matrix.size())
                        && (j + k < matrix.get(k).size())
                        && (compare.length() < longestWord); k++) {
//                    System.out.println( (i+k)+ " "+ (j+k)+" ");
                    endChar = matrix.get(i + k).get(j + k); //line.get(k);
                    compare = compare + endChar.toString();
//                    System.out.println(compare);
                    if (lookupWords.contains(compare)) {
//                        System.err.println(startChar.getCenterY()+" "+endChar.getCenterY());
//                        System.out.println(compare + " : in diagnol start :" + j+" " +i);
                        crossed_words.add(new OcrChar[]{startChar, endChar});
                    }
                } //   /*end of look for word in diagnols*/
            }
        }    
        return crossed_words;
    }
}
//        int[] coordx = crossed_words.get(0)[0].getCenter();
//        int[] coordy = crossed_words.get(0)[1].getCenter();
//        System.out.println(coordx[0]+" "+coordx[1]+"|"+coordy[0]+" " + coordy[1]);