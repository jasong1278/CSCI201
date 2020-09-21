/**
 ******************************************************************************
 *                    HOMAnyTypeWORK-2, cs201
 ******************************************************************************
 *                    Amortized Dictionary
 ******************************************************************************
 *
 * Implementation of an Amortized Array-Based Dictionary data structure.
 *
 * This data structure supports duplicates and does *NOT* support storage of
 * null references.
 *
 * Notes:
 * 		-It is *highly* recommended that you begin by reading over all the methods,
 *       all the comments, and all the code that has already been written for you.
 *
 * 		-the specifications provided are to help you understand what the methods
 *       are supposed to accomplish.
 * 		-they are *NOT* descriptions for how you should implement the methods.
 * 		-See the lab documentation & lecture notes for implementation details.
 *
 * 		-Some of the helper methods specify a runtime cost; make sure your
 *       implementation meets that requirement.
 * 		-(Also, obviously, if the lecture notes and/or the lab documentation specifies
 *       a runtime cost for a method, you need to pay attention to that).
 *
 *
 *****************************************************************************/



public class Dictionary<AnyType extends Comparable<AnyType>>  implements DictionaryInterface<AnyType>
{
	/*
	 * Keeps track of the number of elements in the dictionary.
	 * Take a look at the implementation of size()
	 */
	private int size;
	/*
	 * The head reference to the linked list of Nodes.
	 * Take a look at the Node class.
	 */
	private Node head;

	/**
	 * Creates an empty dictionary.
	 */
	public Dictionary()
	{
		size = 0;
		head = null;
	}

	/**
	 * Adds e to the dictionary, thus making contains(e) true.
	 * Increments size so as to ensure size() is correct.
	 */
	public void add(AnyType e)
	{
		if(e == null)
		{
			return;
		}
		Comparable [] temp = new Comparable[1];
		temp[0] = e;
		size++;
		if(head == null)
		{
			head = new Node(0,temp, null);
			return;
		}		

		Node new_node = new Node(0, temp, head);
		head = new_node;
		
		mergeDown();
		
		/*
		 * Your code goes here...
		 */

		//throw new RuntimeAnyTypexception("You need to implement this method!");
	}

	/**
	 * Removes e from the dictionary.  If contains(e) was formerly false,
	 * it is still false.
	 * Otherwise, decrements size so as to ensure size() is correct.
	 */
	public void remove(AnyType e)
	{
		if(e == null || contains(e) == false)
		{
			return;
		}
		else if(!contains(e) )
		{
			return;
		}
		
		Node temp = head;
		while(!temp.contains(e))
		{
			temp = temp.next;
		}
		if(head.array.length == 1 && temp == head)
		{
			head = head.next;
			size --;
			return;
		}
		
		Comparable [] new_list = new Comparable[temp.array.length];
		Boolean seen = false;
		Integer j = 0;
		for(Integer i = 0; i < temp.array.length; i++)
		{
			if(temp.array[i].compareTo(e) == 0 && !seen)
			{
				seen = true;
				continue;
			}
			new_list[j] = temp.array[i];
			j++;
		}
		temp.array = new_list;
		System.out.print(this);
		if(temp != head)
		{
			System.out.print("ENYERS");

			Comparable [] temp_list = new Comparable[1];
			temp_list[0] = head.array[head.array.length -1];
			Comparable [] temp_head_list = new Comparable[head.array.length - 1];
			for(Integer i = 0; i < head.array.length -1; i ++)
			{
				temp_head_list[i] = head.array[i];
			}
			head.array = temp_head_list;
			
			temp.array = merge(temp_list,temp.array);
			System.out.print(this);
			if(temp_head_list.length == 0)
			{
				head= head.next;
				return;
			}
		}
		java.util.Queue<Comparable[]> q = splitUp(head.array, head.power);


		/*
		 * Your code goes here...
		 */
		
		//throw new RuntimeAnyTypexception("You need to implement this method!");
	}

	/**
	 * Returns true iff the dictionary contains an element equal to e.
	 */
	public boolean contains(AnyType e)
	{
		if(e == null)
		{
			return false;
		}
		Node temp = head;
		while(temp != null)
		{
			if(temp.contains(e) )
			{
				return true;
			}
			
			temp = temp.next;
		}
		/*
		 * Your code goes here...
		 */

		return false;
	}

	/**
	 * Returns the number of elements in the dictionary equal to e.
	 * This is logically equivalent to the number of times remove(e) needs to be performed
	 * in order for contains(e) to be false.
	 */
	@SuppressWarnings("unchecked")
	public int frequency(AnyType e)
	{
		if(e == null || head == null)
		{
			return 0;
		}
		if(!contains(e))
		{
			return 0;
		}
		
		Node temp = head;
		Integer count = 0;
		while (temp != null)
		{
			if(temp.contains(e))
			{
				for(Integer i = 0; i < temp.array.length; i++)
				{
					if(temp.array[i].compareTo(e) == 0)
					{
						count += 1;
					}
				}
			}			
			temp = temp.next;
		}

		/*
		 * Your code goes here...
		 */

		return count;
	}

