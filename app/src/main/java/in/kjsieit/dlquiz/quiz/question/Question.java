package in.kjsieit.dlquiz.quiz.question;

import java.util.List;

public class Question {
    String question;
    List<String> options;
    int answer;

    public Question(String question, List<String> options, int answer) {
        this.question = question;
        this.options = options;
        this.answer = answer;
    }

    public String getQuestion() {
        return this.question;
    }

    public List<String> getOptions() {
        return this.options;
    }

    public int getAnswer() {
        return this.answer;
    }
}
