public class SynchronousCounter {

    private int counter;
    private final int maxValue;

    public SynchronousCounter(int maxValue) {
        this.counter = 0;
        this.maxValue = maxValue;
    }

    public synchronized int incAndGet(){
        if(counter == maxValue)
            return counter;
        return ++counter;
    }

    public synchronized boolean isFinished(){
        return counter == maxValue;
    }

}
