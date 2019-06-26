package and.bfop.kftc.com.useorgsampleapprenewal.layout;

import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import and.bfop.kftc.com.useorgsampleapprenewal.layout.userInfo.SignupActivity;

public class Mainlogin extends AppCompatActivity {

    RelativeLayout rellay1, rellay2;

    public static final int sub = 1001;
    Handler handler = new Handler();
    Runnable runnble = new Runnable(){
        @Override
        public void run(){
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlogin);


        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);

        handler.postDelayed(runnble, 2000);

        Button btnlogin = (Button) findViewById(R.id.btn_login);
        Button signupbtn = (Button) findViewById(R.id.signup);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mainlogin.this, MainActivity.class);
                startActivity(intent);
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mainlogin.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }

}
