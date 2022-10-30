package com.example.organizze.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizze.R;
import com.example.organizze.model.Transaction;

import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {
    private List<Transaction> transactions;
    private Context context;

    public TransactionAdapter(List<Transaction> transactions, Context context) {
        this.transactions = transactions;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transaction, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        holder.title.setText(transaction.description);
        holder.value.setText(String.valueOf(transaction.value));
        holder.category.setText(transaction.category);
        holder.value.setTextColor(context.getResources().getColor(R.color.colorAccentIncomes));

        if(transaction.type.equals(Transaction.EXPENSE_TRANSACTION_TYPE)) {
            holder.value.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.value.setText(String.format("-%s", transaction.value));
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, value, category;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.adapterTitle);
            value = itemView.findViewById(R.id.adapterValue);
            category = itemView.findViewById(R.id.adapterCategory);
        }
    }
}
