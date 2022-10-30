package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.model.Transaction;
import com.example.organizze.model.User;
import com.example.organizze.util.Util;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class IncomeActivity extends AppCompatActivity {
    private TextInputEditText date, category, description;
    private EditText value;
    private FloatingActionButton saveBtn;
    private double userTotalIncome = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        setViews();
        getUserTotalIncome();
        setSaveClickListener();
    }

    private void setViews() {
        value = findViewById(R.id.income_value);

        date = findViewById(R.id.income_date);
        date.setText(Util.getCurrentDate());

        category = findViewById(R.id.income_category);
        description = findViewById(R.id.income_description);
        saveBtn = findViewById(R.id.save_income_btn);
    }

    private void setSaveClickListener() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    double income = Double.parseDouble(value.getText().toString());
                    Transaction tx = new Transaction(
                            Objects.requireNonNull(category.getText()).toString(),
                            Objects.requireNonNull(date.getText()).toString(),
                            Objects.requireNonNull(description.getText()).toString(),
                            Transaction.INCOME_TRANSACTION_TYPE,
                            income
                    );
                    tx.save();

                    userTotalIncome += income;
                    setUserTotalIncome(userTotalIncome);

                    finish();
                }
            }
        });
    }

    private Boolean validateFields() {
        if (value.getText().toString().isEmpty()) {
            Toast.makeText(this, "Preencha o valor!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (date.getText().toString().isEmpty()) {
            Toast.makeText(this, "Preencha a data!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (category.getText().toString().isEmpty()) {
            Toast.makeText(this, "Preencha a categoria!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void getUserTotalIncome() {
        DatabaseReference userRef = User.getUserReference();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                userTotalIncome = user.totalIncome;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUserTotalIncome(double totalIncome) {
        DatabaseReference userRef = User.getUserReference();
        userRef.child("totalIncome").setValue(totalIncome);
    }
}