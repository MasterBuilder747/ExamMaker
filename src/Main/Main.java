package Main;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    static int max; //max number of questions in an exam
    static int bankSize; //total available questions
    static ArrayList<Question> questions; //the questions
    static Question[] exam;
    static String[] choiceOrders; //set random orders for choices per question (ids 0-7)
    static String[] answers; //like choices, but these are 1 for selected and 0 for not, used for validating exam
    static String[] realAnswers; //the correct answers to compare user's answers to

    public static final String BASE = "/Users/jaath/IdeaProjects/ExamMakerOCI23/src/";

    public static Color dark = new Color(0xFF3D3D3D, true);
    public static Color light = new Color(0xFFFFFFFF, true);
    public static Color theme;

    public static final boolean isDark = Detector.isDarkMode();

    public static void main(String[] args) throws IOException {
        theme = isDark ? dark : light;

        //0: all questions
        //1-14: every 10 questions
        //21-27: every 20 questions: 21, 22, 23, 24, 25, 26, 27
        //31-34: every 35 questions: 31, 32, 33, 34
        String examId = "0";
        //open exam window, gen the exam
        questions = build(examId);
        //printQuestions();
        exam = generate(questions, max);
        //printIds();
        choiceOrders = genChoices(exam);
        answers = genDefaultAnswers(exam);
        realAnswers = genCorrectAnswers(exam, choiceOrders);
        MainWindow window = new MainWindow(exam, choiceOrders, answers, realAnswers, max, examId, theme);
        window.setVisible(true);
    }

    private static String[] genCorrectAnswers(Question[] qs, String[] choiceOrders) {
        String[] output = new String[qs.length];
        for (int i = 0; i < qs.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < choiceOrders[i].length(); j++) {
                if (qs[i].choices.get(Integer.parseInt(choiceOrders[i].substring(j, j+1))).answer) {
                    sb.append("1");
                } else {
                    sb.append("0");
                }
            }
            output[i] = sb.toString();
        }
        return output;
    }

    private static String[] genDefaultAnswers(Question[] qs) {
        String[] output = new String[qs.length];
        for (int i = 0; i < qs.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < qs[i].choices.size(); j++) {
                sb.append("0");
            }
            output[i] = sb.toString();
        }
        return output;
    }


    private static String[] genChoices(Question[] qs) {
        String[] output = new String[qs.length];
        for (int i = 0; i < qs.length; i++) {
            output[i] = randOrder(qs[i].choices.size());
        }
        return output;
    }

    //given n, return a string of ints in a random order starting with 0
    private static String randOrder(int n) {
        int size = n;
        StringBuilder sb = new StringBuilder();
        ArrayList<String> ints = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            ints.add(Integer.toString(i));
        }
        for (int i = 0; i < n; i++) {
            String j = ints.get((int)(Math.random() * size));
            sb.append(j);
            ints.remove(j);
            size--;
        }
        return sb.toString();
    }

    //generate the randomized exam
    private static Question[] generate(ArrayList<Question> questions, int size) {
        Question[] output = new Question[size];
        int num = questions.size();
        for (int i = 0; i < size; i++) {
            Question q = questions.get((int)(Math.random() * num));
            output[i] = q;
            questions.remove(q);
            num--;
        }
        return output;
    }

    private static ArrayList<Question> build(String examId) throws IOException {
        //given a text file, creates the exam
        //this will then randomize the questions order and the choices order
        /*
        Syntax:
        First line: the number of questions (this must be equal to or less than the bank size)
        imageName (optional, this can simply be a number.jpg)
        choice1
        *choice2 (* indicates the correct answer, if multiple, then multiple are correct)
        choice3
        ...
        =indicates end of question info
        */
        ArrayList<Question> output = new ArrayList<>();
        FileReader fr;
        fr = new FileReader(BASE + "exam/exam"+examId+".txt");
        BufferedReader br = new BufferedReader(fr);
        int num = 0; //keeps count of total questions
        int line = 1; //for logging
        while (true) {
            String s = br.readLine();
            if (s != null) {
                if (num == 0) {
                    max = Integer.parseInt(s); //set max questions
                    num = 1;
                } else {
                    ArrayList<String> buffer = new ArrayList<>();
                    while (!s.equals("]")) {
                        buffer.add(s);
                        s = br.readLine();
                        line++;
                    }
                    String[] qs = buffer.toArray(new String[0]);
                    //determine multiple choice or multiple answer
                    int count = 0;
                    for (String s1 : qs) {
                        if (s1.contains(">")) {
                            count++;
                        }
                    }
                    if (count == 0) {
                        throw new IllegalArgumentException("Question must have at least one correct answer at line " + line);
                    }
                    //add the question
                    //ids start at 0
                    Question q = new Question(qs[0], num,count > 1);
                    for (int i = 1; i < qs.length; i++) {
                        q.addChoice(i - 1, qs[i].replace(">", ""), qs[i].contains(">"));
                    }
                    output.add(q);
                    num++;
                }
                line++;
            } else {
                break;
            }
        }
        bankSize = num;
        return output;
    }

    /*    private static class MyPanel extends JPanel {
        private BufferedImage image;

        public MyPanel() {
            try {
                image = ImageIO.read(MyPanel.class.getResource("/resources/images/test.png"));
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return image == null ? new Dimension(400, 300): new Dimension(image.getWidth(), image.getHeight());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }*/

    public static void centerWindow(java.awt.Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int)((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int)((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    public static String numMap(int n) {
        return switch (n) {
            case 0 -> "A. ";
            case 1 -> "B. ";
            case 2 -> "C. ";
            case 3 -> "D. ";
            case 4 -> "E. ";
            case 5 -> "F. ";
            case 6 -> "G. ";
            case 7 -> "H. ";
            case 8 -> "I. ";
            case 9 -> "J. ";
            default -> "";
        };
    }

    public static void printAnswers() {
        int num = 1;
        for (Question q : questions) {
            System.out.print(num + ". ");
            System.out.println(q);
            num++;
        }
    }

    public static void printQuestions() {
        //should be called before randomizing the exam
        int num = 1;
        for (Question q : questions) {
            System.out.print(num + ". ");
            System.out.println(q.text);
            num++;
        }
    }

    public static void printIds() {
        for (Question q : exam) {
            System.out.println(q.id);
        }
    }
}
