package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GenWindow extends JFrame {
    JButton start;
    JSpinner spinner;
    int questions; //the number of questions to generate for the exam

    public GenWindow(int num) {
        //start exam window
        setLayout(null);
        setTitle("Start Exam");
        setSize(new Dimension(200, 200));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setLocationRelativeTo(null);
        setResizable(false);
        //getContentPane().setLayout(new GridLayout());

        //set in the exam file: number of questions, halved until value is 10
        ArrayList<String> nums = new ArrayList<>();
        while (num > 9) {
            nums.add(Integer.toString(num));
            num /= 2;
        }
        String[] numQuestions = new String[nums.size()];
        int j = 0;
        for (int i = nums.size()-1; i > -1; i--) {
            numQuestions[i] = nums.get(j);
            j++;
        }
        SpinnerListModel monthModel = new SpinnerListModel(numQuestions);
        spinner = new JSpinner(monthModel);
        spinner.setBounds(50, 25, 100, 50);
        getContentPane().add(spinner);

        start = new JButton("Start");
        start.addActionListener(e -> {
            questions = Integer.parseInt(spinner.getValue().toString());
            setVisible(false);
        });
        start.setBounds(50, 100, 100, 50);
        getContentPane().add(start);
    }

    public int getQuestions() {
        return this.questions;
    }
}
