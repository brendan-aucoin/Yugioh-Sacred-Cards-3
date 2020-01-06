package file_loader;


/*
Brendan Aucoin
11/03/17
this class creates files, as well as writes and reads from files. which can be used in most contexts when using files.
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
public class FileLoader
{
 /*all the variables used in this class.*/
 private File file;
 private String fileName;
 private Scanner inputFile;
 private PrintWriter outputFile;
 /*a constructor that takes in the file name that you wanna use to read from or write to.*/
 public FileLoader(String fileName)
 {
   this.fileName = fileName;
   file = new File(fileName);//makes a new file with the filename you pass in.
 }
 public FileLoader(FileLoader loader)
 {
	this.file = loader.file;
	this.fileName = loader.fileName;
 }
 /*a empty constructor, becuase if you dont wanna initialize the file name right when you initialize the fileloader object.*/
 public FileLoader(){}
 /*this method creates a file using the printwriter and the filename.*/
 public void createFile()
 {
     try{outputFile = new PrintWriter(fileName);}catch(FileNotFoundException e){e.printStackTrace();}
 }
 /*this method creates an array list with all the text in the file and returns it so you can do whatever you would like with all the text in the file you wanted.*/
 public ArrayList<String> read()
 {
   try{inputFile = new Scanner(file);}catch(FileNotFoundException e){e.printStackTrace();}//initializing the scanner.
   ArrayList<String>wordList = new ArrayList<String>();//initializing the array list.
  while(inputFile.hasNext())//while there is still text in the file.
  {
    wordList.add(inputFile.nextLine());//add the text from the file to the array list.
  }
  return wordList;//return the arraylist with all the text from the file.
 }
 /*this method writes any amount of text into the file.*/
 public void write(String ... words)//takes in an unknown amount of parameters that are type string.
 {
   /*the words you pass in will be an array because you dont know how many words are going to be passed in.*/
   for(int i =0; i < words.length; i++)//a for loop starting from 0 and goes to the amount of strings you pass in.
   {
     outputFile.println(words[i]);//writes the text that you pass in the parameters to the file.
   }
 }
 /*these 2 methods close the output and input respectively*/
 
 public void closeOutput()
 {
   outputFile.close();
 }
 public void closeInput()
 {
   inputFile.close();
 }
 /*this method checks if the file exists*/
 public boolean exists()
 {
  if(file.exists()) {return true;}
  else {return false;}
 }
 public void makeDirectory(String dirName)
 {
	 File file = new File(dirName);
	 if(!file.exists()) {
		 file.mkdir();
	 }
	 else {return;}
 }
 /*returns the file.*/
 public File getFile(){return file;}
 /*initializes the file with a file name you pass in through the parameters. this is used if you initialize the file loader object with the empty constructor.*/
 public void setFileName(String fileName)
 {
   this.fileName = fileName;
   file = new File(fileName);
 }
 public String getFileName() {return fileName;}
 /*setting the file by another file object.*/
 public void setFile(File file)
 {
  this.file = file;
 }
 
}