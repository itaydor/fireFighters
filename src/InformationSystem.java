public class InformationSystem {

    private final Queue<FireEvent> shortDistance;
    private final Queue<FireEvent> mediumDistance;
    private final Queue<FireEvent> longDistance;


    public InformationSystem() {
        shortDistance = new Queue<>();
        mediumDistance = new Queue<>();
        longDistance = new Queue<>();
    }

    public synchronized void insert(FireEvent fireEvent){
        int distance = Integer.parseInt(fireEvent.getAddress().split("")[0]);
        if(distance <= 10)
            shortDistance.insert(fireEvent);
        else if(distance <= 20)
            mediumDistance.insert(fireEvent);
        else
            longDistance.insert(fireEvent);
        notifyAll();
    }

    public synchronized FireEvent extract() throws InterruptedException {
        while (isEmpty())
            wait();
        if(!shortDistance.isEmpty())
            return shortDistance.extract();
        if (!mediumDistance.isEmpty())
            return mediumDistance.extract();
        return longDistance.extract();
    }

    public synchronized boolean isEmpty(){
        return shortDistance.isEmpty() && mediumDistance.isEmpty() && longDistance.isEmpty();
    }
}
