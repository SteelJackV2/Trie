package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		TrieNode trieNode = new TrieNode(null,null, null);
		TrieNode currentStage = trieNode.firstChild;

		for (int x = 0; x<allWords.length; x++){
			int endIndex = allWords[x].length() - 1;
			if(currentStage == null) {
				currentStage = new TrieNode(new Indexes(0, (short) 0, (short) endIndex), null, null);
			}else{
				String c = allWords[x];
				while (currentStage != null){
					if (currentStage.firstChild == null) {			//Leaf
						String comparator = allWords[currentStage.substr.wordIndex];

						if ( getAmtShared( c, prefix ) > 0 ) {		//If there's any shared chars in the leaf
							//Leaf turns into a tree, and put the word under the new tree.
							TrieNode insert = new TrieNode(null, null, null);
							currentStage.firstChild = insert;					//Point currentStage to the new word.
							System.out.println();
							currentStage.substr.endIndex = (short)(getAmtShared( c, prefix ) - 1);
							insert.substr = new Indexes( currentStage.substr.wordIndex , (short) currentStage.substr.startIndex , (short) (allWords[currentStage.substr.wordIndex].length() - 1) );
							insert.sibling = new TrieNode( null , null , null);
							insert.sibling.substr = new Indexes( allWords.length - 1 , (short)currentStage.substr.startIndex , (short) allWords[allWords.length - 1].length() );
							return;
						}

					}
				}
			}
		}

		return trieNode;
	}

	private int sameLetters( String word, String word2){

	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		return null;
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode currentStage=root.firstChild; currentStage != null; currentStage=currentStage.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(currentStage, indent+1, words);
		}
	}
 }
