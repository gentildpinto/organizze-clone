package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.FirebaseConfig;
import com.example.organizze.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class SignUpActivity extends AppCompatActivity {
    private Button registerBtn;
    private TextView signInBtn;
    private EditText name, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setViews();
        signInBtnClickListener();
        registerBtnClickListener();
    }

    private void setViews() {
        name = findViewById(R.id.name_field);
        email = findViewById(R.id.email_field);
        password = findViewById(R.id.password_field);
        registerBtn = findViewById(R.id.register_btn);
        signInBtn = findViewById(R.id.sign_in_btn);
    }

    private void registerBtnClickListener() {
        registerBtn.setOnClickListener(v -> {
            if (!name.getText().toString().isEmpty()) {
                if (!email.getText().toString().isEmpty()) {
                    if (!password.getText().toString().isEmpty()) {
                        String userName = name.getText().toString().trim();
                        String userEmail = email.getText().toString().trim();
                        String userPassword = password.getText().toString().trim();
                        createUser(userName, userEmail, userPassword);
                    } else {
                        Toast.makeText(SignUpActivity.this, "Preencha o campo da senha!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Preencha o campo do email!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SignUpActivity.this, "Preencha o campo do nome!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signInBtnClickListener() {
        signInBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        });
    }

    private void createUser(String name, String email, String password) {
        FirebaseAuth auth = FirebaseConfig.GetFirebaseInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                User user = new User(name, email);
                user.save();
                Toast.makeText(SignUpActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, SignInActivity.class));
                finish();
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
        });
    }
}