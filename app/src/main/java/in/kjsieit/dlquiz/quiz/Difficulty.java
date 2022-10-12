package in.kjsieit.dlquiz.quiz;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ImmutableList;
import in.kjsieit.dlquiz.R;
import in.kjsieit.dlquiz.quiz.question.Question;
import in.kjsieit.dlquiz.quiz.question.QuestionSetParser;

public enum Difficulty {
    EASY("easy", R.raw.easy), MEDIUM("medium", R.raw.medium), HARD("hard", R.raw.hard);

    public final String name;
    public final ImmutableList<Question> questionSet;

    Difficulty(String name, int id) {
        this.name = name;
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
