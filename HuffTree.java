import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

/*
 * This work complies with the JMU Honor Code. 
 * References and Acknowledgments: Referenced the OpenDSA textbook.
 */

/**
 * Huffman Tree implementation.
 * 
 * @author Mia Pham
 * @verison 11-30-2021
 */
public class HuffTree {
  private HuffNode root;
  
  /**
   * Constructs a Huffman tree with just the root.
   * 
   * @param root The root of the Huffman tree
   */
  public HuffTree(HuffNode root) {
    this.root = root;
  }

  /**
   * Returns the overall weight of the tree.
   */
  public int weight() {
    return root.weight();
  }

  /**
   * Returns the root of the tree.
   */
  public HuffNode root() {
    return root;
  }
  
  /**
   * Compares the two nodes based on the frequencies. 
   * If frequency is tied it will compare which t has the lowest byte value.
   */
  public static Comparator<HuffNode> treeComparator = new Comparator<HuffNode>() {

    @Override
    public int compare(HuffNode n1, HuffNode n2) {
      int comp = n1.weight() - n2.weight();

      if (comp == 0) {
        comp = minValue(n1) - minValue(n2);
      }
      return comp;
    }   
  };
  
  /**
   * Returns the smallest byte value in the given tree.
   * 
   * @param node The given Huffman node
   * @return The smallest value in tree
   */
  public static byte minValue(HuffNode node) {
    HuffNode current = node;
    
    while (current.left() != null) {
      current = current.left(); 
    }  
    return current.data();
  }
  
  /**
   * Stores the frequency of each byte in a HashMap.
   * 
   * @param bytes The array of bytes read from the input file
   * @return Hash Map of frequencies for each byte
   */
  public static HashMap<Byte, Integer> countFrequencies(byte[] bytes) {
    HashMap<Byte, Integer> frequencies = new HashMap<Byte, Integer>();
    
    for (byte b : bytes) {     
      if (frequencies.containsKey(b)) {
        frequencies.put(b, frequencies.get(b) + 1);
      
      } else {
        frequencies.put(b, 1);
      }
    }
    return frequencies;   
  }

  /**
   * Builds a Huffman Tree according to the frequencies of each byte.
   * 
   * @param frequencies The HashMap of frequencies for each byte
   * @return Huffman Tree based on frequencies of bytes
   */
  public static HuffTree buildTree(HashMap<Byte, Integer> frequencies) {
    HuffNode node = null;
    HuffTree tree = new HuffTree(node);
    
    if (frequencies.size() >= 1) {
      PriorityQueue<HuffNode> pq = new PriorityQueue<>(frequencies.size(), treeComparator);    
      
      for (var entry : frequencies.entrySet()) {
        node = new HuffNode(entry.getKey(), entry.getValue());
        pq.add(node);
      }
      
      while (pq.size() > 1) {
        HuffNode left = pq.poll();
        HuffNode right = pq.poll();
        
        node = new HuffNode(left.weight() + right.weight(), left, right);
        pq.add(node);
      }
      
      tree.root = node;
    }
    return tree;
  }

  /**
   * Stores the Huffman code for each byte in a HashMap.
   * 
   * @param huffCode The HashMap to be filled
   * @param node The node of the tree
   * @param s The string representation of the Huffman code for each byte
   */
  public static void encode(HashMap<Byte, String> huffCode, HuffNode node, String s) {
    if (node == null) {
      return;
    }
    
    if (node.isLeaf()) {
      huffCode.put(node.data(), s);
    }

    encode(huffCode, node.left(), s + "0");
    encode(huffCode, node.right(), s + "1");
  }
  
  /**
   * Builds a String of Huffman code from the bytes in the file.
   * 
   * @param huffCode The Hash Map that contains the Huffman code for each byte
   * @return A String of Huffman code for the given bytes
   */
  public static String encodeBytes(HashMap<Byte, String> huffCode, byte[] bytes) {
    StringBuilder s = new StringBuilder();
    
    for (byte b : bytes) {
      if (huffCode.get(b) == "") {
        s.append("0");
      }
      
      s.append(huffCode.get(b));
    }   
    return s.toString();   
  }
  
  /**
   * Loops through the Huffman tree to decode the BitSequence of the compressed file
   * and returns the original byte array.
   * 
   * @param node The node of the tree to traverse through
   * @param bits The BitSequence from the compressed file
   * @param decoding The array of bytes containing the contents of the original file
   */
  public static void decode(HuffNode node, BitSequence bits,
      byte[] decoding) {
    Iterator<Integer> it = bits.iterator();
    HuffNode root = node;
    
    // the index of the decoding array
    int codeInd = 0; 
    // the index of the bitSequence
    int index = 0;
    while (index <= bits.length() && node != null) { 
      int b = 0;
      if (index < bits.length()) {
        b = it.next();
      } 
    
      if (node.isLeaf()) {
        decoding[codeInd] = node.data();
        codeInd++;
        node = root;
      }
      
      if (b == 0) {
        node = node.left();

      } else {
        node = node.right();
      }
      index++;
    }    
  }
}
