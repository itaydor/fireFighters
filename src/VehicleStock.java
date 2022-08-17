public class VehicleStock {

    private int availableTrucks;
    private int availablePlanes;

    public VehicleStock(int availableTrucks, int availablePlanes) {
        this.availableTrucks = availableTrucks;
        this.availablePlanes = availablePlanes;
    }

    public int calculatePlanesNeededByState(int state){
        int planes;
        switch (state){
            case 3 -> planes = 1;
            case 4 -> planes = 2;
            case 5 -> planes = 3;
            default -> planes = 0;
        }
        return planes;
    }

    public synchronized int calculateTrucksNeededByState(int state){
        int trucks = 0;
        switch (state){
            case 1 -> trucks = Math.min(4, Math.max(2, availableTrucks));
            case 2 -> trucks = Math.min(5, Math.max(2, availableTrucks) + 1);
            case 3 -> trucks = Math.min(7, Math.max(2, availableTrucks) + 3);
            case 4 -> trucks = Math.min(9, Math.max(2, availableTrucks) + 5);
            case 5 -> trucks = Math.min(12, Math.max(8, availableTrucks) + 1);
        }
        return trucks;
    }

    public synchronized void getVehicles(int trucks, int planes) throws InterruptedException {
        while(!hasEnoughVehicles(trucks, planes))
            wait();
        availableTrucks -= trucks;
        availablePlanes -= planes;
    }

    public synchronized void retrieveVehicles(int trucks, int planes){
        availableTrucks += trucks;
        availablePlanes += planes;
        notifyAll();
    }

    private synchronized boolean hasEnoughVehicles(int trucks, int planes){
        return availablePlanes >= planes && availableTrucks >= trucks;
    }
}
