package com.daybook.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.daybook.datastore.BillDataStore;

import java.util.List;

public class MissingBills extends Activity{
    private ListView missingBillList;
    private Button show;
    private EditText startingNumberText;
    private EditText endingNumberText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.missing_bills_list);

        missingBillList = (ListView) findViewById(R.id.missing_bill_list);
        show = (Button) findViewById(R.id.show_button);
        startingNumberText = (EditText) findViewById(R.id.starting_number);
        endingNumberText = (EditText) findViewById(R.id.ending_number);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int startingNumber, endingNumber;

                startingNumber = Integer.valueOf(startingNumberText.getText().toString());
                endingNumber = Integer.valueOf(endingNumberText.getText().toString());

                List<String> missingBills = new BillDataStore(getApplicationContext())
                                                .missingBills(startingNumber, endingNumber);

                ListAdapter missingBillsAdapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.missing_bills,R.id.missing_bill_number, missingBills);

                missingBillList.setAdapter(missingBillsAdapter);

                missingBillList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("Number", String.valueOf(missingBillList.getAdapter().getItem(i)));
                        startActivity(intent);
                    }
                });
            }
        });
    }
}