/*
 * This work complies with the JMU Honor Code. 
 * References and Acknowledgments: Referenced the OpenDSA textbook.
 */

/**
 * Huffman Tree node implementation.
 * 
 * @author Mia Pham
 * @verison 11-30-2021
 *
 */
public class HuffNode {
  private byte data;
  private int weight;
  private HuffNode left;  
  private HuffNode right; 
  
  /**
   * Huffman node constructor for leaf node with no children.
   * 
   * @param data The byte value of the node
   * @param weight The frequency of the value
   */
  public HuffNode(byte data, int weight) {
    this.data = data;
    this.weight = weight;
    this.left = null;
    this.right = null;
  }
  
  /**
   * Huffman node constructor for internal node.
   * 
   * @param weight The weight of the left and right children combined
   * @param left The left child
   * @param right The right child
   */
  public HuffNode(int weight, HuffNode left, HuffNode right) {
    this.data = 0;
    this.weight = weight;
    this.left = left;
    this.right = right;
  }
  
  /**
   * Checks if the node is a leaf or not.
   * 
   * @return True if node is a leaf
   */
  public boolean isLeaf() {
    return left == null && right == null;
  }
  
  /**
   * Returns the byte value of the node.
   */
  public byte data() {
    return data;
  }
  
  /**
   * Returns the weight (relative frequency) of the corresponding byte.
   */
  public int weight() {
    return weight;
  }
  
  /**
   * Returns the left child of the internal node.
   */
  public HuffNode left() {
    return left;
  }
  
  /**
   * Returns the right child of the internal node.
   */
  public HuffNode right() {
    return right;
  }
}
