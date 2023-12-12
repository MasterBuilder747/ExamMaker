package Main;

import java.util.*;

public class Question {
    int id; //name of the question image file that will be displayed
    String text; //the question being asked
    final List<Choice> choices; //possible answers, option A, B, ..., //must be: size =< 2 //ID -> QuestionText
    boolean isMultipleAnswer; //if multiple answer or not

    Question(String text, int id, boolean isMultipleAnswer) {
        this.id = id;
        this.text = text;
        this.isMultipleAnswer = isMultipleAnswer;
        this.choices = new ArrayList<>();
    }
    void addChoice(int i, String c, boolean isAnswer) {
        this.choices.add(new Choice(i, c, isAnswer));
    }

/*    String ask() {
        //1) display image (ask question)
        System.out.println(this.image);

        //2) display choice options
        if (this.isMultipleAnswer) {
            System.out.println("Choose " + this.answers.length + " options:");
        } else {
            System.out.println("Choose one option:");
        }
        Collections.shuffle(this.choices);
        for (int i = 0; i < this.choices.size(); i++) {
            System.out.println(numMap(i) + this.choices.get(i).text);
        }

        //3) get user input
        Scanner sc = new Scanner(System.in);
        String s = sc.next();
        if (s.length() != this.answers.length) {
            System.out.println("Invalid input. Please try again.");
            this.ask();
        } else {
            System.out.println("Answer recorded");
        }
        return s;
    }*/

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.text);
        sb.append("\n");
        for (int i = 0; i < this.choices.size(); i++) {
            sb.append(Main.numMap(i));
            sb.append(this.choices.get(i));
            sb.append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }
}
