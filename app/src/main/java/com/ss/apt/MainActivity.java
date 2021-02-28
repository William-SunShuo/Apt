package com.ss.apt;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.apt.ButterKnife;
import com.example.apt.Unbinder;
import com.ss.aptlib.BindView;

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