	/**
	 * Returns the size of the dictionary.
	 */
	public int size()
	{
		return size;
	}

	/**
	 * Combines with the other AAD using the algorithm discussed in lecture.
	 *
	 * Formally, the following need to be true after combining an AAD with another AAD:
	 * 		-the resulting dictionary contains an item iff it was contained in either of the two dictionaries
	 * 		-the resulting frequency of any item is the sum of its frequency in the two dictionaries
	 * 		-the resulting size is the sum of the two sizes
	 */
	public void combine(Dictionary<AnyType> other)
	{
		if(other == null || this == other)
		{
			return;
		}
		size = this.size + other.size;
		Node first_track = this.head;
		Node other_track = other.head;
		Node temp_head;

		if(first_track.power < other_track.power)
		{
			temp_head = new Node(first_track.power, first_track.array, null);
			first_track = first_track.next;
		}
		else {
			temp_head = new Node(other_track.power, other_track.array, null);
			other_track = other_track.next;
		}
		head = temp_head;
		while(first_track != null && other_track != null)
		{
			if(first_track.power < other_track.power)
			{
				temp_head.next = new Node(first_track.power, first_track.array, null);
				first_track = first_track.next;
			}
			else {
				temp_head.next = new Node(other_track.power, other_track.array, null);
				other_track = other_track.next;
			}
			temp_head = temp_head.next;
		}
		
		if(first_track != null)
		{
			while(first_track!= null)
			{
				temp_head.next = new Node(first_track.power, first_track.array, null);
				first_track = first_track.next;
				temp_head = temp_head.next;
			}
		}
		else {
			while(other_track!= null)
			{
				temp_head.next = new Node(other_track.power, other_track.array, null);
				other_track = other_track.next;
				temp_head = temp_head.next;
			}
		}
		
		mergeDown();
		/*
		 * Your code goes here...
		 */

	//	throw new RuntimeAnyTypexception("You need to implement this method!");
	}

	/**
	 * Returns a helpful string representation of the dictionary.
	 */
	public String toString()
	{
		String temp = "";
		Node curr = head;
		while (curr != null)
		{
			temp += Integer.toString(curr.power);
			temp += ":";
			temp += " ";
			temp += curr.toString();
			temp += "\n";
			curr = curr.next;
		}
		return temp;
	}


	/**
	 * Starting with the smallest array, mergeDown() merges arrays of the same size together until
	 * all the arrays have different size.
	 *
	 * This is very useful for implementing add(e)!!!  See the lecture notes for the theory behind this.
	 */
	private void mergeDown()
	{
		mergeHelp(head);
		//other();
	}
	private void mergeHelp(Node fix)
	{
		if(fix == null || fix.next == null)
		{
			return;
		}
		else if(fix.array.length != fix.next.array.length )
		{
			return;
		}
		else if(fix.next.next != null && fix.next.array.length == fix.next.next.array.length)
		{
			fix = fix.next;
			mergeHelp(fix);
			return;
		}
		
		fix.array = merge(fix.array, fix.next.array);
		fix.power += 1;
		fix.next = fix.next.next;
		if(fix.next == null)
		{
			return;
		}
		if(fix.array.length != fix.next.array.length)
		{
			fix = fix.next;
		}
		mergeHelp(fix);
	}


	/**
	 * Assumes a is sorted.
	 *
	 * contains(a, item) 	= -1, if there is no element of a equal to item
	 * 						= k, otherwise, where a[k] is equal to item
	 *
	 * This is needed for Node's indexOf(e)
	 *
	 * O(log(a.length))
	 */
	@SuppressWarnings("unchecked")
	public static int binarySearch(Comparable[] a, Comparable item)
	{
		/*
		 * Your code goes here...
		 */
		Integer start = 0;
		Integer end = a.length -1;
		while(start <= end)
		{
			Integer mid = start + (end - start)/2;
			if(a[mid].compareTo(item) == 0) {
				return mid;
			}
			else if ( a[mid].compareTo(item) == -1 )
			{
				start = mid +1;
			}
			else {
				end = mid-1;
			}
			
		}
		
		return -1;
	}

	/**
	 * Assumes a is sorted.
	 *
	 * Returns the number of elements of a equal to item.
	 *
	 * This is needed for Node's frequency(e).
	 *
	 * O(log(a.length) + frequency(item))
	 */
	@SuppressWarnings("unchecked")
	public static int frequency(Comparable[] a, Comparable item)
	{
		/*
		 * Your code goes here...
		 */
		if(binarySearch(a,item) == -1)
		{
			return 0;
		}
		int counter = 0;
		for(int i = 0; i < a.length; i++)
		{
			if(a[i].equals(item))
			{
				counter +=1;
			}
		}

		return counter;
	}

