package com.example.testois.fragments;

import android.content.Context;
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
import android.widget.TextView;

import com.example.testois.R;
import com.example.testois.utilities.severinaDB;

import java.util.List;

public class UpdateOrderDiaFragment extends DialogFragment {
    private static final String TAG = "UpdateOrdFrag";
    public UpdateOrderDiaFragment.OnInputListener fragupdate;
    private TextView ord_id_txt, ord_desc_txt, ord_stat_txt;
    private EditText ord_name_txt, ord_qty_txt;
    private Spinner ord_desc_drop, ord_stat_drop;
    int id, qty_updated;
    String name_updated, stat_updated, desc_updated;
    private Button btn_add_vord, btn_back_vord;
    private severinaDB db;

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
        ord_desc_drop = view.findViewById(R.id.ord_desc_dropv);
        ord_desc_txt = view.findViewById(R.id.ord_desc_txtv);
        ord_stat_drop = view.findViewById(R.id.ord_stat_dropv);
        ord_stat_txt  = view.findViewById(R.id.ord_stat_txtv);
        btn_add_vord = view.findViewById(R.id.btn_add_vord);
        btn_back_vord = view.findViewById(R.id.btn_back_vord);
        db = new severinaDB(getActivity());
        loadSpinnerDescData();
        loadSpinnerStatData();

        Bundle args = getArguments();
        ord_id_txt.setText(args.getString("id"));
        ord_name_txt.setText(args.getString("name"));
        ord_qty_txt.setText(args.getString("qty"));
        ord_desc_txt.setText(args.getString("desc"));
        ord_stat_txt .setText(args.getString("stat"));

        btn_back_vord.setOnClickListener(v -> {
            Log.d(TAG, "onClick: closing dialog");
            getDialog().dismiss();
        });
        btn_add_vord.setOnClickListener(v ->{
            Log.d(TAG, "onClick: capturing updates");
            id = Integer.parseInt(ord_id_txt.getText().toString());
            name_updated = ord_name_txt.getText().toString();
            qty_updated =  Integer.parseInt(ord_qty_txt .getText().toString());
            desc_updated = ord_desc_txt.getText().toString();
            stat_updated = ord_stat_txt .getText().toString();
            fragupdate.UpdateInput(id,name_updated, qty_updated,desc_updated, stat_updated);
            getDialog().dismiss();
            getActivity().recreate();
        });
        return view;
    }
    //REF: https://www.androidhive.info/2012/06/android-populating-spinner-data-from-sqlite-database/
    private void loadSpinnerDescData() {
        db = new severinaDB(getActivity());
        List<String> inventory_dropdown = db.getInvName();
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, inventory_dropdown);
        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ord_desc_drop.setAdapter(itemAdapter);
        ord_desc_drop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                ord_desc_txt.setText(item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadSpinnerStatData() {
        String[] choice = new String[]{"TODAY", "DELIVERED"};
        ArrayAdapter<String> statAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, choice);
        statAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ord_stat_drop.setAdapter(statAdapter);
        ord_stat_drop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                ord_stat_txt.setText(item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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

