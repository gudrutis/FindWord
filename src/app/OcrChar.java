/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

/**
 * This is special class to keep relevant information of OCR recognized character 
 * 
 * 
 * @author Zygis
 */
public class OcrChar {
    public char Char ;
    /**
     * these points reprezentes box coordinates of box corners on the image inside witch 
     * char sits.
     */
    public int x1,y1,x2,y2;
    
    public OcrChar(char Char,int x1,int y1,int x2,int y2 ){
        this.Char = Char;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;        
    }
    /**
     * this will return int array with x y values to put points on the image.
     * @return 
     */
    public int getCenterX(){
        return (this.x1 + this.x2) / 2; 
    }
    public int getCenterY(){
        return (this.y1 + this.y2) / 2;
    }
    
    @Override
    public String toString() {
        return Char+""; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
