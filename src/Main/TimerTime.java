package Main;

import java.awt.event.*;
import java.text.NumberFormat;
import javax.swing.*;
import javax.swing.Timer;

public class TimerTime extends JLabel implements ActionListener {
    long time; // How many milliseconds remain in the countdown.
    Timer timer; // Updates the count every second
    NumberFormat format; // Format hours:minutes:seconds with leading zeros
    boolean outOfTime;

    public TimerTime(long remaining) {
        this.outOfTime = false;
        this.time = remaining;

        setHorizontalAlignment(SwingConstants.CENTER);
        setOpaque(true); // So label draws the background color

        format = NumberFormat.getNumberInstance();
        format.setMinimumIntegerDigits(2); // pad with 0 if necessary

        timer = new Timer(1000, this);
        timer.setInitialDelay(0);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (time >= 0) {
            time -= 1000;
            int hours = (int) (time / 3600000);
            int minutes = (int) (time / 60000) - (hours * 60);
            int seconds = (int) (time / 1000) - (hours * 3600) - (minutes * 60);
            setText("Time left: " + format.format(hours) + ":" + format.format(minutes) + ":" + format.format(seconds));

            if (time == 0) {
                timer.stop();
                this.outOfTime = true;
            }
        }
    }
}
