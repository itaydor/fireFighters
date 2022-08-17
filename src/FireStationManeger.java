import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FireStationManeger implements Runnable{

    private int totalEvents;
    private int numberOfFinishedEvents = 0;
    private int severeEvents = 0;
    private final List<DailyWorker> dailyWorkers;
    private final List<HasSalary> hasSalaries;

    public FireStationManeger(int totalEvents, List<Dispatcher> dispatchers, List<EventHandler> eventHandlers, List<StationWorker> stationWorkers, List<EventCommander> eventCommanders) {
        dailyWorkers = Stream.of(eventHandlers, stationWorkers, eventCommanders)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        hasSalaries = Stream.of(dispatchers, eventHandlers, stationWorkers)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        this.totalEvents = totalEvents;
        eventCommanders.forEach(ec -> ec.setManager(this));
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (numberOfFinishedEvents < totalEvents)
                    wait();
            }
            dailyWorkers.forEach(DailyWorker::endDay);
            double totalCost = hasSalaries.stream().mapToDouble(HasSalary::getSalary).sum();
            System.out.println("Manager: Total cost: " + totalCost);
            System.out.println("Manager: Number of events handled: " + totalEvents);
            System.out.println("Manager: Number of severe events: " + severeEvents);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public synchronized void update(boolean isSevere){
        if(isSevere)
            severeEvents++;
        numberOfFinishedEvents++;
        notify();
    }
}
