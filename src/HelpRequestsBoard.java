import java.util.Vector;

public class HelpRequestsBoard {

    private final Vector<HelpRequest> board;

    public HelpRequestsBoard() {
        this.board = new Vector<>();
    }

    public synchronized void putRequest(EventCommander commander, int missionTime){
        board.add(new HelpRequest(commander, missionTime));
    }

    public synchronized void removeRequest(EventCommander commander){
        board.stream().filter(hr -> hr.getCommander().equals(commander)).findFirst().ifPresent(board::remove);
    }

    public synchronized HelpRequest help(){
        if(board.isEmpty())
            return null;
        return board.remove(0);
    }
}
