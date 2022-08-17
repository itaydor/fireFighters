import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI implements ActionListener {

    private JFrame frame = new JFrame();
    private JTextField commanderTextField = new JTextField();
    private JTextField workTimeTextField = new JTextField();
    private JTextField trucksTextField = new JTextField();
    private JTextField planesTextField = new JTextField();
    private JButton startButton = new JButton("START");
    private JButton exitButton = new JButton("EXIT");
    private StationBuilder stationBuilder;

    public GUI() {
        JLabel header = new JLabel("Welcome To FatmaUnit Fire-Station");
        header.setBounds(100, 20 , 400, 10);
        header.setFont(new Font("MV Boli", Font.BOLD, 14));
        header.setOpaque(true);

        JLabel commanderLabel = new JLabel("number of event commander to add");
        commanderLabel.setBounds(10, 100 , 235, 10);
        commanderLabel.setFont(new Font("MV Boli", Font.BOLD, 13));
        commanderLabel.setOpaque(true);

        JLabel workTimeLabel = new JLabel("work time for station worker");
        workTimeLabel.setBounds(270, 100 , 270, 10);
        workTimeLabel.setFont(new Font("MV Boli", Font.BOLD, 13));
        workTimeLabel.setOpaque(true);

        JLabel trucksLablel = new JLabel("number of fire trucks to add");
        trucksLablel.setBounds(10, 200 , 235, 10);
        trucksLablel.setFont(new Font("MV Boli", Font.BOLD, 13));
        trucksLablel.setOpaque(true);

        JLabel planesLablel = new JLabel("number of fire planes to add");
        planesLablel.setBounds(270, 200 , 270, 10);
        planesLablel.setFont(new Font("MV Boli", Font.BOLD, 13));
        planesLablel.setOpaque(true);

        commanderTextField.setBounds(70, 150 , 100, 20);
        commanderTextField.setText("0");


        workTimeTextField.setBounds(310, 150 , 100, 20);
        workTimeTextField.setText("1");

        trucksTextField.setBounds(70, 250 , 100, 20);
        trucksTextField.setText("0");

        planesTextField.setBounds(310, 250 , 100, 20);
        planesTextField.setText("0");

        startButton.setBounds(70, 300 , 100, 20);
        startButton.addActionListener(this);

        exitButton.setBounds(310, 300 , 100, 20);
        exitButton.addActionListener(this);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(null);

        frame.add(header);
        frame.add(commanderLabel);
        frame.add(workTimeLabel);
        frame.add(trucksLablel);
        frame.add(planesLablel);

        frame.add(commanderTextField);
        frame.add(workTimeTextField);
        frame.add(trucksTextField);
        frame.add(planesTextField);

        frame.add(startButton);
        frame.add(exitButton);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new GUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startButton){
            int commanders = Integer.parseInt(commanderTextField.getText());
            int trucks = Integer.parseInt(trucksTextField.getText());
            int planes = Integer.parseInt(planesTextField.getText());
            double workTime = Double.parseDouble(workTimeTextField.getText());
            stationBuilder = new StationBuilder(commanders, trucks, planes, workTime);
            stationBuilder.start();
        }
        if(e.getSource() == exitButton)
            System.exit(0);
    }
}
