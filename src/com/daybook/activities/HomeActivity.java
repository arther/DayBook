package com.daybook.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.daybook.datastore.BillDataStore;
import com.daybook.model.Bill;
import com.daybook.util.Report;

import java.util.List;

public class HomeActivity extends Activity {
    private int billNumber;
    private int billAmount;
    private Button save;
    private Button edit;
    private BillDataStore billDataStore;
    private Button summary;
    private Button missingBills;
    private Button report;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        int maxLength = 4;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final EditText billNumberView = (EditText) findViewById(R.id.bill_number);
        final EditText billAmountView = (EditText) findViewById(R.id.bill_amount);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            billNumberView.setText(extras.getString("Number"));
        }

        billNumberView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        billDataStore = new BillDataStore(HomeActivity.this);
        save = (Button) findViewById(R.id.save_button);
        edit = (Button) findViewById(R.id.edit_button);
        summary = (Button) findViewById(R.id.summary_button);
        missingBills = (Button) findViewById(R.id.missing_bill_button);
        report = (Button) findViewById(R.id.report_button);

        billNumberView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Integer keyCode = keyEvent.getKeyCode();
                if (keyCode != KeyEvent.KEYCODE_DEL && keyCode != KeyEvent.KEYCODE_FORWARD_DEL) {
                    if (billNumberView.getText().length() == 4) {
                        billAmountView.requestFocus();
                    }
                }
                return false;
            }
        });

        setSaveOnClick(billAmountView, billNumberView);

        setEditOnClick(billAmountView, billNumberView);

        setSummaryOnclick();

        setMissingOnclick();

        setReportOnclick();

    }

    private void setReportOnclick() {
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Bill> bills = billDataStore.getBillsOf(1);
                new Report().generateReport(bills);
            }
        });
    }

    private void setMissingOnclick() {
        missingBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MissingBills.class);
                startActivity(intent);
            }
        });
    }

    private void setSummaryOnclick() {
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Summary.class);
                startActivity(intent);
            }
        });
    }

    private void setSaveOnClick(final EditText billAmountView, final EditText billNumberView) {
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "Bill Added", Toast.LENGTH_LONG);
                billAmount = Integer.valueOf(billAmountView.getText().toString());
                billNumber = Integer.valueOf(billNumberView.getText().toString());
                try{
                    billDataStore.addBill(new Bill(billNumber, billAmount));
                }catch (SQLException exception){
                    toast = Toast.makeText(getApplicationContext(), "Bill Already exists",
                            Toast.LENGTH_LONG);
                }
                toast.show();
                billAmountView.setText("");
                billNumberView.setText("");
                billNumberView.requestFocus();
            }
        });
    }

    private void setEditOnClick(final EditText billAmountView, final EditText billNumberView) {
        edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                billNumber = Integer.valueOf(billNumberView.getText().toString());
                Bill resultBill = billDataStore.findBillWith(billNumber);
                billAmountView.setText("");
                if (resultBill != null) {
                    billAmountView.setText(String.valueOf(resultBill.getAmount()));
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Bill Number not found",
                            Toast.LENGTH_LONG);
                    Spannable spannable = new SpannableString(String.valueOf(billNumber));
                    spannable.setSpan(new ForegroundColorSpan(Color.RED), 0, 4, Spanned.SPAN_MARK_MARK);
                    billNumberView.setText(spannable);
                    billNumberView.selectAll();
                    toast.show();
                }
            }
        });
    }
}
