/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * class used to parse HORC.html style output and get necessary informations to
 * 2 demensional arrays using jsoup to parse input string
 * https://jsoup.org/cookbook/input/parse-document-from-string
 * @author Zygis
 */
public class ParseHORC {
    
    
    public static ArrayList<ArrayList<OcrChar>> parse(String html){
        
        ArrayList<ArrayList<OcrChar>> twoDArrayList = new ArrayList<ArrayList<OcrChar>>();
        Document doc = Jsoup.parse(html);
        
        
        
        Elements lines = doc.select("span.ocr_line");        
        for (Element line : lines) {
            Elements words = line.select("span.ocrx_word");
            ArrayList OrcCharLine = new ArrayList<OcrChar>();
            
            for (Element word : words){
                String data = word.attr("title");
                data = data.replace(";","");
                String[] proc_data = data.split(" ");

                OcrChar letter = new OcrChar(word.text().charAt(0),
                                        Integer.parseInt(proc_data[1]),
                                        Integer.parseInt(proc_data[2]),
                                        Integer.parseInt(proc_data[3]),
                                        Integer.parseInt(proc_data[4]));
                
                OrcCharLine.add(letter);
            }
            twoDArrayList.add(OrcCharLine);
        }
        

        return twoDArrayList;
        
    } //<end>pasre()
} // <end>classs


//span.ocr_line[id='line_1_1'] > span.ocrx_word

//-----------------------        
//        test of array traversing
//-----------
//        for (ArrayList<OcrChar> line_array : twoDArrayList){
//            for (OcrChar Char : line_array){
//                System.out.print(Char+":"+Char.getCenterX() +" "+Char.getCenterY()+"|" );
////                System.out.print(Char+":"+Char.x1 +" "+Char.y1 +" "+Char.x2 +" "+Char.y2 +" "+"|" );
//            }
//            System.out.println("");
//        }
        // return array