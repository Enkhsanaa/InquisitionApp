package mn.enkhsanaa.model;

import mn.enkhsanaa.Main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Enkhsanaa Natsagdorj on 11/28/2017.
 * Test class
 */

public class Test implements Serializable {
    private static final long serialVersionUID = 3L;
    private int questionCount;
    private List<Question> questions;

    public Test() { }
    public Test(Test test) {
        questionCount = test.questionCount;
        questions = new ArrayList<>(questionCount);
        for (int i = 0; i < questionCount; i++) {
            questions.add(new Question(test.getQuestion(i)));
        }
    }

    // Helpers
    public boolean checkAnswer(int index, String answer) { return questions.get(index).checkAnswer(answer); }
    private InputStream getInputStream(boolean fromFile) throws FileNotFoundException {
        if (fromFile) return new FileInputStream(Main.textFilePath);
        return System.in;
    }

    // Getters
    public Question getQuestion(int index) { return questions.get(index); }
    public int getQuestionCount() {
        return questionCount;
    }

    // Readers
    private void readQuestionCount(Scanner sc, boolean fromFile) {
        if (!fromFile) System.out.print("Enter number of questions: ");
        this.questionCount = Integer.parseInt(sc.nextLine());
        questions = new ArrayList<>(this.questionCount);
    }
    private void readQuestion(Scanner sc, boolean fromFile) {
        if (!fromFile && questions.size() > 0) System.out.println("Next question");
        Question question = new Question();
        question.read(sc, fromFile);
        questions.add(question);
    }


    public void read(boolean fromFile) throws IOException {
        InputStream fs = getInputStream(fromFile);
        Scanner sc = new Scanner(fs);
        readQuestionCount(sc, fromFile);
        for (int i = 0; i < questionCount; i++) {
            readQuestion(sc, fromFile);
        }
    }
    public void print(PrintStream out) {
        out.printf("The test has %d questions.\n", getQuestionCount());
        for (int i = 0; i < getQuestionCount(); i++) {
            Question question = getQuestion(i);
            question.print(out);
        }
    }
}
