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
import com.example.organizze.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText name = findViewById(R.id.name_field);
        EditText email = findViewById(R.id.email_field);
        EditText password = findViewById(R.id.password_field);
        Button registerBtn = findViewById(R.id.register_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().isEmpty()) {
                    if (!email.getText().toString().isEmpty()) {
                        if (!password.getText().toString().isEmpty()) {
                            User user = new User(name.getText().toString(), email.getText().toString(), password.getText().toString());
                            createUser(user);
                        } else {
                            Toast.makeText(SignUpActivity.this, "Preencha o campo da senha!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "Preencha o campo do email!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Preencha o campo do nome!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createUser(User user) {
        auth = FirebaseConfig.GetFirebaseInstance();
        auth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    String exception = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        exception = "Com pelo menos 6 carateres";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        exception = "Email inválido!";
                    } catch (FirebaseAuthUserCollisionException e) {
                        exception = "Usuário já existe!";
                    } catch (Exception e) {
                        exception = "Erro ao cadastrar usuário! " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(SignUpActivity.this, exception, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}