package com.example.ilgwon.alarmbomb.user_interface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class MainActivity extends Activity {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
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
    private Button signupBtn ;
    private Button signinBtn;
    private EditText signinEmail;
    private EditText signinPassword;
    private String email = "";
    private String password = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signinEmail = findViewById(R.id.signinID);
        signinPassword = findViewById(R.id.signinPassword);
        firebaseAuth = FirebaseAuth.getInstance();

        buttonGoogle = findViewById(R.id.btn_googleSignIn);

        signupBtn  = findViewById(R.id.signupBtn);
        signinBtn  = findViewById(R.id.signinBtn);
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
        //                        Intent intent=new Intent(MainActivity.this,AlarmSettingActivity.class);
        //                        startActivity(intent);
        //                    finish();}
        //
        //                else{
        //                    Toast.makeText(getApplicationContext(), "you can't enter", Toast.LENGTH_SHORT).show();
        //
        //                }}
        //
        //        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(signUpIntent, 7979);
            }
        });
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(view);
            }
        });

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
                            Intent intent = new Intent(MainActivity.this, AlarmSettingActivity.class);
                            startActivity(intent);
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


    public void signIn(View view) {
        email = signinEmail.getText().toString();
        password = signinPassword.getText().toString();

        if(isValidEmail() && isValidPasswd()) {
            loginUser(email, password);
        }
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 로그인
    private void loginUser(String email, String password)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(MainActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, AlarmSettingActivity.class);
                            startActivity(intent);
                        } else {
                            // 로그인 실패
                            Toast.makeText(MainActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}


//    public void register(View v){
//        Intent intent=new Intent(getApplicationContext(),Register_act.class);
//        startActivityForResult(intent, 1000);
//    }


