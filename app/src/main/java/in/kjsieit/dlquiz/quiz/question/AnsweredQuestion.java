package in.kjsieit.dlquiz.quiz.question;

import android.os.Parcel;
import android.os.Parcelable;
import in.kjsieit.dlquiz.quiz.Difficulty;

public class AnsweredQuestion implements Parcelable {
    Difficulty difficulty;
    String question;
    String answer;
    String selectedAnswer;
    int isCorrect;
    int time;

    public AnsweredQuestion(Difficulty difficulty, String question, String answer, String selectedAnswer, int isCorrect, int time) {
        this.difficulty = difficulty;
        this.question = question;
        this.answer = answer;
        this.selectedAnswer = selectedAnswer;
        this.isCorrect = isCorrect;
        this.time = time;
    }

    protected AnsweredQuestion(Parcel in) {
        difficulty = Enum.valueOf(Difficulty.class ,in.readString());
        question = in.readString();
        answer = in.readString();
        selectedAnswer = in.readString();
        isCorrect = in.readInt();
        time = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(difficulty.name());
        dest.writeString(question);
        dest.writeString(answer);
        dest.writeString(selectedAnswer);
        dest.writeInt(isCorrect);
        dest.writeInt(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AnsweredQuestion> CREATOR = new Creator<AnsweredQuestion>() {
        @Override
        public AnsweredQuestion createFromParcel(Parcel in) {
            return new AnsweredQuestion(in);
        }

        @Override
        public AnsweredQuestion[] newArray(int size) {
            return new AnsweredQuestion[size];
        }
    };

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public String getQuestion() {
        return this.question;
    }

    public String getAnswer() {
        return this.answer;
    }

    public String getSelectedAnswer() {
        return this.selectedAnswer;
    }

    public boolean isCorrect() {
        return this.isCorrect != 0;
    }

    public int getTime() {
        return this.time;
    }
}
