package com.emad.fcmtester;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.emad.fcmtester.firebase.AndroidUtils;
import com.emad.fcmtester.firebase.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getIntent().getExtras() != null && getIntent().hasExtra("FCM")) {
            ((TextView) findViewById(R.id.tv_token)).setText(((String) getIntent().getExtras().get("FCM")));
            Toast.makeText(this, "NEW NOTIFICATION RECEIVED", Toast.LENGTH_SHORT).show();
        }
        findViewById(R.id.tv_get_token).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNewToken = AndroidUtils.getPref(Constants.KEY_FCM_IS_NEW, true);
                if (isNewToken) {
                    final String[] token = {""};
                    FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if(!task.isSuccessful() || task.getResult() == null)
                                return;
                            token[0] = task.getResult().getToken();
                            Log.e("tag", "#FCM TOKEN REGISTERING: " + token[0] );
                            AndroidUtils.editPref(Constants.KEY_FCM_TOKEN, token[0]);
                            ((TextView) findViewById(R.id.tv_token)).setText(token[0]);
                        }
                    });
                }
            }
        });
    }
}