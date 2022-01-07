import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.HashMap;

/*
 * This work complies with the JMU Honor Code. References and Acknowledgments: I received no outside
 * help with this programming assignment.
 */

/**
 * Executable that reads in a compressed file and recreates a copy of the original file.
 * 
 * @author Mia Pham
 * @version 11-30-2021
 *
 */
public class JMUnzip {
  
  /**
   * Private helper method to check if the number of arguments given is correct.
   * 
   * @param files The given arguments
   * @return False if the number of arguments is less than 2
   */
  private static boolean hasCorrectArgs(String[] files) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    
    if (files.length < 2) {
      try {
        bos.write("Incorrect number of arguments.".getBytes());
        
      } catch (IOException e) {
        e.printStackTrace();        
      } 
      
      byte[] b = bos.toByteArray();
      for (int x = 0; x < b.length; x++) {
        System.out.print((char) b[x]);
      }
      return false;
    }
    return true;
  }
  
  /**
   * Private helper method to check if input file is unreadable or output file is unwritable.
   * 
   * @param input The file to be read
   * @param output The file to write to
   * @return False if input can't be read OR output can't be written to
   */
  private static boolean canReadAndWrite(File input, File output) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    String extension = input.toString().substring(input.toString().lastIndexOf("."));
    
    if (!input.canRead() || !extension.equals(".jmz")) {
      try {
        bos.write("File is unreadable or File format incorrect".getBytes());
      
      } catch (IOException e) {
        e.printStackTrace();
      }
      
      byte[] b = bos.toByteArray();
      for (int x = 0; x < b.length; x++) {
        System.out.print((char) b[x]); 
      }
      return false;
    }
    return true;
  }
  
  /**
   * Private helper method to read in the compressed file.
   * 
   * @param input The given file
   * @return A HuffmanSave object
   */
  private static HuffmanSave readFile(File input) {
    byte[] bytes = {};
    FileInputStream fos = null;
    ObjectInputStream ois = null;
    HuffmanSave huffResult = null;

    try {
      fos = new FileInputStream(input);
      ois = new ObjectInputStream(fos);
      huffResult = (HuffmanSave) ois.readObject();

    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return huffResult;
  }
  
  /**
   * Private helper method to original bytes to the given file.
   * 
   * @param output The given file to write to
   * @param decoding The byte contents of the original file
   */
  private static void writeToFile(File output, byte[] decoding) {
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(output);
      fos.write(decoding);
      fos.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Private helper method to calculate how many bytes should be written to output file.
   * 
   * @param frequencies The frequency map that stores frequencies for each byte
   * @return The total number of bytes in the map
   */
  private static int numBytes(HashMap<Byte, Integer> frequencies) {
    int total = 0;
    
    for (byte b : frequencies.keySet()) {
      total += frequencies.get(b);
    }
    return total;
  }

  /**
   * Decompresses the given file.
   */
  public static void main(String[] args) {   
    if (hasCorrectArgs(args)) {
      File input = new File(args[0]);
      File output = new File(args[1]);
      
      try {
        output.createNewFile();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      
      if (canReadAndWrite(input, output)) {
        HuffmanSave huffResult = readFile(input);
        
        HashMap<Byte, Integer> frequencies = huffResult.getFrequencies();
        HuffTree tree = HuffTree.buildTree(frequencies);
        
        BitSequence bits = huffResult.getEncoding();
        
        int size = numBytes(frequencies);
        byte[] decoding = new byte[size];
        HuffTree.decode(tree.root(), bits, decoding);

        writeToFile(output, decoding);
      }
    }     
  }
}