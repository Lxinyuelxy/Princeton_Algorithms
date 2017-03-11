import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private int N;
	private Item[] a;
	
    public RandomizedQueue() {
    	N = 0;
	    a = (Item[]) new Object[2];
    }
 
    public boolean isEmpty() {
    	return N == 0;
    }
 
    public int size() {
	    return N;
    }

    public void enqueue(Item item) {
    	checkadd(item);
	    if(size() == a.length) resize(a.length * 2);
	    a[N++] = item;
    }
  
    public Item dequeue() {
    	checkremove();
	    int index = StdRandom.uniform(N);
	    Item item = a[index];
	    a[index] = a[N - 1];
	    a[N - 1] = null;
	    N--;
	    if(!isEmpty() && N < a.length/4) resize(a.length/2);
	    return item;
    }
 
   public Item sample(){
	   checkremove();
	   int index = StdRandom.uniform(N);
	   return a[index];
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
   
   private void resize(int len){
	   Item[] temp = (Item[] )new Object[len];
	   for(int i = 0; i < N; i++){
		   temp[i] = a[i];
	   }
	   a = temp;
   }
   
   public Iterator<Item> iterator(){return new ListIterator();}
   
   private class ListIterator implements Iterator<Item>{
	   private int current = 0;
	   private Item[] arr = (Item[]) new Object[a.length];
	   
	   public ListIterator(){
		   for(int i = 0; i < N; i++){
			   arr[i] = a[i];
		   }
	   }
	   
	   public boolean hasNext() {
		   return current < N; 
	   }
	   
	   public Item next() {
		   if(!hasNext())
				throw new java.util.NoSuchElementException("exception");
		   int random = StdRandom.uniform(N);
		   Item item = arr[random];
		   Item temp = arr[random];
		   arr[random] = arr[current];
		   arr[current] = temp;
		   current++;
		   return item;
	   }
	   
	   public void remove() {
			throw new java.lang.UnsupportedOperationException("exception");	
	   }
   }
 
}
