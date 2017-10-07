package com.teamnothing.couponate.CouponListActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.teamnothing.couponate.R;
import com.teamnothing.couponate.couponretrieveasynctask.CouponRetrieveAsyncTask;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CouponListActivity extends AppCompatActivity {

    ListView couponListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list);
        Intent intent = getIntent();
        couponListView = (ListView) findViewById(R.id.couponListView);
        ArrayAdapter<String> adapter;
        ArrayList<String> al = null;
        try {
            al = new CouponRetrieveAsyncTask()
                    .execute("" + intent.getIntExtra("UserID", 0), intent.getStringExtra("UserMail")).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        adapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_list_item_1, al);
        couponListView.setAdapter(adapter);

        couponListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData data = ClipData.newPlainText("couponCode", item.trim());
                clipboardManager.setPrimaryClip(data);
                Toast.makeText(getBaseContext(), "Coupon copied to Clipboard!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
