package com.daybook.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.daybook.activities.R;
import com.daybook.model.Bill;

import java.util.List;

public class SummaryAdapter extends ArrayAdapter<Bill>{
    Context context;
    private List<Bill> bills;
    private int total = 0;

    public SummaryAdapter(Context context, int textViewResourceId, List<Bill> bills) {
        super(context, textViewResourceId, bills);
        this.context = context;
        this.bills = bills;

        for(Bill bill : bills ){
            total += bill.getAmount();
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.summary, parent, false);

        TextView billNumber = (TextView) rowView.findViewById(R.id.summary_bill_number);
        TextView billAmount = (TextView) rowView.findViewById(R.id.summary_bill_amount);

        Bill currentBill = bills.get(position);

        Integer number = currentBill.getNumber();
        billNumber.setText(String.valueOf(number));
        billAmount.setText(String.valueOf(currentBill.getAmount()));

        if(number == 0){
            billNumber.setText("Total");
        }

        return rowView;
    }
}