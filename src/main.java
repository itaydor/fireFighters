import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class main {

    public static void main(String[] args) throws IOException {

        List<Call> listOfCalls = new ArrayList<>();
        Queue<Call> calls = new Queue<>();
        BufferedReader inFile=null;
        try
        {
            FileReader fr = new FileReader ("input.txt");
            inFile = new BufferedReader (fr);
            inFile.readLine();
            String line = inFile.readLine();
            do{
                String[] splitedLine = line.split("\t");
                int state = Integer.parseInt(splitedLine[0]);
                int area = Integer.parseInt(splitedLine[1]);
                double time = Double.parseDouble(splitedLine[2]);
                double arrival = Double.parseDouble(splitedLine[3]);
                String address = splitedLine[4];
                listOfCalls.add(new Call(address, state, area, time, arrival, calls));
                line = inFile.readLine();
            } while (line != null);
        }

        catch (FileNotFoundException exception)
        {
            System.out.println ("The file input.txt was not found.");
        } finally{
            inFile.close();
        }

        Queue<FireEvent> fireEvents = new Queue<>();
        BoundedQueue<ReadyEvent> readyEvents = new BoundedQueue<>(15);
        SynchronousCounter synchronousCounter = new SynchronousCounter(200);
        InformationSystem informationSystem = new InformationSystem();
        VehicleStock vehicleStock = new VehicleStock(50, 10);
        HelpRequestsBoard helpRequestsBoard = new HelpRequestsBoard();

        Dispatcher dispatcher1 = new Dispatcher("d1", 1, calls, fireEvents, synchronousCounter);
        Dispatcher dispatcher2 = new Dispatcher("d2", 1, calls, fireEvents, synchronousCounter);
        Dispatcher dispatcher3 = new Dispatcher("d3", 1, calls, fireEvents, synchronousCounter);
        Dispatcher dispatcher4 = new Dispatcher("d4", 1, calls, fireEvents, synchronousCounter);
        Dispatcher dispatcher5 = new Dispatcher("d5", 1, calls, fireEvents, synchronousCounter);

        EventHandler eventHandler1 = new EventHandler("ev1", fireEvents, informationSystem);
        EventHandler eventHandler2 = new EventHandler("ev2", fireEvents, informationSystem);
        EventHandler eventHandler3 = new EventHandler("ev3", fireEvents, informationSystem);

        StationWorker stationWorker1 = new StationWorker("sw1", 1, readyEvents, informationSystem, vehicleStock);
        StationWorker stationWorker2 = new StationWorker("sw2", 1, readyEvents, informationSystem, vehicleStock);
        StationWorker stationWorker3 = new StationWorker("sw3", 1, readyEvents, informationSystem, vehicleStock);

        EventCommander eventCommander1 = new EventCommander("ec1", readyEvents, vehicleStock, helpRequestsBoard);
        EventCommander eventCommander2 = new EventCommander("ec2", readyEvents, vehicleStock, helpRequestsBoard);
        EventCommander eventCommander3 = new EventCommander("ec3", readyEvents, vehicleStock, helpRequestsBoard);
        EventCommander eventCommander4 = new EventCommander("ec4", readyEvents, vehicleStock, helpRequestsBoard);
        EventCommander eventCommander5 = new EventCommander("ec5", readyEvents, vehicleStock, helpRequestsBoard);

        List<Dispatcher> dispatchers = Arrays.asList(dispatcher1, dispatcher2, dispatcher3, dispatcher4, dispatcher5);
        List<EventHandler> eventHandlers = Arrays.asList(eventHandler1, eventHandler2, eventHandler3);
        List<StationWorker> stationWorkers = Arrays.asList(stationWorker1, stationWorker2, stationWorker3);
        List<EventCommander> eventCommanders = Arrays.asList(eventCommander1, eventCommander2, eventCommander3, eventCommander4, eventCommander5);
        FireStationManeger fireStationManeger = new FireStationManeger(200, dispatchers, eventHandlers, stationWorkers, eventCommanders);

        eventCommander1.setManager(fireStationManeger);
        eventCommander2.setManager(fireStationManeger);
        eventCommander3.setManager(fireStationManeger);
        eventCommander4.setManager(fireStationManeger);
        eventCommander5.setManager(fireStationManeger);

        Thread tDis1 = new Thread(dispatcher1);
        Thread tDis2 = new Thread(dispatcher2);
        Thread tDis3 = new Thread(dispatcher3);
        Thread tDis4 = new Thread(dispatcher4);
        Thread tDis5 = new Thread(dispatcher5);

        Thread tEh1 = new Thread(eventHandler1);
        Thread tEh2 = new Thread(eventHandler2);
        Thread tEh3 = new Thread(eventHandler3);

        Thread tSw1 = new Thread(stationWorker1);
        Thread tSw2 = new Thread(stationWorker2);
        Thread tSw3 = new Thread(stationWorker3);

        Thread tEc1 = new Thread(eventCommander1);
        Thread tEc2 = new Thread(eventCommander2);
        Thread tEc3 = new Thread(eventCommander3);
        Thread tEc4 = new Thread(eventCommander4);
        Thread tEc5 = new Thread(eventCommander5);

        Thread tFm = new Thread(fireStationManeger);

        tDis1.start();
        tDis2.start();
        tDis3.start();
        tDis4.start();
        tDis5.start();

        tEh1.start();
        tEh2.start();
        tEh3.start();

        tSw1.start();
        tSw2.start();
        tSw3.start();

        tEc1.start();
        tEc2.start();
        tEc3.start();
        tEc4.start();
        tEc5.start();

        tFm.start();

        for (Call call : listOfCalls) {
            Thread t = new Thread(call);
            t.start();
        }

    }
}
