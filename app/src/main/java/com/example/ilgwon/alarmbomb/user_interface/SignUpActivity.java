package com.example.ilgwon.alarmbomb.user_interface;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ilgwon.alarmbomb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    // 이메일과 비밀번호
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextAccountNum;
    private String editTextAccountBank;
    private EditText editTextUserID;
    private EditText editTextPhone;
    private Button btn;
    private Spinner s;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase = firebaseDatabase.getReference();

    private String email = "";
    private String password = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.signupEmail);
        editTextPassword = findViewById(R.id.signupPassword);
        editTextAccountNum = findViewById(R.id.signupAccountNum);
        editTextUserID = findViewById(R.id.signupUserID);
        editTextPhone = findViewById(R.id.signupPhone);
        btn = findViewById(R.id.signupBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singUp(view);
            }
        });

        s = (Spinner) findViewById(R.id.signupBank);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editTextAccountBank = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void singUp(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        if (isValidEmail() && isValidPasswd()) {
            createUser(email, password);
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

    // 회원가입
    private void createUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            Toast.makeText(SignUpActivity.this, R.string.success_signup, Toast.LENGTH_SHORT).show();
                            String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                            mDatabase.child("users").child(uid).child("uid").setValue(uid);
                            mDatabase.child("users").child(uid).child("email").setValue(editTextEmail.getText().toString());
                            mDatabase.child("users").child(uid).child("password").setValue(editTextPassword.getText().toString());
                            mDatabase.child("users").child(uid).child("accountNumber").setValue(editTextAccountNum.getText().toString());
                            mDatabase.child("users").child(uid).child("accountBank").setValue(editTextAccountBank);
                            mDatabase.child("users").child(uid).child("userID").setValue(editTextUserID.getText().toString());
                            mDatabase.child("users").child(uid).child("phone").setValue(editTextPhone.getText().toString());
                            loginUser(editTextEmail.getText().toString(),editTextPassword.getText().toString());
                        } else {
                            // 회원가입 실패
                            Toast.makeText(SignUpActivity.this, R.string.failed_signup, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                            Toast.makeText(SignUpActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, AlarmSettingActivity.class);
                            startActivity(intent);
                        } else {
                            // 로그인 실패
                            Toast.makeText(SignUpActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
