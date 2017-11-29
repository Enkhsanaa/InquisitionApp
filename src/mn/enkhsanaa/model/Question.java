/**
 * Created by Enkhsanaa Natsagodrj on 11/28/2017.
 * Question class
 */
package mn.enkhsanaa.model;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Question implements Serializable {
    private static final long serialVersionUID = 2L;
    private static int count = 1;
    private int id;
    private String question;
    private String note;
    private int optionCount;
    private List <Option> options;
    private String answer = null; // A, B, C, D, E etc..

    public Question() { this.id = count++; }

    public Question(Question question) {
        this.id = question.id;
        this.question = question.toString();
        this.note = question.getNote();
        this.optionCount = question.getOptionCount();
        this.options = new ArrayList<>(getOptionCount());
        for (int i = 0; i < optionCount; i++) {
            options.add(new Option(question.getOption(i).toString()));
        }
        this.answer = question.answer;
    }

    // Actions
    public boolean checkAnswer(String answer) { return (this.answer.equals(answer)); }


    // Getters
    public int getId() { return id; }
    public int getOptionCount() {
        return optionCount;
    }
    public Option getOption(int index) {
        return options.get(index);
    }
    public String getAnswer() { return answer; }
    public String getNote() { return note; }


    // Readers
    public void readQuestion(Scanner sc, boolean fromFile) {
        if (!fromFile) System.out.print("Question? ");
        else System.out.println("Reading question " + id);
        while(true) {
            question = sc.nextLine();
            if (question.length() != 0) {
                break;
            }
            System.out.println("Question cannot be empty");
        }
    }

    public void readOptionCount(Scanner sc, boolean fromFile) {
        while (true) {
            try {
                if (!fromFile) System.out.print("Number of options? ");
                optionCount = Integer.parseInt(sc.nextLine());
                options = new ArrayList<>(this.optionCount);
                break;
            } catch (InputMismatchException e) {
                if (!fromFile) System.out.print("Not an integer. ");
            }
        }
    }
    public void readOption(Scanner sc, boolean fromFile) {
        if (!fromFile) System.out.printf("Option %c: ", (char)((int)'A' + options.size() + 1));
        while (true) {
            Option option = new Option(sc.nextLine());
            if (option.toString().length() != 0) {
                options.add(option);
                break;
            }
            System.out.println("Option cannot be empty");
        }
    }
    public void readAnswer(Scanner sc, boolean fromFile) {
        if (!fromFile) System.out.print("Which one is the right option [A, B, C, D, ..]? ");

        while(true) {
            answer = sc.nextLine();
            if (answer.length() != 0 && 'A' <= answer.charAt(0) && answer.charAt(0) <= 'A' + optionCount) {
                break;
            }
            System.out.println("Invalid option");
        }
    }
    public void readNote(Scanner sc, boolean fromFile) {
        if (!fromFile) System.out.print("Explanation for the correct answer [Can be left blank]: ");
        note = sc.nextLine();
    }

    public void read(Scanner sc, boolean fromFile) {
        readQuestion(sc, fromFile);
        readOptionCount(sc, fromFile);
        for (int i = 0; i < optionCount; i++) {
            readOption(sc, fromFile);
        }
        readAnswer(sc, fromFile);
        readNote(sc, fromFile);
    }
    public void print(PrintStream out) {
        out.printf("Question %d: %s\n", id, question);
        for (int i = 0; i < optionCount; i++) {
            out.printf("%c) %s\n", (char) ((int) 'A' + i), this.getOption(i));
        }
    }

    @Override
    public String toString() { return question; }
}
