public class BoundedQueue<T> extends Queue<T>{

    private final int maxCapacity;

    public BoundedQueue(int maxCapacity) {
        super();
        this.maxCapacity = maxCapacity;
    }

    @Override
    public synchronized void insert(T item) {
        while (queue.size() == maxCapacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        queue.add(item);
        notifyAll();
    }

    @Override
    public synchronized T extract() throws InterruptedException {
        while (queue.isEmpty())
            wait();
        notifyAll();
        return queue.remove(0);
    }
}
