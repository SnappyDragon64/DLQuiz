package in.kjsieit.dlquiz.quiz;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ImmutableList;
import in.kjsieit.dlquiz.R;
import in.kjsieit.dlquiz.quiz.json.Question;
import in.kjsieit.dlquiz.quiz.json.QuestionSetParser;

public enum Difficulty {
    EASY(R.raw.easy), MEDIUM(R.raw.medium), HARD(R.raw.hard);

    private final ImmutableList<Question> questionSet;

    Difficulty(int id) {
        this.questionSet = QuestionSetParser.parseSet(id);
    }

    public ImmutableList<Question> getQuestionSet() {
        return this.questionSet;
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
