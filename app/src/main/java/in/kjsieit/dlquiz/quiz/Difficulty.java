package in.kjsieit.dlquiz.quiz;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ImmutableList;
import in.kjsieit.dlquiz.R;
import in.kjsieit.dlquiz.quiz.question.Question;
import in.kjsieit.dlquiz.quiz.question.QuestionSetParser;

public enum Difficulty {
    EASY(R.raw.easy), MEDIUM(R.raw.medium), HARD(R.raw.hard);
    public final ImmutableList<Question> questionSet;

    Difficulty(int id) {
        this.questionSet = QuestionSetParser.parseSet(id);
    }

    public Difficulty increase() {
        switch (this) {
            case EASY:
                return MEDIUM;
            case MEDIUM:
            case HARD:
            default:
                return HARD;
        }
    }

    public Difficulty decrease() {
        switch (this) {
            case HARD:
                return MEDIUM;
            case MEDIUM:
            case EASY:
            default:
                return EASY;
        }
    }
}
