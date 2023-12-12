package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class MainWindow extends JFrame implements ActionListener {
    JPanel navigation;
    public JSplitPane mainSplit;
    String[] choiceOrders; //set randomized choice patterns
    Question[] questions; //the bank of questions
    String[] answers; //the user's answers
    String[] realAnswers; //the correct answers
    private int question; //the current question (1-n)
    int numQuestions; //defined amount of questions to use
    JButton next;
    JButton previous;
    JButton finish;
    QuestionPanel qp;
    TimerTime tt;
    boolean timeUp;
    int height;
    int width;
    long time;
    String examId;
    Color theme;

    public MainWindow(Question[] questions, String[] choiceOrders, String[] answers, String[] realAnswers, int numQuestions, String examId, Color theme) throws IOException {
        this.examId = examId;
        this.question = 0;
        this.questions = questions;
        this.numQuestions = numQuestions;
        this.choiceOrders = choiceOrders;
        this.answers = answers;
        this.realAnswers = realAnswers;
        this.timeUp = false;
        this.height = 980;
        this.width = 1000;
        this.theme = theme;

        //navigation
        //previous
        previous = new JButton("<-- Previous");
        previous.setMnemonic(KeyEvent.VK_LEFT);
        previous.setOpaque(true);
        previous.setBackground(this.theme);
        previous.addActionListener(e -> {
            //fetch data
            //greys out on the first question
                try {
                    answers[question] = qp.getAnswer();
                    this.question--;
                    update(true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                //JOptionPane.showMessageDialog(null, "This is the first question of the exam. Cannot go back.");
        });

        //next
        next = new JButton("Next -->");
        next.setMnemonic(KeyEvent.VK_RIGHT);
        next.setOpaque(true);
        next.setBackground(this.theme);
        next.addActionListener(e -> {
            //record the data, fetch if needed
            //update the display
            //greys out on last question, finish button appears
                try {
                    answers[question] = qp.getAnswer();
                    this.question++;
                    update(true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                //JOptionPane.showMessageDialog(null, "This is the last question of the exam. Press finish to finish and grade the exam.");
        });

        //finish
        finish = new JButton("Finish");
        finish.setMnemonic(KeyEvent.VK_ESCAPE);
        finish.setOpaque(true);
        finish.setBackground(this.theme);
        finish.addActionListener(e -> {
            //record the last answer first
            answers[question] = qp.getAnswer();
            //finish the exam, show results
            boolean isDone = true;
            for (String s : answers) {
                if (!s.contains("1")) {
                    int quitMsg = JOptionPane.showConfirmDialog(null,"You haven't answered all the questions yet. Abort now?", "Exit Exam", JOptionPane.YES_NO_OPTION);
                    if (quitMsg == JOptionPane.NO_OPTION) {
                        isDone = false;
                        break;
                    } else {
                        String results = validateAnswers(); //output to a file
                        int place = 0;
                        for (int i = results.length() - 1; i > 0; i--) {
                            if (results.charAt(i) == '\n') {
                                place = i;
                                break;
                            }
                        }
                        JOptionPane resultMsg = new JOptionPane();
//                        resultMsg.setOpaque(true); //dark theme msg
//                        resultMsg.setBackground(this.theme);
//                        resultMsg.createDialog(results.substring(place));
//                        resultMsg.setMessageType(JOptionPane.INFORMATION_MESSAGE);
                        JOptionPane.showMessageDialog(null, results.substring(place));
                        System.out.println(results);
                        //JOptionPane.showMessageDialog(null, "Exam results are outputted to a text file.");
                        System.exit(0);
                    }
                }
            }
            int quitMsg = -1;
            if (isDone) {
               quitMsg = JOptionPane.showConfirmDialog(null, "Finish exam?", "Exit Exam", JOptionPane.YES_NO_OPTION);
            }
            if (quitMsg == JOptionPane.YES_OPTION) {
                String results = validateAnswers(); //output to a file
                int place = 0;
                for (int i = results.length() - 1; i > 0; i--) {
                    if (results.charAt(i) == '\n') {
                        place = i;
                        break;
                    }
                }
                JOptionPane.showMessageDialog(null, results.substring(place));
                System.out.println(results);
                //JOptionPane.showMessageDialog(null, "Exam results are outputted to a text file.");
                System.exit(0);
            }
        });

        //timer
        this.time = this.numQuestions * 60 * 1000L; //extra time: 2:48 per question, or 168s | regular time: 1:52 per question, or 112s
        tt = new TimerTime(this.time);
        Timer t = new Timer(1000, this);
        t.setInitialDelay(0);
        //t.start();

        //window settings
        setSize(new Dimension(this.width, this.height));
        //Main.centerWindow(this);
        setLocationByPlatform(true);
        setLocationRelativeTo(null);
        setResizable(false); //resizing does not work, breaks the divider locations
        //setMinimumSize(new Dimension(1000, 750));
        setTitle("Quiz Maker");
        getContentPane().setLayout(new GridLayout());
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        //setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        update(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //every second, check if the timer has run out of time
        if (tt.outOfTime) {
            JOptionPane.showMessageDialog(null, "You ran out of time!");
            String results = validateAnswers(); //output to a file
            int place = 0;
            for (int i = results.length() - 1; i > 0; i--) {
                if (results.charAt(i) == '\n') {
                    place = i;
                    break;
                }
            }
            JOptionPane.showMessageDialog(null, results.substring(place));
            System.out.println(results);
            System.exit(0);
        }
    }

    private void update(boolean remove) throws IOException {
        setVisible(false);
        if (remove) {
            remove(mainSplit);
        }
        qp = new QuestionPanel(question, questions[question], choiceOrders[question], answers[question], examId, this.theme);
        navigation = new JPanel();
        if (question > 0) {
            navigation.add(previous);
        }
        if (question < numQuestions-1) {
            navigation.add(next);
            finish.setText("Finish?");
        }
        if (question == numQuestions-1) {
            finish.setText("Finish");
        }
        navigation.setOpaque(true);
        navigation.setBackground(this.theme);
        navigation.add(tt);
        navigation.add(finish);
        //main splitter: questionFull | navigation
        mainSplit = new JSplitPane();
        mainSplit.setOpaque(true);
        mainSplit.setBackground(this.theme);
        mainSplit.setTopComponent(qp);
        mainSplit.setBottomComponent(navigation);
        mainSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
        mainSplit.setEnabled(false);
        mainSplit.setDividerLocation(900); //(picture)
        add(mainSplit);
        getContentPane().setBackground(this.theme);
        setVisible(true);
    }

    private String validateAnswers() {
        double correct = 0;
        double wrong = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("============== Your exam results ==============").append("\n");
        sb.append("Key:").append("\n");
        sb.append("* you chose the right answer (if multiple answer)").append("\n");
        sb.append("# you chose the wrong answer").append("\n");
        sb.append("> the right answer").append("\n");
        sb.append("===============================================\n");
        sb.append("Questions you got wrong (if any):").append("\n");
        sb.append("===============================================\n");
        for (int i = 0; i < answers.length; i++) {
            //System.out.println(answers[i] + " : " + realAnswers[i]);
            if (answers[i].equals(this.realAnswers[i])) {
                correct++;
            } else {
                sb.append(i+1).append(". ").append(questions[i].text).append("\n");
                sb.append(convertAnswer(answers[i], questions[i], choiceOrders[i]));
                sb.append("===============================================\n");
                wrong++;
            }
        }
        sb.append("\n");
        sb.append("Your score is ").append((int) ((correct / (double) answers.length) * 1000) / 10);
        sb.append("% with ").append((int)wrong).append(" wrong answers.");
        return sb.toString();
    }

    //displays the user's answer for a question when reviewing results
    private String convertAnswer(String answer, Question correctAnswer, String choiceOrder) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < answer.length(); i++) {
            sb.append("  ");
            final int index = Integer.parseInt(choiceOrder.substring(i, i + 1));
            if (answer.charAt(i) == '1' && correctAnswer.choices.get(index).answer) {
                sb.append("* ");
            } else if (answer.charAt(i) == '1' && !correctAnswer.choices.get(index).answer) {
                sb.append("# ");
            } else if (answer.charAt(i) == '0' && correctAnswer.choices.get(index).answer) {
                sb.append("> ");
            } else {
                sb.append("  ");
            }
            sb.append(Main.numMap(i));
            sb.append(correctAnswer.choices.get(index).text);
            sb.append("\n");
        }
        return sb.toString();
    }
}
