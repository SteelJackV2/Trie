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
		trieNode.firstChild = new TrieNode(new Indexes(0, (short) 0, (short) (allWords[0].length() - 1)), null, null);
		TrieNode currentStage;

		for (int x = 1; x<allWords.length; x++){
			int endIndex = allWords[x].length() - 1;
			int previousLevel = 0;
			String c = allWords[x];
			currentStage = trieNode.firstChild;
			boolean end = false;
			while (currentStage != null && !end){
				if (currentStage.firstChild == null) { // It is a Leaf
					String comparator = allWords[currentStage.substr.wordIndex];
					int same = sameLetters(c, comparator);
					if(same>previousLevel){ // if there are same letters then make a tree
						currentStage.substr = new Indexes(currentStage.substr.wordIndex, currentStage.substr.startIndex, (short)(same-1));
						TrieNode child = new TrieNode(new Indexes( x , (short)same, (short)(c.length() -1) ), null , null);
						currentStage.firstChild = new TrieNode(new Indexes(currentStage.substr.wordIndex, (short)same, (short)(comparator.length()-1) ), null ,child);
						end = true;
					}else if (currentStage.sibling == null){ //or else add a sibling to the end;
						currentStage.sibling = new TrieNode(new Indexes(x, currentStage.substr.startIndex, (short)endIndex), null, null);
						end = true;
					}else{ // check the next sibling
						currentStage = currentStage.sibling;
					}
				}else{    // if it is a child
					String substring = allWords[currentStage.substr.wordIndex].substring(currentStage.substr.startIndex,currentStage.substr.endIndex+1);
					int same = sameLetters(c, substring);
					if(includes(substring,c)){ //if the word should be included inside the child
						currentStage = currentStage.firstChild;
						previousLevel = sameLetters(c, substring);
					}else if(same>0 && same>previousLevel && !includes(substring,c)){ //if the word doesn't fit inside a child and has to become a sibling leaf with a child
							TrieNode child = new TrieNode(new Indexes(x, (short)same, (short)(c.length() - 1)), null, null);
							currentStage.firstChild = new TrieNode(currentStage.substr, currentStage.firstChild, currentStage.sibling);
							currentStage.substr = new Indexes(currentStage.substr.wordIndex, currentStage.substr.startIndex, (short)(same - 1));
							currentStage.firstChild.substr.startIndex = (short)(currentStage.substr.endIndex+1);
							currentStage.firstChild.sibling = child;
							end = true;
					}else if (currentStage.sibling == null){ //or else add a sibling to the end
						currentStage.sibling = new TrieNode(new Indexes(x, currentStage.substr.startIndex, (short)endIndex), null, null);
						end = true;
					}else{ // check the next sibling
						currentStage = currentStage.sibling;
					}
				}
			}
 		}

		return trieNode;
	}
	private static String getVal(TrieNode node, String[] words){
		Indexes indexes = node.substr;
		String r = words[indexes.wordIndex];
		return r.substring(0, indexes.endIndex+1);
	}


	private static boolean includes(String tree, String word){
		char[] first = tree.toCharArray();
		char[] second = word.toCharArray();
		if(word.length() < tree.length()){
			return false;
		}
		for(int x = 0; x<first.length; x++){
			if(first[x] != second[x]){
				return false;
			}
		}
		return true;
	}
	private static int sameLetters(String word, String word2){
		int sameLetters = 0;
		char[] first = word.toCharArray();
		char[] second = word2.toCharArray();
		int length = second.length;
		if(first.length< second.length) {
			length = first.length;
		}
		boolean done = false;
		while (!done && sameLetters<length){
			if(first[sameLetters] == second[sameLetters]){
				sameLetters++;
			}else{
				done = true;
			}
		}
		return sameLetters;
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
	public static ArrayList<TrieNode> completionList(TrieNode root,String[] allWords,String prefix) {
		ArrayList<TrieNode> results= new ArrayList<>();
		TrieNode mainPointer = root.firstChild;
		boolean end = false;
		while(mainPointer != null && !end){
			String current = getVal(mainPointer, allWords);
			if(includes(prefix,current)){
				results.addAll(getWords(mainPointer));
				mainPointer = mainPointer.sibling;
			}else if(includes(current, prefix)){
				mainPointer = mainPointer.firstChild;
			}else {
				mainPointer = mainPointer.sibling;
			}
		}
		if (results.size() == 0){
			return null;
		}
		return results;
	}

	private static ArrayList<TrieNode> getWords(TrieNode root){
		ArrayList<TrieNode> results= new ArrayList<TrieNode>();
		if(root.firstChild == null){
			results.add(root);
			return results;
		}else {
			TrieNode mainPointer = root.firstChild;
			while (mainPointer != null) {
				if (mainPointer.firstChild == null) {
					results.add(mainPointer);
					mainPointer = mainPointer.sibling;
				} else {
					results.addAll(getWords(mainPointer));
					mainPointer = mainPointer.sibling;
				}
			}
		}
		return results;
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
