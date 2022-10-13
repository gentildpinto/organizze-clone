package com.example.organizze.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.organizze.R;
import com.github.clans.fab.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private FloatingActionButton expenses, incomes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViews();
        incomesFABOnclickListener();
        expensesFABOnclickListener();
    }

    private void setViews() {
        incomes = findViewById(R.id.incomes);
        expenses = findViewById(R.id.expenses);
    }

    private void incomesFABOnclickListener() {
        incomes.setOnClickListener(v -> startActivity(new Intent(this, IncomeActivity.class)));
    }

    private void expensesFABOnclickListener() {
        expenses.setOnClickListener(v -> startActivity(new Intent(this, ExpenseActivity.class)));
    }
}