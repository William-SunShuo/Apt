package com.ss.apt;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Response;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.apt.ButterKnife;
import com.example.apt.Unbinder;
import com.example.network.Okhttp;
import com.ss.aptlib.BindView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView textView;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        textView.setText("java poet");
        Okhttp okhttp = new Okhttp();
        okhttp.getData(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TAG", "IOException");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null) {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        Log.i("TAG", jsonObject.toString());
                    } catch (JSONException e) {

                    }
                }
            }
        });

    }
//
//    @OnClick(R.id.tv)
//    public void onClick(View view){
//        if(view.getId() == R.id.tv){
//            Toast.makeText(MainActivity.this,"tv",Toast.LENGTH_LONG).show();
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}