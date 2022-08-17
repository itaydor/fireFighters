import java.util.Vector;

public class Queue<T>{

    protected final Vector<T> queue;

    public Queue() {
        this.queue = new Vector<>();
    }

    public synchronized void insert(T item){
        queue.add(item);
        notifyAll();
    }

    public synchronized T extract() throws InterruptedException {
        while (queue.isEmpty())
            wait();
        return queue.remove(0);
    }

    public synchronized boolean isEmpty(){
        return queue.isEmpty();
    }

}
