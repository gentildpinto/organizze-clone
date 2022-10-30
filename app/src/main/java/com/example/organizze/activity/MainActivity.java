package com.example.organizze.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.adapter.TransactionAdapter;
import com.example.organizze.config.FirebaseConfig;
import com.example.organizze.model.Transaction;
import com.example.organizze.model.User;
import com.example.organizze.util.Util;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private DatabaseReference userRef;
    private String balance = "0.0", userID, monthYear;
    private DatabaseReference transactionsRef;
    private MaterialCalendarView calendarView;
    private TransactionAdapter txAdapter;
    private RecyclerView transactionsRecyclerView;
    private FloatingActionButton expenses, incomes;
    private ValueEventListener valueUserEventListener, valueTransactionEventListener;
    private TextView greetingsTextView, balanceTextView;
    private double totalIncome = 0.0, totalExpense = 0.0;
    private List<Transaction> transactions = new ArrayList<>();
    private Transaction transaction;
    private static final CharSequence[] MONTHS = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpViews();
        setUpCalendarView();
        setUpRecyclerView();
        swipe();
        setUpUserID();
        incomesFABOnclickListener();
        expensesFABOnclickListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpResume();
        setUpTransactions();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userRef.removeEventListener(valueUserEventListener);
        transactionsRef.removeEventListener(valueTransactionEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout_btn) {
            FirebaseConfig.getFirebaseInstance().signOut();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateBalance(Transaction tx) {
        userRef = FirebaseConfig.getDatabaseReference().child("users").child(userID);

        if(tx.type.equals(Transaction.INCOME_TRANSACTION_TYPE)) {
            totalIncome -= tx.value;
            userRef.child("totalIncome").setValue(totalIncome);
        } else if(tx.type.equals(Transaction.EXPENSE_TRANSACTION_TYPE)) {
            totalExpense -= tx.value;
            userRef.child("totalExpense").setValue(totalExpense);
        }
    }

    public void swipe() {
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.START | ItemTouchHelper.END);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeTransaction(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(transactionsRecyclerView);
    }

    public void removeTransaction(RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Excluir Transação");
        alertDialog.setMessage("Tem certeza de que quer excluir esta transação?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                transaction = transactions.get(position);

                transactionsRef = FirebaseConfig.getDatabaseReference()
                        .child("transactions")
                        .child(userID)
                        .child(monthYear)
                        .child(transaction.key);
                transactionsRef.removeValue();

                txAdapter.notifyItemRemoved(position);
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                Toast.makeText(MainActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                txAdapter.notifyItemChanged(position);
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void setUpViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        incomes = findViewById(R.id.incomes);
        expenses = findViewById(R.id.expenses);
        balanceTextView = findViewById(R.id.balanceTextView);
        greetingsTextView = findViewById(R.id.greetingsTextView);
        transactionsRecyclerView = findViewById(R.id.transactionsRecyclerView);
    }

    private void setUpCalendarView() {
        calendarView = findViewById(R.id.calendarView);
        calendarView.setTitleMonths(MONTHS);

        CalendarDay currentDate = calendarView.getCurrentDate();
        monthYear = String.format(Locale.getDefault(),"%02d%d", (currentDate.getMonth() + 1), currentDate.getYear());

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                monthYear = String.format(Locale.getDefault(),"%02d%d", (date.getMonth() + 1), date.getYear());

                transactionsRef.removeEventListener(valueTransactionEventListener);
                setUpTransactions();
            }
        });
    }

    private void setUpUserID() {
        String userEmail = Objects.requireNonNull(FirebaseConfig.getFirebaseInstance().getCurrentUser()).getEmail();
        userID = Util.encodeBase64(userEmail);
    }

    private void setUpTransactions() {
        transactionsRef = FirebaseConfig.getDatabaseReference()
                .child("transactions")
                .child(userID)
                .child(monthYear);

        valueTransactionEventListener = transactionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                transactions.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Transaction tx = dataSnapshot.getValue(Transaction.class);
                    assert tx != null;
                    tx.key = dataSnapshot.getKey();
                    transactions.add(tx);
                }

                txAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUpRecyclerView() {
        txAdapter = new TransactionAdapter(transactions, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        transactionsRecyclerView.setLayoutManager(layoutManager);
        transactionsRecyclerView.setHasFixedSize(true);
        transactionsRecyclerView.setAdapter(txAdapter);
    }

    private void setUpResume() {
        userRef = FirebaseConfig.getDatabaseReference().child("users").child(userID);
        valueUserEventListener = userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                assert user != null : "Cannot get user";
                totalExpense = user.totalExpense;
                totalIncome = user.totalIncome;

                DecimalFormat format = new DecimalFormat("0.##");
                balance = format.format(totalIncome - totalExpense);

                greetingsTextView.setText(String.format("Olá, %s", user.name));
                balanceTextView.setText(String.format("%s USD", balance));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void incomesFABOnclickListener() {
        incomes.setOnClickListener(v -> startActivity(new Intent(this, IncomeActivity.class)));
    }

    private void expensesFABOnclickListener() {
        expenses.setOnClickListener(v -> startActivity(new Intent(this, ExpenseActivity.class)));
    }
}