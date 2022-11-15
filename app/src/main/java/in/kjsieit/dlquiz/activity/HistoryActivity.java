package in.kjsieit.dlquiz.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;
import in.kjsieit.dlquiz.R;
import in.kjsieit.dlquiz.quiz.Difficulty;
import in.kjsieit.dlquiz.quiz.question.AnsweredQuestion;
import in.kjsieit.dlquiz.quiz.util.Bundler;
import in.kjsieit.dlquiz.quiz.util.Stringify;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        LinearLayout layout = findViewById(R.id.hCardList);
        layout.removeAllViews();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        JSONArray historyList = new JSONArray();
        String value = preferences.getString("history", historyList.toString());
        try {
            historyList = new JSONArray(value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int historyLen = historyList.length();

        for (int i = historyLen - 1; i >= 0; i--) {
            try {
                JSONObject historyObj = historyList.getJSONObject(i);
                int score = historyObj.getInt("score");
                int seconds = historyObj.getInt("time");
                int easyCtr = historyObj.getInt("easy");
                int mediumCtr = historyObj.getInt("medium");
                int hardCtr = historyObj.getInt("hard");
                JSONArray answeredObj = historyObj.getJSONArray("answeredQuestions");
                ArrayList<AnsweredQuestion> answeredQuestions = new ArrayList<>();

                int answeredLen = answeredObj.length();

                for (int j = 0; j < answeredLen; j++) {
                    try {
                        JSONObject answeredQ = answeredObj.getJSONObject(j);
                        answeredQuestions.add(new AnsweredQuestion(
                                Difficulty.valueOf(answeredQ.getString("difficulty")),
                                answeredQ.getString("question"),
                                answeredQ.getString("answer"),
                                answeredQ.getString("selectedAnswer"),
                                answeredQ.getBoolean("isCorrect") ? 1 : 0,
                                answeredQ.getInt("time")
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                LayoutInflater infalInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View childView = infalInflater.inflate(R.layout.history_card_layout, null);
                CardView cardView = childView.findViewById(R.id.hCard);

                TextView scoreDisp = childView.findViewById(R.id.scoreDisp);
                scoreDisp.setText(String.format(Locale.getDefault(), "%d/20", score));
                TextView timeDisp = childView.findViewById(R.id.timeDisp);
                timeDisp.setText(Stringify.time(seconds));

                Intent intent = new Intent(HistoryActivity.this, ScorecardActivity.class);
                intent.putExtras(Bundler.bundle(score, seconds, easyCtr, mediumCtr, hardCtr, answeredQuestions));

                cardView.setOnClickListener(v -> startActivity(intent));

                layout.addView(childView);

                if (i > 0) {
                    Space space = new Space(getBaseContext());
                    space.setMinimumHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    layout.addView(space);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
