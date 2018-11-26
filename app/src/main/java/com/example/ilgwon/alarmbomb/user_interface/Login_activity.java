package com.example.ilgwon.alarmbomb.user_interface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ilgwon.alarmbomb.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login_activity extends Activity {
    private static final int RC_SIGN_IN = 900;
    private boolean check_acc;
    // 구글api클라이언트
    private GoogleSignInClient googleSignInClient;
    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;
    // 구글  로그인 버튼
    private SignInButton buttonGoogle;
    //firebase database 사용
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference();
    private DataSnapshot dataSnapshot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonGoogle = findViewById(R.id.btn_googleSignIn);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }


        });
        //        enter.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                if (check_acc==true){
        //                        Intent intent=new Intent(Login_activity.this,AlarmSettingActivity.class);
        //                        startActivity(intent);
        //                    finish();}
        //
        //                else{
        //                    Toast.makeText(getApplicationContext(), "you can't enter", Toast.LENGTH_SHORT).show();
        //
        //                }}
        //
        //        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }


        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.i("auth2", "auth task ");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        Log.i("auth2", "auth task " + credential);

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i("auth2", "auth before task is successful");
                        Toast.makeText(getApplicationContext(), "welcome before" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Log.i("auth2", "auth task is successful");
                            Toast.makeText(getApplicationContext(), "welcome", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            check_acc = true;
                            Intent intent = new Intent(Login_activity.this, AlarmSettingActivity.class);
                            startActivity(intent);

                            //database 저장
//                          FirebaseDatabase.getInstance().getReference().child()
                            String email=user.getEmail();
                            databaseReference.child("user_info").push().setValue(email);

                        } else {
                            // 로그인 실패
                            Log.i("auth2", "auth task is not successful");
                            Toast.makeText(getApplicationContext(), "retry", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}


//    public void register(View v){
//        Intent intent=new Intent(getApplicationContext(),Register_act.class);
//        startActivityForResult(intent, 1000);
//    }


