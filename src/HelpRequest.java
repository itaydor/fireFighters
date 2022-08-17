public class HelpRequest{

    private EventCommander commander;
    private int time;

    public HelpRequest(EventCommander commander, int time){
        this.commander = commander;
        this.time = time;
    }

    public EventCommander getCommander() {
        return commander;
    }

    public int getTime() {
        return time;
    }
}