	/**
	 * When a and b are sorted arrays, merge(a,b) returns a sorted array
	 * that has length (a.length+b.length) than contains the elements
	 * of a and the elements of b.
	 *
	 * This is useful for implementing the mergeDown() method.
	 *
	 * O(a.length + b.length)
	 */
	@SuppressWarnings("unchecked")
	public static Comparable[] merge(Comparable[] a, Comparable[] b)
	{
		/*
		 * Your code goes here...
		 */
		Comparable[] combined = new Comparable[a.length + b.length];
		int a_count = 0;
		int b_count = 0;
		int counter = 0;
		while(a_count < a.length && b_count < b.length )
		{
			if(a[a_count].compareTo(b[b_count]) == -1)
			{
				combined[counter] = a[a_count];
				a_count +=1;
			}
			else if(a[a_count].compareTo(b[b_count]) == 1)
			{
				combined[counter] = b[b_count];
				b_count +=1;
			}
			else {
				combined[counter] = b[b_count];
				b_count +=1;
			}
			counter += 1;
		}
		if(a_count < a.length)
		{
			while(a_count < a.length)
			{
				combined[counter] = a[a_count];
				a_count +=1;
				counter += 1;
			}
		}
		else if(b_count < b.length)
		{
			while(b_count < b.length)
			{
				combined[counter] = b[b_count];
				b_count +=1;
				counter += 1;
			}
		}
		

		return combined;
	}

	/**
	 * Returns base^exponent.  This is useful for implementing splitUp(a,k)
	 */
	private static int power(int base, int exponent)
	{
		return (int)(Math.pow(base, exponent));
	}

	/**
	 * Assumes a.length >= 2^k - 1, for the given k.
	 *
	 * Splits the first (2^k -1) elements of a up into k-1 sorted arrays of
	 * length 2^(k-1), 2^(k-2), ..., 2, 1.
	 * Returns a Queue of these arrays (in  above order, i.e. the one with
	 * length 2^(k-1) is at the front).
	 *
	 * This is useful for implementing remove(e) using the algorithm discussed in class.
	 *
	 * O(a.length)
	 */
	@SuppressWarnings("unchecked")
	public static java.util.Queue<Comparable[]> splitUp(Comparable[] a, int k)
	{
		/*
		 * We'll just use a LinkedList as a Queue in this fashion.  Take a look at the
		 * API for the java.util.Queue interface.
		 */
		int temp_k = k;
		
		java.util.Queue<Comparable[]> q = new java.util.LinkedList<Comparable[]>();
		int i = 0;
		while(temp_k >= 0)
		{
			int counter = 0;
			Comparable[] temp_array = new Comparable[power(2,k-1)];
			while(counter < (power(2, temp_k-1) ) && i < a.length)
			{
				temp_array[counter] = a[i];
				i++;
				counter++; 
			}
			if(i== a.length)
			{
				break;
			}
			System.out.print("-------ARRAY---->" + "\n");

			for(int j = 0; j < temp_array.length; j ++)
			{
				System.out.print(temp_array[j] + "    ");

			}
			System.out.print("-------ARRAY---->" + "\n");

			q.add(temp_array);
			temp_k--;
		}
		/*
		 * Your code goes here...
		 */
		return q;
	//	throw new RuntimeAnyTypexception("You need to implement this method!");
	}

	/**
	 * Implementation of the underlying array-based data structure.
	 *
	 * AnyTypeach Node:
	 * 			-knows k, its "power"
	 * 			-has myArray, a sorted array of 2^k elements
	 * 			-knows myNext, the next Node in the linked list of Nodes
	 *
	 * You do *NOT* need to change this class.
	 * It is, however, very important that you understand how it works.
	 * You may add additional methods, although you have been provided with sufficient
	 * functionality needed to implement the dictionary.
	 */
	@SuppressWarnings("unchecked")
	private static class Node
	{
		private int power;
		private Comparable[] array;
		private Node next;

		/**
		 * Creates an Node with the specified parameters.
		 */
		public Node(int power, Comparable[] array, Node next)
		{
			this.power = power;
			this.array = array;
			this.next = next;
		}

		/**
		 * Returns 	-1, if there is no element in the array equal to e
		 * 			 k, otherwise, where array[k] is equal to e
		 */
		public int indexOf(Comparable e)
		{
			return Dictionary.binarySearch(array, e);
		}

		/**
		 * Returns	true, if there is an element in the array equal to e
		 * 			false, otherwise
		 */
		public boolean contains(Comparable e)
		{
			return indexOf(e) > -1;
		}

		/**
		 * Returns the number of elements in the array equal to e
		 */
		public int frequency(Comparable e)
		{
			return Dictionary.frequency(array, e);
		}

		/**
		 * Returns a useful representation of this Node.  (Note how this is used by Dictionary's toString()).
		 */
		public String toString()
		{
			return java.util.Arrays.toString(array);
		}
	}

}


