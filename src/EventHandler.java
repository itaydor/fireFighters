import java.util.Random;
import java.util.regex.Pattern;

public class EventHandler implements Runnable, DailyWorker, HasSalary{

    private final String name;
    private double salary = 0;
    private boolean isDayEnded = false;
    private final Queue<FireEvent> fireEvents;
    private final InformationSystem informationSystem;

    public EventHandler(String name, Queue<FireEvent> fireEvents, InformationSystem informationSystem) {
        this.name = name;
        this.fireEvents = fireEvents;
        this.informationSystem = informationSystem;
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
        while(!isDayEnded){
            try {
                FireEvent fireEvent;
                synchronized (fireEvents){
                    while(fireEvents.isEmpty() && !isDayEnded){
                        System.out.println(name + ": Waiting For Events.");
                        fireEvents.wait();
                    }
                    if(isDayEnded)
                        continue;
                    fireEvent = fireEvents.extract();
                }
                System.out.println(name + ": handle Event " + fireEvent.getId());
                Thread.sleep(3000);
//                Thread.sleep(1);
                updateAddress(fireEvent);
                salary += 3;
                informationSystem.insert(fireEvent);
                System.out.println("Notice - New Emergency " + fireEvent.getId() + " In " + fireEvent.getAddress());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(name + ": Day Ended.");
    }

    private void updateAddress(FireEvent fireEvent) {
        String address = fireEvent.getAddress();
        int distance;
        String[] splitedAddress = address.split(" ");
        if(isInteger(splitedAddress[0])){
            distance = numberOfWordsToDistance(splitedAddress.length - 1) + Integer.parseInt(splitedAddress[0]);
            fireEvent.setAddress(address.replaceFirst(splitedAddress[0], "" + distance));
        }
        else{
            distance = numberOfWordsToDistance(splitedAddress.length) + 8 + new Random().nextInt(13);
            fireEvent.setAddress(distance + " " + address);
        }
    }

    private int numberOfWordsToDistance(int numberOfWords) {
        Random random = new Random();
        int distance;
        switch (numberOfWords){
            case 1 -> distance = 1 + random.nextInt(5);
            case 2 -> distance = 3 + random.nextInt(5);
            default -> distance = 5 + random.nextInt(6);
        }
        return distance;
    }

    private boolean isInteger(String string) {
        Pattern pattern = Pattern.compile("\\d+");
        return pattern.matcher(string).matches();
    }

    @Override
    public void endDay(){
        isDayEnded = true;
        synchronized (fireEvents){
            fireEvents.notifyAll();
        }
    }
}
