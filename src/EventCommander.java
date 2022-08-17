
public class EventCommander implements Runnable, DailyWorker{

    private boolean isDayEnded = false;
    private final String name;
    private final BoundedQueue<ReadyEvent> readyEvents;
    private final VehicleStock vehicleStock;
    private FireStationManeger manager;
    private final HelpRequestsBoard helpRequestsBoard;

    public EventCommander(String name, BoundedQueue<ReadyEvent> readyEvents, VehicleStock vehicleStock, HelpRequestsBoard helpRequestsBoard) {
        this.name = name;
        this.readyEvents = readyEvents;
        this.vehicleStock = vehicleStock;
        this.helpRequestsBoard = helpRequestsBoard;
    }

    public void setManager(FireStationManeger manager){
        this.manager = manager;
    }

    @Override
    public void run() {
        System.out.println(name + ": Day Started.");
        while (!isDayEnded){
            try {
                ReadyEvent readyEvent = null;
                HelpRequest help = null;
                synchronized (readyEvents){
                    while (readyEvents.isEmpty() && !isDayEnded){
                        help = helpRequestsBoard.help();
                        if(help != null)
                            break;
                        System.out.println(name + ": Waiting For Events.");
                        readyEvents.wait();
                    }
                    if(isDayEnded)
                        continue;
                    if(help == null)
                        readyEvent = readyEvents.extract();
                }
                if(help != null){
                    System.out.println(name + ": join to " + help.getCommander().name);
                    Thread.sleep(help.getTime());
                    continue;
                }
                System.out.println(name + ": handling event in location: " + readyEvent.getAddress());
                int distance = Integer.parseInt(readyEvent.getAddress().split(" ")[0]);
                vehicleStock.getVehicles(readyEvent.getNumberOfTrucks(), readyEvent.getNumberOfPlanes());
                int missionTime = (int)(calculateMissionTime(
                        readyEvent.getState(),
                        distance,
                        readyEvent.getNumberOfTrucks(),
                        readyEvent.getNumberOfPlanes()) * 1000);
                if(readyEvent.getState() == 5) {
                    helpRequestsBoard.putRequest(this, missionTime);
                    System.out.println(name + ": Ask for help handling severe event.");
                }
                Thread.sleep(missionTime);
//                Thread.sleep(1);
                helpRequestsBoard.removeRequest(this);
                vehicleStock.retrieveVehicles(readyEvent.getNumberOfTrucks(), readyEvent.getNumberOfPlanes());
                System.out.println(name + ": Event in location " + readyEvent.getAddress() +" Ended, Updating manager.");
                manager.update(readyEvent.getState() == 5);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        System.out.println(name + ": Day Ended.");
    }

    private double calculateMissionTime(int state, int distance, int trucks, int planes){
        return (state * 2.0 + distance) / (trucks + planes);
    }

    @Override
    public void endDay() {
        isDayEnded = true;
        synchronized (readyEvents){
            readyEvents.notifyAll();
        }
    }
}
