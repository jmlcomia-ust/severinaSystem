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
import android.widget.Spinner;

import com.example.testois.Orders;
import com.example.testois.R;
import com.example.testois.ViewOrder;
import com.example.testois.adapter.CustomViewAdapInv;
import com.example.testois.adapter.CustomViewAdapOrd;
import com.example.testois.severinaDB;

import java.util.List;

public class UpdateOrderDiaFragment extends DialogFragment {
    private static final String TAG = "UpdateOrdFrag";
    public UpdateOrderDiaFragment.OnInputListener fragupdate;
    private EditText ord_id_txt, ord_name_txt, ord_qty_txt , ord_stat_txt ;
    private Spinner ord_desc_txt;
    String id, qty_updated, name_updated, stat_updated, desc_updated;
    private Button btn_add_vord, btn_back_vord;
    private SQLiteDatabase sql;

    public interface OnInputListener {
        //void UpdateInput(int id, String name, int qty, String desc, String stat, byte[] bytesImage);
        void UpdateInput(int id, String name, int qty, String desc, String stat);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_order_dia, container, false);
        ord_id_txt = view.findViewById(R.id.ord_id_txtv);
        ord_name_txt = view.findViewById(R.id.ord_name_txtv);
        ord_qty_txt  = view.findViewById(R.id.ord_qty_txtv);
        ord_desc_txt = view.findViewById(R.id.ord_desc_txtv);
        ord_stat_txt  = view.findViewById(R.id.ord_stat_txtv);
        btn_add_vord = view.findViewById(R.id.btn_add_vord);
        btn_back_vord = view.findViewById(R.id.btn_back_vord);

        Bundle args = getArguments();
        ord_id_txt.setText(args.getString("id"));
        ord_name_txt.setText(args.getString("name"));
        ord_qty_txt .setText(args.getString("qty"));
        //ord_desc_txt.setAdapter();
        ord_stat_txt .setText(args.getString("stat"));

        btn_back_vord.setOnClickListener(v -> {
            Log.d(TAG, "onClick: closing dialog");
            getDialog().dismiss();
        });
        btn_add_vord.setOnClickListener(v ->{
            Log.d(TAG, "onClick: capturing updates");
            id = ord_id_txt.getText().toString();
            name_updated = ord_name_txt.getText().toString();
            qty_updated = ord_qty_txt .getText().toString();
            //desc_updated = ord_desc_txt.getText().toString();
            stat_updated = ord_stat_txt .getText().toString();
            fragupdate.UpdateInput(Integer.parseInt(id),name_updated, Integer.parseInt(qty_updated),desc_updated, stat_updated);
            getDialog().dismiss();
            getActivity().recreate();
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            fragupdate = (UpdateOrderDiaFragment.OnInputListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }
}

