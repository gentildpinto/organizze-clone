package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.FirebaseConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        EditText email = findViewById(R.id.email_login_field);
        EditText password = findViewById(R.id.password_login_field);

        Button loginBtn = findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }

    private void login(String email, String password) {
        auth = FirebaseConfig.GetFirebaseInstance();
        auth.signInWithEmailAndPassword(
                email, password
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignInActivity.this, "Logado com sucesso!", Toast.LENGTH_SHORT).show();
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
            }
        });
    }
}