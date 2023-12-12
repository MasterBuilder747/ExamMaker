package Main;

public class Choice {
    int id;
    String text;
    boolean answer;

    Choice (int id, String text, boolean answer) {
        this.id = id;
        this.text = text;
        this.answer = answer;
    }

    public String toString() {
        if (this.answer) {
            return "> " + this.text;
        } else {
            return this.text;
        }
    }
}
