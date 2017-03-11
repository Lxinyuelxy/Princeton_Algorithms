import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
	private Node first;
	private Node last;
	private int N;
	
	private class Node{
		Item item;
		Node next;
		Node before;
	}

	// construct an empty deque
	public Deque(){                           
		first = last;
	}
	
	// is the deque empty?
	public boolean isEmpty(){
		return N == 0;
	}
	
	// return the number of items on the deque
	public int size(){
		return N;
	}
	
	// add the item to the front
	public void addFirst(Item item){
		checkadd(item);
		Node oldfirst = first;
		first = new Node();
		first.item = item;
		first.next = oldfirst;
		if(isEmpty()){
			last = first;
		}else{
			oldfirst.before = first;
		}
		N++;
	}
	
	// add the item to the end
	public void addLast(Item item){
		checkadd(item);
		Node oldlast = last;
		last = new Node();
		last.item = item;
		last.before = oldlast;
		if(isEmpty()){
			first = last;
		}else{
			oldlast.next = last;
		}
		N++;
	}
	
	// remove and return the item from the front
	public Item removeFirst(){
		checkremove();
		Item item = first.item;
		first = first.next;
		N--;
		if(isEmpty()){
			last = first;
		}else{
			first.before = null;
		}
		return item;
	}
	
	// remove and return the item from the end
	public Item removeLast(){
		checkremove();
		Item item = last.item;
		last = last.before;
		N--;
		if(isEmpty()){
			first = last;
		}else{
			last.next = null;
		}	
		return item;
	}
	
	private void checkadd(Item item){
		if(item == null){
			throw new java.lang.NullPointerException("exception");
		}
	}
	
	private void checkremove(){
		if(isEmpty()){
			throw new java.util.NoSuchElementException("exception");
		}
	}
	
	// return an iterator over items in order from front to end
	public Iterator<Item> iterator(){
		return new ListIterator();
	}
	private class ListIterator implements Iterator<Item>{
		private Node current = first;
		public boolean hasNext() {
			return current != null;
		}

		public Item next() {
			if(!hasNext())
				throw new java.util.NoSuchElementException("exception");
			Item item = current.item;
			current = current.next;
			return item;
		}
		
		public void remove(){
			throw new java.lang.UnsupportedOperationException("exception");
		}
		
	}
	
}
