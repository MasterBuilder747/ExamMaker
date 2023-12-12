package Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class QuestionPanel extends JPanel {
    Question question; //the current question data
    ButtonGroup group;
    JSplitPane fullQuestionSplit;
    JSplitPane questSplit;
    JLabel q; //question text
    JLabel pic; //image
    JPanel imagePanel;
    JPanel questPanel;
    JPanel optionsPanel;
    JPanel radioPanel;
    BufferedImage questionImg;
    ArrayList<JCheckBox> checks;
    ArrayList<JRadioButton> buttons;
    String examId;
    Color theme;
    String textColor;
    String placeholder;

    public QuestionPanel(int num, Question question, String order, String answer, String examId, Color theme) throws IOException {
        this.examId = examId;
        //given question data, construct and render a question and load current data if needed

        //components for user input
//        JLabel textLabel = new JLabel("testing", SwingConstants.CENTER);
//        textLabel.setPreferredSize(new Dimension(300, 100));
//        this.add(textLabel);

        this.checks = new ArrayList<>();
        this.buttons = new ArrayList<>();
        this.question = question;
        setSize(new Dimension(1000, 870));
        setLayout(new GridLayout());
        
        this.theme = theme;
        if (Main.isDark) {
            this.textColor = "FFFFFF";
            this.placeholder = "placeholder";
        } else {
            this.textColor = "000000";
            this.placeholder = "placeholder_light";
        }

        //this panel is the image
        imagePanel = new JPanel();
        int id = this.question.id;
        //int eid = Integer.parseInt(examId);
/*        if (eid < 20 && eid > 0) {
            //every 10 questions, last one is 9 qs (x14), 1-14: 1-10, 11-20...
            id = Integer.toString(this.question.id + (10 * (Math.abs((eid - 1)))));
        } else if (eid > 20 && eid < 30) {
            //every 20 questions, last one is 19 qs (x7), 21-27: 1-20, 21-40...
            id = Integer.toString(this.question.id + (20 * (Math.abs((eid - 21)))));
        } else if (eid > 30) {
            //every 35 questions, last one is 34 qs (x4), 31-34: 1-35, 36-70...
            id = Integer.toString(this.question.id + (35 * (Math.abs((eid - 31)))));
        }*/
        //System.out.println(id + ".png");
        try {
            questionImg = ImageIO.read(new File(Main.BASE + "images/" + id + ".png"));
        } catch (IOException e) {
            //if there is no image file, we can assume it has no image for the question
            questionImg = ImageIO.read(new File(Main.BASE + "images/" + this.placeholder + ".png"));
        }
        Dimension imgSize = new Dimension(questionImg.getWidth(), questionImg.getHeight());
        Dimension boundary = new Dimension(this.getWidth(), 550);
        Dimension dim = getScaledDimension(imgSize, boundary);
        ImageIcon imgIcon = new ImageIcon(new ImageIcon(questionImg).getImage().getScaledInstance(dim.width, dim.height, Image.SCALE_SMOOTH));
        pic = new JLabel(imgIcon);
        pic.setOpaque(true);
        pic.setBackground(this.theme);
        //JLabel pic = new JLabel(new ImageIcon(question));
        //pic.setPreferredSize(new Dimension(700, 500));
        //Image dimg = question.getScaledInstance(pic.getWidth(), pic.getHeight(), Image.SCALE_SMOOTH);
        //this.getContentPane().add(picLabel);
        //image for question
        //MyPanel contentPane = new MyPanel();
        //BufferedImage image = ImageIO.read(Main.class.getResource("/resources/images/test.png"));
        imagePanel.add(pic);
        imagePanel.setOpaque(true);
        imagePanel.setBackground(this.theme);
        //setResizable(false);

        //add question text
        //optionsPanel.setBorder(BorderFactory.createEmptyBorder());
        //select.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.LINE_START);
        questPanel = new JPanel();
        questPanel.setLayout(new BoxLayout(questPanel, BoxLayout.PAGE_AXIS));
        //questPanel.setBorder(new EmptyBorder(0, 30, 5, 5));
        String text = (num+1) + ")  " + question.text;
        //"requisite items only play a role when no other modules of that module type have succeeded or failed. In that case, the success or failure of a requisite flagged module determines the overall PAM authentication for that module type";
        q = new JLabel("<html><p style=\"color:#" + this.textColor + "\">" + text + "</style></html>", SwingConstants.LEFT);
        //q.setLayout(new BorderLayout());
        //q.setHorizontalAlignment(SwingConstants.LEFT);
        questPanel.add(q);
        int pad = 5;
        questPanel.setBorder(new EmptyBorder(pad, 15, pad, pad));
        questPanel.setOpaque(true);
        questPanel.setBackground(this.theme);

        //add options here
        //max length for text wrapping: 75
        Choice[] choices = this.question.choices.toArray(new Choice[0]);

        group = new ButtonGroup();
        //at most 8 options for checkbox, at most 5 for radiobutton
        radioPanel = new JPanel(new GridLayout(0, 1));
        radioPanel.setOpaque(true);
        radioPanel.setBackground(this.theme);

        if (!this.question.isMultipleAnswer) {
            for (int i = 0; i < choices.length; i++) {
                JRadioButton j = new JRadioButton("<html><p style=\"color:#" + this.textColor + "\">" + Main.numMap(i) + choices[Integer.parseInt(order.substring(i, i+1))].text + "</style></html>");
                j.setSelected((answer.charAt(i) == '1'));
                buttons.add(j);
                group.add(j);
                radioPanel.add(j);
            }
        } else {
            for (int i = 0; i < choices.length; i++) {
                JCheckBox j = new JCheckBox("<html><p style=\"color:#" + this.textColor + "\">" + Main.numMap(i) + choices[Integer.parseInt(order.substring(i, i+1))].text + "</style></html>");
                j.setSelected((answer.charAt(i) == '1'));
                checks.add(j);
                radioPanel.add(j);
            }
        }

        //middle panel: user input
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));
        //optionsPanel.setLayout(new GridLayout(2, 1));
        optionsPanel.setBorder(new EmptyBorder(0, 20, 10, 10));
        optionsPanel.add(radioPanel);
        optionsPanel.setOpaque(true);
        optionsPanel.setBackground(this.theme);

        //question splitter: questText | options
        questSplit = new JSplitPane();
        questSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
        questSplit.setEnabled(false);
        questSplit.setTopComponent(questPanel);
        questSplit.setBottomComponent(optionsPanel);
        questSplit.setDividerLocation(90); //questText

        //fullQuestion splitter: image | question
        fullQuestionSplit = new JSplitPane();
        fullQuestionSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
        fullQuestionSplit.setEnabled(false);
        fullQuestionSplit.setTopComponent(imagePanel);
        fullQuestionSplit.setBottomComponent(questSplit);
        fullQuestionSplit.setDividerLocation(550); //image

        add(fullQuestionSplit);
    }

    public String getAnswer() {
        StringBuilder sb = new StringBuilder();
        if (this.question.isMultipleAnswer) {
            for (JCheckBox j : checks) {
                if (j.isSelected()) {
                    sb.append('1');
                } else {
                    sb.append("0");
                }
            }
        } else {
            for (JRadioButton j : buttons) {
                if (j.isSelected()) {
                    sb.append('1');
                } else {
                    sb.append("0");
                }
            }
        }
        return sb.toString();
    }

    //use for image scaling while maintaining aspect ratio
    private static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }
}