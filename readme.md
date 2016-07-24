# Find word project

## About

This is java Netbeans project and code of findWord program. It uses Tesseract OCR
 library and OpenCV. 

It uses tess4j - a Tesseract wrapper: http://tess4j.sourceforge.net/tutorial/
and also OpenCV java wrapper (just google it), plus some additional libraries 
(like jsoup for XML like document parsing).

*This is not an ideal code nor fully working. 
It should serve more as a proof of concept how to look for words in images and cross them out. 
Also how to setup both libraries as one Java project in Netbeans.*

Ideally, you should just replace _src/app_ source code with your own, just take a look how to load *opencv_java310* at src/app/Gui2.java 

```java
    static {
        // this will check architecture and dinamikli load
//        System.out.println(System.getProperty("user.dir"));
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
``` 
## Idea

Tesseract is an Optical Character Recognition (OCR) library. OpenCV is Computer 
Vision library. For the moment I use Tesseract to extract words and letters from
images and OpenCV only for drawing objects on the image.

Tesseract (or at least tess4j library) returns recognized text in 2 ways - a simple 
letters' stream with no info about text position or in hOCR format (formatted in 
HTML/XML) with size, position and other information. 

*The problem* is that the outputted hOCR contains info only about words, not letters, meaning 
there is no easy way to tell the exact position of letters. At the moment if I 
get only a position of a word, I divide it into subregions by a number of letters, 
but it is very inaccurate. That is the reason why in output examples lines crossing 
found words are a little bit offset.  

So program works like this:
- image or region of an image is given from entry point class
- Tesseract library returns hOCR file
- hOCR is processed using *jsoup* library
- letters are stored as OcrChar.java objects with all relevant information in 2 
dimensional list.
- a list of words are given either in a .txt file or selected from an image
- selecting letters from text 1 by 1 in 3 directions (straight, down, diagonal),
create words with them, and check if they are in the lookup list. If so, add found
word's coordinate to the list
- take this coordinates and draw line trough them on the image

## Project structure 

### Entry points

- src/app/GUI.java <- very simple GUI, select image with prepared letter matrix and .txt 
with words to look.
- src/app/GUI2.java <- more advanced GUI, able to select a region of the image where is letter matrix 
 and where is words' list which to look.

### Other files
src/com/ <- came with tutorial I followed, not sure if I need them
src/net/ <- came with tutorial I followed, not sure if I need them
src/app/utils <- helper utilities 

src/app/AppMain.java <- main application logic
src/app/FindWord.java <- takes word list and letter matrix and looks for words
src/app/OrcChar.java <- Class to store letters with relevant information
src/app/ParseHOCR.java <- to extract info from hOCR file 

tessdata/ <- configuration data for Tesseract library
resource/ <- example images and words' lists stored here

