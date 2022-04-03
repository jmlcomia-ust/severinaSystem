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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.testois.Orders;
import com.example.testois.R;
import com.example.testois.severinaDB;

import java.util.List;


public class AddOrderDiaFragment extends DialogFragment{

    private static final String TAG = "AddOrderDiaFragment";
    private EditText ord_name_txt, ord_qty_txt , ord_stat_txt ,ord_desc_txt;
    Spinner ord_desc_drop, ord_stat_drop;
    Button btn_add, btn_back, btn_insert;
    private severinaDB db;
    public interface OnInputListener {

        void sendInput(String name, int qty, String desc, String stat);
    }
    public OnInputListener fraglisten;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_order_dia, container, false);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

        try {
            ord_name_txt = view.findViewById(R.id.ord_name_txt);
            ord_qty_txt = view.findViewById(R.id.ord_qty_txt);
            ord_desc_drop = view.findViewById(R.id.ord_desc_drop);
            ord_desc_txt = view.findViewById(R.id.ord_desc_txt);
            ord_stat_drop = view.findViewById(R.id.ord_stat_drop);
            ord_stat_txt = view.findViewById(R.id.ord_stat_txt);
            btn_add = view.findViewById(R.id.btn_add_ord);
            btn_back = view.findViewById(R.id.btn_back_ord);
            db = new severinaDB(getContext());
            loadSpinnerDescData();
            loadSpinnerStatData();

            btn_back.setOnClickListener(v -> {
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();
            });

            btn_add.setOnClickListener(v -> {
                if(!ord_name_txt.getText().toString().isEmpty() && !ord_qty_txt.getText().toString().isEmpty() && !ord_stat_txt.getText().toString().isEmpty()){
                    Log.d(TAG, "onClick: capturing input");
                    loadSpinnerDescData();
                    loadSpinnerStatData();
                    String name = ord_name_txt.getText().toString().toUpperCase();
                    int qty = Integer.parseInt(ord_qty_txt.getText().toString());
                    String stat = ord_stat_txt.getText().toString().toUpperCase();
                    String item = ord_desc_txt.getText().toString().toUpperCase();
                    //db.checkStocks(orders, inventory);
                    fraglisten.sendInput(name, qty, item, stat);
                    getDialog().dismiss();
                    getActivity().recreate();
                }
                else if(ord_name_txt.getText().toString().isEmpty()){ ord_name_txt.requestFocus(); }
                else if(ord_qty_txt.getText().toString().isEmpty()){ ord_qty_txt.requestFocus(); }
                else if(ord_stat_txt.getText().toString().isEmpty()){ord_stat_txt.requestFocus(); }
                else{Toast.makeText(getContext(), "Error! Please fill out all the needed inputs.", Toast.LENGTH_SHORT).show(); }

            });

            ord_desc_drop.setOnItemClickListener((parent, view1, position, id) -> {
                String item = parent.getItemAtPosition(position).toString();
                ord_desc_txt.setText(item);
            });

            ord_stat_drop.setOnItemClickListener((parent, view1, position, id) -> {
                String stat = parent.getItemAtPosition(position).toString();
                ord_desc_txt.setText(stat);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void loadSpinnerDescData() {
        db = new severinaDB(getActivity());
        List<String> inventory_dropdown = db.getInventoryItems();
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, inventory_dropdown);
        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ord_desc_drop.setAdapter(itemAdapter);
    }

    private void loadSpinnerStatData() {
        String[] choice = new String[]{"TODAY", "DELIVERED"};
        ArrayAdapter<String> statAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, choice);
        statAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ord_desc_drop.setAdapter(statAdapter);
    }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            try{
                fraglisten = (AddOrderDiaFragment.OnInputListener) getActivity();
                //checkInventory = (AddOrderDiaFragment.CheckInventory) getActivity();

            }catch (ClassCastException e){
                Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
            }
        }
    }