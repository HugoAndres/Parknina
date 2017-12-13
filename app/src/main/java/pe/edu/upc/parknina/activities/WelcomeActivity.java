package pe.edu.upc.parknina.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.ekalips.fancybuttonproj.FancyButton;

import pe.edu.upc.parknina.R;

//REMEMBER: Not used.
public class WelcomeActivity extends AppCompatActivity {
//    private CircularProgressButton startCircularProgressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null){
            actionBar.setTitle(R.string.IndeterminateProgressSample);
        }

        /*startCircularProgressButton = (CircularProgressButton) findViewById(R.id.startCircularProgressButton);
        startCircularProgressButton.setIndeterminateProgressMode(true);
        startCircularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startCircularProgressButton.getProgress() == 0) {
                    startCircularProgressButton.setProgress(50);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startCircularProgressButton.setProgress(100);
                            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                            finish();
                        }
                    }, 2000);
                }
                else if (startCircularProgressButton.getProgress() == 100) {
                    startCircularProgressButton.setProgress(0);
                }
                else {
                    startCircularProgressButton.setProgress(100);
                }
            }
        });*/

        FancyButton button1 = (FancyButton) findViewById(R.id.btn1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view instanceof  FancyButton) {
                    if (((FancyButton)view).isExpanded())
                        ((FancyButton)view).collapse();
                    else
                        ((FancyButton)view).expand();
                }
            }
        });
    }
}
