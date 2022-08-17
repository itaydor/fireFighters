
public class Call implements Runnable{

    private final String address;
    private final int state;
    private final int area;
    private final double time;
    private final double arrival;
    private final Queue<Call> calls;

    public Call(String address, int state, int area, double time, double arrival, Queue<Call> calls) {
        this.address = address;
        this.state = state;
        this.area = area;
        this.time = time;
        this.arrival = arrival;
        this.calls = calls;
    }

    public String getAddress() {
        return address;
    }

    public int getState() {
        return state;
    }

    public int getArea() {
        return area;
    }

    public double getTime() {
        return time;
    }

    public double getArrival() {
        return arrival;
    }

    @Override
    public void run() {
        try {
            Thread.sleep((int)(arrival * 1000));
            System.out.println(address + " Calling... ");
//            Thread.sleep(1);
            call();
            System.out.println(address + " Call ended.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void call(){
        calls.insert(this);
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void endCall(){
        notify();
    }

}
