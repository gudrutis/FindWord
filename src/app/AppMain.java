/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

// Unable to load library 'libtesseract304'
// issprendziau nukopindamas .ddl failus tiesiai i projekta
import com.sun.jna.Pointer;
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

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    // class    
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
