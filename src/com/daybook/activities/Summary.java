package com.daybook.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.daybook.datastore.BillDataStore;
import com.daybook.model.Bill;
import com.daybook.util.SummaryAdapter;

import java.util.List;

public class Summary extends Activity {
    private BillDataStore billDataStore;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_list);

        billDataStore = new BillDataStore(getApplicationContext());

        List<Bill> bills = billDataStore.getBillsOf(1);

        bills.add(billTotal(bills));

        ListView listView = (ListView) findViewById(R.id.day_list);

        SummaryAdapter summaryAdapter = new SummaryAdapter(getApplicationContext(), R.layout.summary, bills);

        listView.setAdapter(summaryAdapter);
    }

    private Bill billTotal(List<Bill> bills) {
        int total = 0;
        for(Bill bill : bills){
            total += bill.getAmount();
        }
        return new Bill(0, total);
    }
}