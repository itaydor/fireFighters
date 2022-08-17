import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StationBuilder {

    private final List<Runnable> runnables;

    public StationBuilder(int commanders, int trucks, int planes, double workTime) {

        int numberOfEvents = getNumberOfEvents();
        SynchronousCounter synchronousCounter = new SynchronousCounter(numberOfEvents);
        VehicleStock vehicleStock = new VehicleStock(50 + trucks, 10 + planes);
        HelpRequestsBoard helpRequestsBoard = new HelpRequestsBoard();

        Queue<Call> callQueue = new Queue<>();
        Queue<FireEvent> fireEventsQueue = new Queue<>();
        InformationSystem informationSystem = new InformationSystem();
        BoundedQueue<ReadyEvent> readyEventsQueue = new BoundedQueue<>(15);

        Random random = new Random();
        List<Dispatcher> dispatchers = IntStream.range(1, 6)
                .mapToObj(i -> new Dispatcher("Dispatcher - " + i, random.nextInt(5) + 1, callQueue, fireEventsQueue, synchronousCounter))
                .collect(Collectors.toList());

        List<EventHandler> eventHandlers = IntStream.range(1, 4)
                .mapToObj(i -> new EventHandler("EventHandler - " + i, fireEventsQueue, informationSystem))
                .collect(Collectors.toList());

        List<StationWorker> stationWorkers = IntStream.range(1, 4)
                .mapToObj(i -> new StationWorker("StationWorker - " + i, workTime, readyEventsQueue, informationSystem, vehicleStock))
                .collect(Collectors.toList());

        List<EventCommander> eventCommanders = IntStream.range(1,6 + commanders)
                .mapToObj(i -> new EventCommander("EventCommander - " + i, readyEventsQueue, vehicleStock, helpRequestsBoard))
                .collect(Collectors.toList());

        List<Call> calls = readFormFile(callQueue);

        FireStationManeger fireStationManeger = new FireStationManeger(numberOfEvents, dispatchers, eventHandlers, stationWorkers, eventCommanders);

        runnables = Stream.of(dispatchers, eventHandlers, stationWorkers, eventCommanders, Collections.singletonList(fireStationManeger), calls)
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    public void start(){
        runnables.stream().map(Thread::new).forEach(Thread::start);
    }

    private int getNumberOfEvents() {
        try(FileReader fr = new FileReader("input.txt")) {
            BufferedReader inFile = new BufferedReader(fr);
            return (int) (inFile.lines().count() - 1);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    private List<Call> readFormFile(Queue<Call> callQueue) {
        List<Call> calls = new ArrayList<>();
        try(FileReader fr = new FileReader("input.txt")) {
//            FileReader fr = new FileReader("input.txt");
            BufferedReader inFile = new BufferedReader(fr);
            inFile.readLine();
            String line = inFile.readLine();
            do {
                String[] splitedLine = line.split("\t");
                int state = Integer.parseInt(splitedLine[0]);
                int area = Integer.parseInt(splitedLine[1]);
                double time = Double.parseDouble(splitedLine[2]);
                double arrival = Double.parseDouble(splitedLine[3]);
                String address = splitedLine[4];
                calls.add(new Call(address, state, area, time, arrival, callQueue));
                line = inFile.readLine();
            } while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calls;
    }
}
