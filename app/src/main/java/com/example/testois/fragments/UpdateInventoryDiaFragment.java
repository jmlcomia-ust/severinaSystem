package com.example.testois.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testois.DashboardOrders;
import com.example.testois.Inventory;
import com.example.testois.Orders;
import com.example.testois.R;
import com.example.testois.severinaDB;

public class UpdateInventoryDiaFragment extends DialogFragment {

    private static final String TAG = "UpdateInvFrag";

    public interface OnInputListener {
        void UpdateInput(String name, String qty, String desc);
    }
    public UpdateInventoryDiaFragment.OnInputListener fragupdate;

    private EditText inv_name_txt, inv_qty_txt, inv_desc_txt;
    private ImageView inv_imgu;
    String qty_updated, name_updated, desc_updated;
    private Button btn_update, btn_back, increment, decrement;
    private severinaDB db;
    private SQLiteDatabase sql;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_inv, container, false);
        // Get field from view
        inv_name_txt = view.findViewById(R.id.inv_name_txtu);
        inv_imgu = view.findViewById(R.id.inv_imgu);
        inv_qty_txt = view.findViewById(R.id.inv_qty_txtu);
        inv_desc_txt = view.findViewById(R.id.inv_desc_txtu);
        btn_update = view.findViewById(R.id.btn_update);
        btn_back = view.findViewById(R.id.btn_back);
        increment = view.findViewById(R.id.increment);
        decrement = view.findViewById(R.id.decrement);

        // FUNCTION TO SET EXSISTING DATA TO EDITTEXTS> setData();

        increment.setOnClickListener(v -> {
            int quantity = Integer.parseInt(inv_qty_txt.getText().toString());
            quantity++;
            inv_qty_txt.setText(String.valueOf(quantity));

        });
        decrement.setOnClickListener(v -> {
            int quantity = Integer.parseInt(inv_qty_txt.getText().toString());
            quantity--;
            inv_qty_txt.setText(String.valueOf(quantity));
        });
        btn_back.setOnClickListener(v -> {
            Log.d(TAG, "onClick: closing dialog");
            getDialog().dismiss();
        });
        btn_update.setOnClickListener(v ->{
            Log.d(TAG, "onClick: capturing updates");
            name_updated = inv_name_txt.getText().toString();
            qty_updated = inv_qty_txt.getText().toString();
            desc_updated = inv_desc_txt.getText().toString();

            fragupdate.UpdateInput(name_updated, qty_updated, desc_updated);
            getDialog().dismiss();
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            fragupdate = (UpdateInventoryDiaFragment.OnInputListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }
}
