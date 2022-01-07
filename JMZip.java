import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/*
 * This work complies with the JMU Honor Code. References and Acknowledgments: I received no outside
 * help with this programming assignment.
 */

/**
 * Executable reads in a file and creates a compressed version of that file.
 * 
 * @author Mia Pham
 * @version 11-30-2021
 * 
 */
public class JMZip {

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

    if (!input.canRead() || !output.canWrite()) {
      try {
        bos.write("File is unreadable or unwritable".getBytes());

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
   * Private helper method to read in the bytes from the given file.
   * 
   * @param input The given file
   * @return An array of bytes read from the file
   */
  private static byte[] readFile(File input) {
    byte[] bytes = {};
    FileInputStream fis = null;

    try {
      fis = new FileInputStream(input);
      bytes = fis.readAllBytes();
      fis.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
    return bytes;
  }

  /**
   * Private helper method to write a HuffmanSave object to the given file.
   * 
   * @param output The given file to write to
   * @param huffResult The HuffmanSave object that will be written to the file
   */
  private static void writeToFile(File output, HuffmanSave huffResult) {
    FileOutputStream fos = null;
    ObjectOutputStream oos = null;
    try {
      fos = new FileOutputStream(output);
      oos = new ObjectOutputStream(fos);
      oos.writeObject(huffResult);
      
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Compresses the given file.
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
        byte[] bytes = {};
        bytes = readFile(input);

        HashMap<Byte, Integer> frequencies = HuffTree.countFrequencies(bytes);
        HuffTree tree = HuffTree.buildTree(frequencies);

        HashMap<Byte, String> huffcode = new HashMap<Byte, String>();
        HuffTree.encode(huffcode, tree.root(), "");
        String encoding = HuffTree.encodeBytes(huffcode, bytes);

        BitSequence bits = new BitSequence();
        bits.appendBits(encoding);

        HuffmanSave result = new HuffmanSave(bits, frequencies);
        
        writeToFile(output, result);
      }
    }
  }
}
