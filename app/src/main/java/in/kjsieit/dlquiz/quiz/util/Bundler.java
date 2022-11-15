package in.kjsieit.dlquiz.quiz.util;

import android.os.Bundle;
import in.kjsieit.dlquiz.quiz.question.AnsweredQuestion;

import java.util.ArrayList;

public class Bundler {
    public static Bundle bundle(int score, int seconds, int easyCtr, int mediumCtr, int hardCtr, ArrayList<AnsweredQuestion> answeredQuestions) {
        Bundle dat = new Bundle();
        dat.putInt("score", score);
        dat.putInt("time", seconds);
        dat.putInt("easy", easyCtr);
        dat.putInt("medium", mediumCtr);
        dat.putInt("hard", hardCtr);
        dat.putParcelableArrayList("answered", answeredQuestions);
        return dat;
    }
}
