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
import android.widget.Toast;

import com.example.testois.Inventory;
import com.example.testois.Orders;
import com.example.testois.R;
import com.example.testois.adapter.CustomViewAdapInv;
import com.example.testois.severinaDB;


public class AddOrderDiaFragment extends DialogFragment {

    private static final String TAG = "AddOrderDiaFragment";
    private EditText ord_name_txt, ord_qty_txt , ord_stat_txt ;
    private Button btn_add, btn_back, btn_insert;
    private severinaDB db;
    private SQLiteDatabase sql;
    Orders orders;

    public interface OnInputListener {

        void sendInput(String name, String qty, String stat);
    }
    public OnInputListener fraglisten;

    //public interface CheckInventory { void getQuanty(String qty);}public CheckInventory checkInventory;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_order_dia, container, false);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

        try {
            ord_name_txt = view.findViewById(R.id.ord_name_txt);
            ord_qty_txt = view.findViewById(R.id.ord_qty_txt);
            ord_stat_txt = view.findViewById(R.id.ord_stat_txt);
            btn_add = view.findViewById(R.id.btn_add_ord);
            btn_back = view.findViewById(R.id.btn_back_ord);
            db = new severinaDB(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        btn_back.setOnClickListener(v -> {
            Log.d(TAG, "onClick: closing dialog");
            getDialog().dismiss();
        });

        btn_add.setOnClickListener(v -> {
            if(!ord_name_txt.getText().toString().isEmpty() && !ord_qty_txt.getText().toString().isEmpty() && !ord_stat_txt.getText().toString().isEmpty()){
                Log.d(TAG, "onClick: capturing input");
                String name = ord_name_txt.getText().toString();
                String qty = ord_qty_txt.getText().toString();
                String stat = ord_stat_txt.getText().toString();
                //db.checkStocks(orders, inventory);
                fraglisten.sendInput(name, qty, stat);
                getDialog().dismiss();
                getActivity().recreate();
            }
            else if(ord_name_txt.getText().toString().isEmpty()){ ord_name_txt.requestFocus(); }
            else if(ord_qty_txt.getText().toString().isEmpty()){ ord_qty_txt.requestFocus(); }
            else if(ord_stat_txt.getText().toString().isEmpty()){ord_stat_txt.requestFocus(); }
            else{Toast.makeText(getContext(), "Error! Please fill out all the needed inputs.", Toast.LENGTH_SHORT).show(); }

        });
        return view;
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