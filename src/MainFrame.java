import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;

public class MainFrame extends JFrame {

    static boolean isSameNetwork;
    final static String[] callInfo = new String[3]; // Holds info about the call
    private static Instant timeStarted;
    final float[] timeTaken = { 0 };// Store the time call lasted
    Container c = getContentPane();
    int hrs = 0;
    int  min = 0;
    int sec = 0;


    public MainFrame(String title) {
        super(title);

        //build();

        //set layout manager
        setLayout(new BorderLayout());

        //create swing components
        JPanel netSelect = new JPanel();
        //JPanel timeShow = new JPanel();
        ButtonGroup netSelection = new ButtonGroup();
        JLabel mainScreenText = new JLabel("Choose a network:", JLabel.CENTER);
        JLabel timeDisplay = new JLabel("00:00:00", JLabel.CENTER);
        JRadioButton net1 = new JRadioButton("Net1", true);
        JRadioButton net2 = new JRadioButton("Net2");
        JRadioButton net3 = new JRadioButton("Net3");
        JButton makeCall = new JButton("Make Call");
        JButton endCall = new JButton("End Call");

        //add components to content pane
        //Container c = getContentPane();

        //formatting
        mainScreenText.setFont(new Font("Verdana", Font.PLAIN, 30));
        timeDisplay.setFont(new Font("Verdana", Font.PLAIN, 60));
        net1.setFont(new Font("Arial", Font.PLAIN, 20));
        net2.setFont(new Font("Arial", Font.PLAIN, 20));
        net3.setFont(new Font("Arial", Font.PLAIN, 20));
        makeCall.setFont(new Font("Calibri", Font.PLAIN, 25));
        endCall.setFont(new Font("Calibri", Font.PLAIN, 25));

        //add radioButtons to button group
        netSelection.add(net1);
        netSelection.add(net2);
        netSelection.add(net3);

        // add radioButtons to netSelect panel
        netSelect.add(net1);
        netSelect.add(net2);
        netSelect.add(net3);


        //add to content pane
        c.add(mainScreenText, BorderLayout.NORTH);
        c.add(netSelect, BorderLayout.CENTER);
        c.add(makeCall, BorderLayout.SOUTH);


        //timer class
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                //timeDisplay.setText(sdf.format(new java.util.Date()));
                if(min==60 && sec==60){
                    hrs += 1;
                    min = 0;
                    sec = 0;
                }
                if (sec==60){
                    min += 1;
                    sec = 0;
                }
                if(hrs<10 && min<10 && sec<10){
                    timeDisplay.setText("0" + hrs + ":" + "0" + min + ":" + "0" + sec);
                } else if(min<10 && sec<10){
                    timeDisplay.setText("0" + hrs + ":" + "0" + min + ":" + "0" + sec);
                }else if(min>59 && sec<10){
                    timeDisplay.setText("0" + hrs + ":" + min + ":" + "0" + sec);
                } else if(sec<10){
                    timeDisplay.setText("0" + hrs + ":" + min + ":" + "0" + sec);
                } else if(min>10 && sec>10){
                    timeDisplay.setText("0" + hrs + ":" + min + ":" + sec);
                }else{
                    timeDisplay.setText("0" + hrs + ":" + "0" + min + ":" + sec);
                }
                sec++;
            }
        });

        //button behaviour
        makeCall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (net1.isSelected()) {
                    isSameNetwork = true;
                } else {
                    isSameNetwork = false;
                }
                //timeDisplay.setText("00:00:00");
                timeDisplay.setText("0" + hrs + ":" + "0" + min + ":" + "0" + sec);
                c.removeAll();
                c.add(timeDisplay, BorderLayout.NORTH);
                c.add(endCall, BorderLayout.SOUTH);
                c.revalidate();
                c.repaint();
                //JOptionPane.showMessageDialog(c,"c");
                start();
                timer.start();
            }
        });

        endCall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                stop();
                billing();
                int anotherCall = JOptionPane.showConfirmDialog(getContentPane(), "Do you want to make another call", "New Call",JOptionPane.YES_NO_OPTION);
                if (anotherCall == 0) {
                    hrs = 0;
                    min = 0;
                    sec = 0;
                    c.removeAll();
                    c.add(mainScreenText, BorderLayout.NORTH);
                    c.add(netSelect, BorderLayout.CENTER);
                    c.add(makeCall, BorderLayout.SOUTH);
                    c.revalidate();
                    c.repaint();
                } else {
                    JOptionPane.showMessageDialog(getContentPane(), "Thank you for using PicoNet");
                    dispose();
                }
            }
        });

    }

    public void start(){
        callInfo[0] = LocalTime.now().toString();
        timeStarted = Instant.now();
    }

    public void stop(){
        timeTaken[0] = (Duration.between(timeStarted, Instant.now()).toMillis() / 1000)-1;
        callInfo[1] = LocalTime.now().toString();
        callInfo[2] = Double.toString(timeTaken[0]);

    }

    public void billing() {
        float tax = 0;
        float timeTaken = Float.parseFloat(callInfo[2]);
        float callCharge = 0;
        float totalPrice;
        if (isSameNetwork) {
            if (Integer.parseInt(callInfo[0].split(":")[0]) > 6
                    && Integer.parseInt(callInfo[0].split(":")[0]) <= 18)
                callCharge += timeTaken * 4 / 60;
            else {
                callCharge += timeTaken * 3 / 60;
            }
        } else {
            callCharge += timeTaken * 5 / 60;
        }
        if (timeTaken > 2)
            tax = (float) (0.16 * callCharge);// Adding VAT
        totalPrice = tax + callCharge;
        JOptionPane.showMessageDialog(null,
                "Time call made :  " + callInfo[0] + "\n\nTime call ended :  " + callInfo[1]
                        + "\n\nTime elapsed :  " + callInfo[2] + "Seconds.\n\nPrice for call is :  "
                        + callCharge + " Ksh\n\nVAT added :  " + tax + "Ksh\n\nTotal Cost :  " + totalPrice + "Ksh");


    }
}
