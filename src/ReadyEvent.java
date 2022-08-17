public class ReadyEvent {

    private final String address;
    private final int state;
    private final int area;
    private final int numberOfTrucks;
    private final int numberOfPlanes;

    public ReadyEvent(String address, int state, int area, int numberOfTrucks, int numberOfPlanes) {
        this.address = address;
        this.state = state;
        this.area = area;
        this.numberOfTrucks = numberOfTrucks;
        this.numberOfPlanes = numberOfPlanes;
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

    public int getNumberOfTrucks() {
        return numberOfTrucks;
    }

    public int getNumberOfPlanes() {
        return numberOfPlanes;
    }
}
