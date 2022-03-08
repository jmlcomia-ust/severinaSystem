//Reference: https://github.com/mitchtabian/DialogFragmentToActivity

package com.example.testois.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.testois.R;
import com.example.testois.severinaDB;

public class AddInventoryDiaFragment extends DialogFragment {

    private static final String TAG = "AddInventoryDiaFragment";

    public interface OnInputListener {
        void sendInput(String name, String qty, String desc);
    }
    public OnInputListener fraglisten;

    private EditText inv_name_txt, inv_qty_txt, inv_desc_txt;
    private Button btn_add, btn_back, increment, decrement;
    private severinaDB severinadb;
    private SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_inventory_dia, container, false);
        // Get field from view
        inv_name_txt = view.findViewById(R.id.inv_name_txt);
        inv_qty_txt = view.findViewById(R.id.inv_qty_txt);
        inv_desc_txt = view.findViewById(R.id.inv_desc_txt);

        btn_add = view.findViewById(R.id.btn_add_inv);
        btn_back = view.findViewById(R.id.btn_back_inv);
        increment = view.findViewById(R.id.increment);
        decrement = view.findViewById(R.id.decrement);

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
        btn_add.setOnClickListener(v ->{
            Log.d(TAG, "onClick: capturing input");
            String name = inv_name_txt.getText().toString();
            String qty = inv_qty_txt.getText().toString();
            String desc = inv_desc_txt.getText().toString();
            fraglisten.sendInput(name, qty, desc);
            getDialog().dismiss();
            getActivity().recreate();

        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            fraglisten = (OnInputListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }
}