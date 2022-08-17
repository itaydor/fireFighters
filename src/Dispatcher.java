import java.util.Random;

public class Dispatcher implements Runnable, HasSalary{

    private final String name;
    private double salary;
    private final SynchronousCounter counter;
    private final Queue<Call> calls;
    private final Queue<FireEvent> fireEvents;

    public Dispatcher(String name, double salary, Queue<Call> calls, Queue<FireEvent> fireEvents, SynchronousCounter counter) {
        this.calls = calls;
        this.fireEvents = fireEvents;
        this.name = name;
        this.salary = salary;
        this.counter = counter;
    }

    public String getName() {
        return name;
    }

    @Override
    public double getSalary() {
        return salary;
    }

    @Override
    public void run() {
        System.out.println(name + ": Day Started.");
        while (!counter.isFinished()) {
            try {
                Call call;
                int id;
                synchronized (calls) {
                    while (calls.isEmpty() && !counter.isFinished()){
                        System.out.println(name + ": Waiting For Calls.");
                        calls.wait();
                    }
                    if (counter.isFinished())
                        continue;
                    call = calls.extract();
                    id = counter.incAndGet();
                    if(counter.isFinished())
                        calls.notifyAll();
                }
                System.out.println(name + ": Handle Call " + id + " Location: " + call.getAddress());
                handleCall(call, id);
                System.out.println(name + ": Ended Handling Call " + id);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(name + ": Day Ended.");
    }

    private void handleCall(Call call, int id) throws InterruptedException {
        Random random = new Random();
        double workingTime = random.nextInt(2) + 1 + call.getTime();
        Thread.sleep((int) (workingTime * 1000));
//        Thread.sleep(1);
        salary += workingTime * 3 + 0.5;
        FireEvent fireEvent = new FireEvent(id, call.getAddress(), call.getState(), call.getArea(), call.getArrival());
        fireEvents.insert(fireEvent);
        call.endCall();
    }
}
