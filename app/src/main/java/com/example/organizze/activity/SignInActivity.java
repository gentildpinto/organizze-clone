package com.example.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.FirebaseConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class SignInActivity extends AppCompatActivity {
    private Button loginBtn;
    private TextView signUpBtn;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        setViews();
        loginBtnClickListener();
        signUpBtnClickListener();
    }

    private void setViews() {
        email = findViewById(R.id.email_login_field);
        password = findViewById(R.id.password_login_field);
        loginBtn = findViewById(R.id.login_btn);
        signUpBtn = findViewById(R.id.create_account_btn);
    }

    private void loginBtnClickListener() {
        loginBtn.setOnClickListener(v -> {
            if (!email.getText().toString().isEmpty()) {
                if ((!password.getText().toString().isEmpty())) {
                    login(
                            email.getText().toString(),
                            password.getText().toString()
                    );
                } else {
                    Toast.makeText(SignInActivity.this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SignInActivity.this, "Preencha o email!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signUpBtnClickListener() {
        signUpBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        });
    }

    private void login(String email, String password) {
        FirebaseAuth auth = FirebaseConfig.getFirebaseInstance();
        auth.signInWithEmailAndPassword(
                email, password
        ).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(SignInActivity.this, "Logado com sucesso!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                String exception = "";
                try {
                    throw task.getException();
                } catch (FirebaseAuthInvalidUserException e) {
                    exception = "Email inválido!";
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    exception = "Senha inválida!";
                } catch (Exception e) {
                    exception = "Erro ao entrar! " + e.getMessage();
                    e.printStackTrace();
                }

                Toast.makeText(SignInActivity.this, exception, Toast.LENGTH_SHORT).show();
            }
        });
    }
}