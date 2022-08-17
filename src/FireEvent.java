public class FireEvent {

    private final int id;
    private String address;
    private final int state;
    private final int area;
    private final double arrival;

    public FireEvent(int id, String address, int state, int area, double arrival) {
        this.id = id;
        this.address = address;
        this.state = state;
        this.area = area;
        this.arrival = arrival;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public int getState() {
        return state;
    }

    public int getArea() {
        return area;
    }

    public double getArrival() {
        return arrival;
    }

    public String toString(){
        return address;
    }
}
