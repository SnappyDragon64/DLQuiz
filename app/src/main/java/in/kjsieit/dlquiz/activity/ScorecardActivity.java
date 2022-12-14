package in.kjsieit.dlquiz.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import in.kjsieit.dlquiz.R;
import in.kjsieit.dlquiz.quiz.Difficulty;
import in.kjsieit.dlquiz.quiz.question.AnsweredQuestion;
import in.kjsieit.dlquiz.quiz.util.Stringify;

import java.util.ArrayList;
import java.util.Locale;

public class ScorecardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecard);

        Bundle dat = getIntent().getExtras();
        int score  = 0;
        int time = 0;
        int easy = 0;
        int medium = 0;
        int hard = 0;
        ArrayList<AnsweredQuestion> answeredQuestions = new ArrayList<>();

        if (dat != null) {
            score = dat.getInt("score");
            time = dat.getInt("time");
            easy = dat.getInt("easy");
            medium = dat.getInt("medium");
            hard = dat.getInt("hard");
            answeredQuestions = dat.getParcelableArrayList("answered");
        }

        String scorestr = String.format(Locale.getDefault(), "Score: %d", score);

        String qstr = String.format(Locale.getDefault(), "Distribution:\nEasy: %d\nMedium: %d\nHard: %d", easy, medium, hard);

        TextView statView = findViewById(R.id.statDisplay);
        statView.setText(String.format("%s\n%s", scorestr, Stringify.time(time)));

        TextView distView = findViewById(R.id.distDisplay);
        distView.setText(qstr);

        LinearLayout layout = findViewById(R.id.qCardList);
        layout.removeAllViews();

        for (int i = 0; i < 20; i++) {
            LayoutInflater infalInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View childView = infalInflater.inflate(R.layout.question_card_layout, null);
            CardView qCard = childView.findViewById(R.id.qCard);
            TextView qNo = childView.findViewById(R.id.qNo);
            TextView qTime = childView.findViewById(R.id.qTime);
            TextView qText = childView.findViewById(R.id.qText);
            TextView qSelectedAnswer = childView.findViewById(R.id.qSelectedAnswer);
            TextView qAnswer = childView.findViewById(R.id.qAnswer);
            AnsweredQuestion answeredQuestion = answeredQuestions.get(i);

            int borderId;
            if (answeredQuestion.getDifficulty() == Difficulty.EASY) borderId = R.drawable.easy_border;
            else if (answeredQuestion.getDifficulty() == Difficulty.MEDIUM) borderId = R.drawable.medium_border;
            else borderId = R.drawable.hard_border;

            qCard.setForeground(getResources().getDrawable(borderId, null));
            qCard.setCardBackgroundColor(getResources().getColor(answeredQuestion.isCorrect() ? R.color.green : R.color.red, null));
            qNo.setText(String.format(Locale.getDefault(), "Q%d", i+1));
            qTime.setText(Stringify.time(answeredQuestion.getTime()));
            qText.setText(answeredQuestion.getQuestion());
            qSelectedAnswer.setText(String.format(Locale.getDefault(), "Your Answer: %s", answeredQuestion.getSelectedAnswer()));
            qAnswer.setText(String.format(Locale.getDefault(), "Answer: %s", answeredQuestion.getAnswer()));

            layout.addView(childView);

            if (i < 19) {
                Space space = new Space(getBaseContext());
                space.setMinimumHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                layout.addView(space);
            }
        }
    }
}
