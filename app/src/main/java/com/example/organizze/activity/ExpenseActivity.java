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

public class ExpenseActivity extends AppCompatActivity {
    private TextInputEditText date, category, description;
    private EditText value;
    private FloatingActionButton saveBtn;
    private Double userTotalExpense = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        setViews();
        getUserTotalExpense();
        setSaveClickListener();
    }

    private void setViews() {
        value = findViewById(R.id.expense_value);

        date = findViewById(R.id.expense_date);
        date.setText(Util.getCurrentDate());

        category = findViewById(R.id.expense_category);
        description = findViewById(R.id.expense_description);
        saveBtn = findViewById(R.id.save_expense_btn);
    }

    private void setSaveClickListener() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    double expense = Double.parseDouble(value.getText().toString());
                    Transaction tx = new Transaction(
                            Objects.requireNonNull(category.getText()).toString(),
                            Objects.requireNonNull(date.getText()).toString(),
                            Objects.requireNonNull(description.getText()).toString(),
                            Transaction.EXPENSE_TRANSACTION_TYPE,
                            expense
                    );
                    tx.save();

                    userTotalExpense += expense;
                    setUserTotalExpense(userTotalExpense);

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

    private void getUserTotalExpense() {
        DatabaseReference userRef = User.getUserReference();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                userTotalExpense = user.totalExpense;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUserTotalExpense(Double totalExpense) {
        DatabaseReference userRef = User.getUserReference();
        userRef.child("totalExpense").setValue(totalExpense);
    }
}