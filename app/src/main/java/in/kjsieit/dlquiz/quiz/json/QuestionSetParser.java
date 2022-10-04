package in.kjsieit.dlquiz.quiz.json;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QuestionSetParser {
    public static List<Question> parse(InputStream raw) {
        try (JsonReader reader = new JsonReader(new InputStreamReader(raw, StandardCharsets.UTF_8))) {
            List<Question> questions = new ArrayList<>();

            reader.beginArray();
            while (reader.hasNext()) {
                questions.add(readQuestion(reader));
            }
            reader.endArray();

            return questions;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Question readQuestion(JsonReader reader) throws IOException {
        String question = null;
        List<String> options = null;
        int answer = -1;

        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            switch (key) {
                case "question":
                    question = reader.nextString();
                    break;
                case "options":
                    options = new ArrayList<>();
                    reader.beginArray();
                    while (reader.hasNext()) {
                        options.add(reader.nextString());
                    }
                    reader.endArray();
                    break;
                case "answer":
                    answer = reader.nextInt();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();

        return new Question(question, options, answer);
    }
}
