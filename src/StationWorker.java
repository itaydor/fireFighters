
public class StationWorker implements Runnable, DailyWorker, HasSalary{

    private final String name;
    private double salary = 100;
    private final double workingTime;
    private boolean isDayEnded = false;
    private final BoundedQueue<ReadyEvent> readyEvents;
    private final InformationSystem informationSystem;
    private final VehicleStock vehicleStock;

    public StationWorker(String name, double workingTime, BoundedQueue<ReadyEvent> readyEvents, InformationSystem informationSystem, VehicleStock vehicleStock) {
        this.name = name;
        this.workingTime = workingTime;
        this.informationSystem = informationSystem;
        this.readyEvents = readyEvents;
        this.vehicleStock = vehicleStock;
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
        while (!isDayEnded) {
            try {
                FireEvent fireEvent;
                synchronized (informationSystem) {
                    while (informationSystem.isEmpty() && !isDayEnded){
                        System.out.println(name + ": Waiting For Events.");
                        informationSystem.wait();
                    }
                    if(isDayEnded)
                        continue;
                    fireEvent = informationSystem.extract();
                }
                System.out.println(name + ": Handle Event " + fireEvent.getId());
                Thread.sleep((int)(workingTime * 1000));
//                Thread.sleep(1);
                salary += 3;
                readyEvents.insert(buildReadyEvent(fireEvent));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(name + ": Day Ended.");
    }

    private ReadyEvent buildReadyEvent(FireEvent fireEvent) {
        int state = fireEvent.getState();
        int numberOfTrucks = vehicleStock.calculateTrucksNeededByState(state);
        int numberOfPlanes = vehicleStock.calculatePlanesNeededByState(state);
        System.out.println(name + ": Building Ready Event: Address: " + fireEvent.getAddress()
                + ", State: " + state + ", Trucks Needed: " + numberOfTrucks + ", Planes Needed: " + numberOfPlanes);
        return new ReadyEvent(fireEvent.getAddress(), state, fireEvent.getArea(), numberOfTrucks, numberOfPlanes);
    }

    @Override
    public void endDay() {
        isDayEnded = true;
        synchronized (informationSystem){
            informationSystem.notifyAll();
        }
    }
}
