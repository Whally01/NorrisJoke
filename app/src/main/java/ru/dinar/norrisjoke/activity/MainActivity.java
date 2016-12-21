package ru.dinar.norrisjoke.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.dinar.norrisjoke.R;
import ru.dinar.norrisjoke.api.CallerAPI;
import ru.dinar.norrisjoke.model.Type;

public class MainActivity extends AppCompatActivity {
    private ProgressBar pb;
    private TextView tv;
    private Button btn;
    private static final String API_URL = "https://api.icndb.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.btnRefresh);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        tv = (TextView) findViewById(R.id.tv_joke);


        if (savedInstanceState != null) {
            tv.setText(savedInstanceState.getCharSequence("currentJoke"));
            pb.setVisibility(View.INVISIBLE);
        } else {
            doSearch();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btnRefresh) {
                    doSearch();
                }
            }
        });
    }

    private void doSearch() {
        AsyncT asyncT = new AsyncT();
        asyncT.execute();
    }

    private void updateUI(Type type) {
        pb.setVisibility(View.GONE);
        if (type != null) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(type.getValue().getJoke());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putCharSequence("currentJoke", tv.getText());
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * AsyncTask
     */
    public class AsyncT extends AsyncTask<Object, Object, Type> {
        private Retrofit retrofit;

        @Override
        protected void onPreExecute() {
            pb.findViewById(R.id.progressBar);
            pb.setVisibility(View.VISIBLE);
            tv.findViewById(R.id.tv_joke);
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        @Override
        protected Type doInBackground(final Object... params) {
            Type type = null;
            try {

                CallerAPI callerAPI = retrofit.create(CallerAPI.class);
                Call<Type> call;
                call = callerAPI.getJoke();
                Response<Type> response;
                response = call.execute();
                type = response.body();

            } catch (final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return type;
        }

        @Override
        protected void onPostExecute(Type value) {
            updateUI(value);
        }

    }

}
