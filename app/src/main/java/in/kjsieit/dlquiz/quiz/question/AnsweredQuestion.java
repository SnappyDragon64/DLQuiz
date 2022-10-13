package in.kjsieit.dlquiz.quiz.question;

import android.os.Parcel;
import android.os.Parcelable;

public class AnsweredQuestion implements Parcelable {
    String question;
    String answer;
    String selectedAnswer;
    int isCorrect;

    public AnsweredQuestion(String question, String answer, String selectedAnswer, int isCorrect) {
        this.question = question;
        this.answer = answer;
        this.selectedAnswer = selectedAnswer;
        this.isCorrect = isCorrect;
    }

    protected AnsweredQuestion(Parcel in) {
        question = in.readString();
        answer = in.readString();
        selectedAnswer = in.readString();
        isCorrect = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(answer);
        dest.writeString(selectedAnswer);
        dest.writeInt(isCorrect);
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
        return isCorrect != 0;
    }
}
