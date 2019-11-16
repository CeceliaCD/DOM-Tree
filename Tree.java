package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		
		root = buildTreeHelper();
		
	}
		
		private TagNode buildTreeHelper() {
			TagNode head = null;
			String word = "";
			if(sc.hasNextLine()) {
				word = sc.nextLine();
		
			}else {
				return null;
			}
			head = new TagNode(word, null, null);
			
			if(head.tag.contains("<")) {
				head.tag = head.tag.substring(1, head.tag.length()-1);
				if(!head.tag.contains("/")) {
					head.firstChild = buildTreeHelper();
				}else {
					return null;
				}
			}
			head.sibling = buildTreeHelper();
			return head;
			
		}
		

	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		
		replaceHelper(root, oldTag, newTag);
	}
	
	private void replaceHelper(TagNode currentNode, String oldTag, String newTag) {
		if(currentNode == null) {
			return;
		}
		
		TagNode cPtr = currentNode;
		while(cPtr != null) {
			if(cPtr.tag.contains(oldTag)) {
				cPtr.tag = newTag;
			}
			replaceHelper(cPtr.firstChild, oldTag, newTag);
			cPtr = cPtr.sibling;
		}
	}
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		
		if(root == null) {
			return;
		}
		
		TagNode ptr = root;
		ptr = ptr.firstChild;
		ptr = ptr.firstChild;
		
		while(ptr != null) {
			if(ptr.tag.equals("table")) {
				break;
			}
			ptr = ptr.sibling;
		}
		int count = 1;
		if(ptr.tag.equals("table")) {
			ptr = ptr.firstChild;
			while(count != row) {
				ptr = ptr.sibling;
				count++;
			}
		}
		
		if(count == row) {
			ptr = ptr.firstChild;
			while(ptr != null) {
				ptr.firstChild = new TagNode("b", ptr.firstChild, null);
				ptr = ptr.sibling;
			}
		}
	}

	
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		
		if(root == null) {
			return;
		}
		
		if(tag.equals("ol") || tag.equals("ul")) {
			oldelete(root, tag);
		}else if(tag.equals("p") || tag.equals("em") || tag.equals("b")) {
			pdelete(root, tag);
		}
		
	}

	//for ol, ul
	private void oldelete(TagNode prev, String delete){
		
		if(prev == null) {
			return;
		}
		
		oldelete(prev.firstChild, delete);
		
		if(prev.firstChild != null){
			if(prev.firstChild.tag.equals(delete)) {
				TagNode chaser = prev.firstChild.firstChild;
				while(chaser.sibling != null){
					chaser.tag = "p";
					chaser = chaser.sibling;
				}
				replaceHelper(chaser, "li", "p");
				chaser.sibling = prev.firstChild.sibling;
				prev.firstChild = prev.firstChild.firstChild;
			}
		}
		
		if(prev.sibling != null){
			if(prev.sibling.tag.equals(delete)) {
				TagNode follow = prev.sibling.firstChild;
				while(follow.sibling != null){
					follow.tag = "p";
					follow = follow.sibling;
				}
				replaceHelper(follow, "li", "p");
				follow.sibling = prev.sibling.sibling;
				prev.sibling = prev.sibling.firstChild;
			}
		}
		
		oldelete(prev.sibling, delete);
	}


	// for the other ones 
	private void pdelete(TagNode curr, String delete) {

		if (curr == null) {
			return;
		}
		
		pdelete(curr.firstChild, delete);	


		if(curr.sibling != null ){
			if(curr.sibling.tag.equals(delete)) {
				if(!curr.tag.equals(delete) && curr.firstChild == null) {
					TagNode forward = curr.sibling;
					if(forward != null && forward.tag.equals(delete)) {
						curr.firstChild = forward.firstChild;
					}
				}
				TagNode last = curr.firstChild;
				while(last.sibling != null){
					last = last.sibling;	
					System.out.println("last: " + last);
				}
				last.sibling = curr.sibling.sibling;
				curr.sibling = curr.sibling.firstChild;
			}	
		}

		if(curr.firstChild != null){
			if(curr.firstChild.tag.equals(delete)) {
				curr.firstChild = curr.firstChild.firstChild;
			}
		}

		pdelete(curr.sibling, delete);
	}

		
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
	}
	

	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|----");
			} else {
				System.out.print("     ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
