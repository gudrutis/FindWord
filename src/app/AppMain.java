/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

// Unable to load library 'libtesseract304'
// issprendziau nukopindamas .ddl failus tiesiai i projekta
import app.utils.ImageProcessor;
import com.sun.jna.Pointer;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.util.ImageIOHelper;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class AppMain {
    

/**
 * 
 * -Djava.library.path="C:\_CustLib\opencv\build\java\x86"
 */
//    static {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//    }
        
    /**
     * This loads librarys second time ( first time is loaded from GUI2.java
     * I use it for redundancy so that code will work with GUI1.java
     */
        static{
        String workingPath = System.getProperty("user.dir");
        String arch = System.getProperty("sun.arch.data.model") ;
        String sep = File.separator;
//        System.setProperty( "java.library.path", "libs" );
        if (arch.equals("64")){
            String path = workingPath + sep + "libs" + sep + "x64"+sep +"opencv_java310.dll";
//            System.setProperty( "java.library.path", "libs" );
            System.load(path);
        }else if (arch.equals("32")){
            String path = workingPath + sep + "libs" + sep + "x86"+sep +"opencv_java310.dll";
            System.load(path);
//            System.loadLibrary("opencv_java310");
        }else{
        System.out.println("different architecture?: "+ arch );
        }
        }
    
    /**
     *
     * @param image
     * @param pointA
     * @param pointB
     * @return
     * @throws TesseractException
     */
    public static ArrayList<String> extractWordList(BufferedImage image, Point pointA,
            Point pointB) throws TesseractException {

        ArrayList<String> lookupWords = new ArrayList<>();
        int dimWidth = (int) ((int) pointB.x - pointA.x);
        int dimHeight = (int) ((int) pointB.y - pointA.y);
        java.awt.Point startPoint = new java.awt.Point((int) pointA.x, (int) pointA.y);
        Dimension dim = new Dimension(dimWidth, dimHeight);
        Rectangle renctArea = new Rectangle(startPoint, dim);
        Tesseract1 instance = new Tesseract1();
        instance.setHocr(false);

        String result = instance.doOCR(image, renctArea);
        String pattern = System.getProperty("line.separator");
        String[] wordArray = result.split("\\r?\\n| ");

        for (String word : wordArray) {
            lookupWords.add(word.toUpperCase());
        }

        System.out.println(lookupWords);
        return lookupWords;
    }

    public static ArrayList<ArrayList<OcrChar>> extractLetterMatix(BufferedImage image,
            Point pointA, Point pointB) throws TesseractException {
        ArrayList<ArrayList<OcrChar>> matrix = new ArrayList<>();

        int dimWidth = (int) ((int) pointB.x - pointA.x);
        int dimHeight = (int) ((int) pointB.y - pointA.y);
        java.awt.Point startPoint = new java.awt.Point((int) pointA.x, (int) pointA.y);
        Dimension dim = new Dimension(dimWidth, dimHeight);
        Rectangle renctArea = new Rectangle(startPoint, dim);

        Tesseract1 instance = new Tesseract1(); //
        instance.setHocr(true);
        instance.setTessVariable("tessedit_char_blacklist", "|\\?/0123456789");
        String result = instance.doOCR(image, renctArea);
        matrix = ParseHORC.parse(result);
        System.out.println(matrix);
        return matrix;
    }

    public static ArrayList<OcrChar[]> findWords(ArrayList<ArrayList<OcrChar>> matrix,
            ArrayList<String> lookupWords) {
        ArrayList<OcrChar[]> foundWords = new ArrayList<>();
        foundWords = FindWord.findWords(matrix, lookupWords);
        return foundWords;
    }

    public static Mat crossImage(Mat image, ArrayList<OcrChar[]> foundWords) {
        for (OcrChar[] points : foundWords) {
            System.out.println(points[0].getCenterX()+" "+points[0].getCenterY()+" "+points[1].getCenterX()+" "+points[1].getCenterY());
            Imgproc.line(image, new Point(points[0].getCenterX(), points[0].getCenterY()),
                    new Point(points[1].getCenterX(), points[1].getCenterY()),
                    new Scalar(0, 0, 0), 3);
        }
        return image;
    }

    public static Mat procesImage(Mat matImage, Point PtA1, Point PtA2, Point PtB1, Point PtB2) throws TesseractException {
        final ImageProcessor imageProcessor = new ImageProcessor();

        BufferedImage bfImage = imageProcessor.toBufferedImage(matImage);
        ArrayList<ArrayList<OcrChar>> matrix = extractLetterMatix(bfImage, PtA1, PtA2);
        ArrayList<String> lookupWords = extractWordList(bfImage, PtB1, PtB2);
        ArrayList<OcrChar[]> foundWords = findWords(matrix, lookupWords);
        matImage = crossImage(matImage, foundWords);

        return matImage;
    }

    /**
     * badly designed main aplication, use with app.GUI.java for fast results.
     *
     * @param image
     * @param lookUpList
     * @throws IOException
     */
    public static void main(String image, String lookUpList) throws IOException {

//        String imagePath = "resource\\find_small.jpg";
        String imagePath = image;
        File imageFile = new File(imagePath);
//        File imageFile1 = new File("C:\\Users\\Zygis\\Desktop\\tess_test_data\\Find_1lookup.jpg");

        ArrayList<String> lookupWords = new ArrayList<>();
        ArrayList<OcrChar[]> foundWords = new ArrayList<>();
        ArrayList<ArrayList<OcrChar>> matrix = new ArrayList<>();

        Mat ImageMatrix = Imgcodecs.imread(imagePath);

        Scanner inFile1 = new Scanner(new File(lookUpList));
        while (inFile1.hasNext()) {
            lookupWords.add(inFile1.nextLine().toUpperCase());
        }
        System.out.println(lookupWords);

        Tesseract1 instance = new Tesseract1(); //
        try {

            instance.setHocr(true);
            instance.setTessVariable("tessedit_char_blacklist", "|0123456789"); // blaklistas
//          instance.setTessVariable("tessedit_create_boxfile", "true");
//          instance.setTessVariable("save_blob_choices", "T"); // kazka padeda char atpazinimui

            String result = instance.doOCR(imageFile);
//            System.out.println(result);
            matrix = ParseHORC.parse(result);
            System.out.println(matrix);
            foundWords = FindWord.findWords(matrix, lookupWords);

            try (
                    //            instance.setHocr(false);
                    //            result = instance.doOCR(imageFile1);
                    //            System.out.println(result);
                    //            -----------
                    //            writing info to html file
                    PrintWriter writer = new PrintWriter("the-file-name.html", "UTF-8")) {
                writer.println(result);
            }
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }

        /*----- 
        cross found letters from the image
         */
        for (OcrChar[] points : foundWords) {
//           System.out.println(points[0].getCenterX()+" "+points[0].getCenterY()+" "+points[1].getCenterX()+" "+points[1].getCenterY());
            Imgproc.line(ImageMatrix, new Point(points[0].getCenterX(), points[0].getCenterY()),
                    new Point(points[1].getCenterX(), points[1].getCenterY()),
                    new Scalar(0, 0, 0), 3);
        }

//        for (OcrChar[] points : foundWords) {
////           System.out.println(points[0].getCenterX()+" "+points[0].getCenterY()+" "+points[1].getCenterX()+" "+points[1].getCenterY());
//            Imgproc.line(ImageMatrix, new Point(points[0].getCenterX(), points[0].getCenterY()),
//                    new Point(points[1].getCenterX(), points[1].getCenterY()),
//                    new Scalar(0, 0, 0), 3);
//        }
//        ////-------
//        // DEBUG 
//        //put regognised chars POSTION on the image ( to check recognised char matches)
//        for (ArrayList<OcrChar> line_array : matrix) {
//            for (OcrChar Char : line_array) {
//                Imgproc.circle(ImageMatrix, new Point(Char.getCenterX(), Char.getCenterY()),
//                        1, new Scalar(0, 0, 255), 4);
//            }
//        }
//        ////-------
//        // DEBUG 
//        // put regognised chars back to the image ( to check recognised char matches)
//        for (ArrayList<OcrChar> line_array : matrix){
//            for (OcrChar Char : line_array){
//                Imgproc.putText(ImageMatrix, Char.toString(), 
//                        new Point(Char.x2, Char.y2),
//                                1, 2,  new Scalar(0, 0, 255));
//            }
//        }
        Imgcodecs.imwrite("resource\\output.jpeg", ImageMatrix);
    } // <end> main
}

//-------------[ snipets]
//      
//	Imgproc.circle(image,new Point(e.getX(),e.getY()),20, new Scalar(0,0,255), 4);
//	updateView(image);
