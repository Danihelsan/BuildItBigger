package pe.asomapps.jokeviewer;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Danihelsan
 */
public class JokeViewerActivity extends AppCompatActivity {
    public static final String KEY_JOKE = "joke";

    TextView jokeText;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.jokeviewer_activity);
        jokeText = (TextView) findViewById(R.id.jokeText);

        if (getIntent().getExtras()!=null){
        Bundle bundle = getIntent().getExtras();
            if (bundle.containsKey(KEY_JOKE)){
                String joke = bundle.getString(KEY_JOKE);

                jokeText.setText(joke);
            }
        }
    }
}
