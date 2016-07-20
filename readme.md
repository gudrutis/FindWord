# Find word project

This is java netbeans project and code of findWord program. It uses Tessract OCR library and OpenCV. 

It use tess4j a Tesseract wraper: http://tess4j.sourceforge.net/tutorial/
and also OpenCV java wrape (just google it), plus some aditonal libraries (like jsoup for xml like document parsing)

*This is not an ideal code nor fully working. It should serve more like a proof of concept how to look for words in images and cross them out. Also how to setup both librarys as one Java project in Netbeans.*

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