package in.kjsieit.dlquiz.activity;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import in.kjsieit.dlquiz.R;
import in.kjsieit.dlquiz.quiz.Difficulty;
import in.kjsieit.dlquiz.quiz.question.AnsweredQuestion;

import java.util.ArrayList;
import java.util.Collections;
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

        String scorestr = String.format(Locale.getDefault(), "Score: %d", score);;

        String timestr;
        int h = time / 3600;
        int m = (time % 3600) / 60;
        int s = time % 60;

        if (h > 0)
            timestr = String.format(Locale.getDefault(), "%d:%02d:%02d", h, m, s);
        else
            timestr = String.format(Locale.getDefault(), "%02d:%02d", m, s);

        String qstr = String.format(Locale.getDefault(), "Distribution:\nEasy: %d\nMedium: %d\nHard: %d", easy, medium, hard);

        TextView statView = findViewById(R.id.statDisplay);
        statView.setText(String.format("%s\n%s", scorestr, timestr));

        TextView distView = findViewById(R.id.distDisplay);
        distView.setText(qstr);

        int[] qCards = {R.id.qCard1, R.id.qCard2, R.id.qCard3, R.id.qCard4, R.id.qCard5, R.id.qCard6, R.id.qCard7, R.id.qCard8, R.id.qCard9, R.id.qCard10, R.id.qCard11, R.id.qCard12, R.id.qCard13, R.id.qCard14, R.id.qCard15, R.id.qCard16, R.id.qCard17, R.id.qCard18, R.id.qCard19, R.id.qCard20};
        int[] qTexts = {R.id.qText1, R.id.qText2, R.id.qText3, R.id.qText4, R.id.qText5, R.id.qText6, R.id.qText7, R.id.qText8, R.id.qText9, R.id.qText10, R.id.qText11, R.id.qText12, R.id.qText13, R.id.qText14, R.id.qText15, R.id.qText16, R.id.qText17, R.id.qText18, R.id.qText19, R.id.qText20};
        int[] qSelectedAnswers = {R.id.qSelectedAnswer1, R.id.qSelectedAnswer2, R.id.qSelectedAnswer3, R.id.qSelectedAnswer4, R.id.qSelectedAnswer5, R.id.qSelectedAnswer6, R.id.qSelectedAnswer7, R.id.qSelectedAnswer8, R.id.qSelectedAnswer9, R.id.qSelectedAnswer10, R.id.qSelectedAnswer11, R.id.qSelectedAnswer12, R.id.qSelectedAnswer13, R.id.qSelectedAnswer14, R.id.qSelectedAnswer15, R.id.qSelectedAnswer16, R.id.qSelectedAnswer17, R.id.qSelectedAnswer18, R.id.qSelectedAnswer19, R.id.qSelectedAnswer20};
        int[] qAnswers = {R.id.qAnswer1, R.id.qAnswer2, R.id.qAnswer3, R.id.qAnswer4, R.id.qAnswer5, R.id.qAnswer6, R.id.qAnswer7, R.id.qAnswer8, R.id.qAnswer9, R.id.qAnswer10, R.id.qAnswer11, R.id.qAnswer12, R.id.qAnswer13, R.id.qAnswer14, R.id.qAnswer15, R.id.qAnswer16, R.id.qAnswer17, R.id.qAnswer18, R.id.qAnswer19, R.id.qAnswer20};

        for (int i = 0; i < 20; i++) {
            CardView qCard = findViewById(qCards[i]);
            TextView qText = findViewById(qTexts[i]);
            TextView qSelectedAnswer = findViewById(qSelectedAnswers[i]);
            TextView qAnswer = findViewById(qAnswers[i]);
            AnsweredQuestion answeredQuestion = answeredQuestions.get(i);

            int borderId;
            if (answeredQuestion.getDifficulty() == Difficulty.EASY) borderId = R.drawable.easy_border;
            else if (answeredQuestion.getDifficulty() == Difficulty.MEDIUM) borderId = R.drawable.medium_border;
            else borderId = R.drawable.hard_border;

            qCard.setForeground(getResources().getDrawable(borderId, null));
            qCard.setCardBackgroundColor(getResources().getColor(answeredQuestion.isCorrect() ? R.color.green : R.color.red, null));
            qText.setText(answeredQuestion.getQuestion());
            qSelectedAnswer.setText(String.format(Locale.getDefault(), "Your answer: %s", answeredQuestion.getSelectedAnswer()));
            qAnswer.setText(String.format(Locale.getDefault(), "Answer: %s", answeredQuestion.getAnswer()));
        }
    }
}
