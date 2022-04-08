package com.example.testois.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.text.InputType;
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
import android.widget.Toast;

import com.example.testois.R;
import com.example.testois.utilities.severinaDB;

import java.util.Calendar;
import java.util.List;

public class UpdateOrderDiaFragment extends DialogFragment {
    private static final String TAG = "UpdateOrdFrag";
    public UpdateOrderDiaFragment.OnInputListener fragupdate;
    private TextView ord_id_txt, ord_desc_txt, ord_stat_txt;
    private EditText ord_name_txt, ord_qty_txt, ord_date_txt;
    private Spinner ord_desc_drop, ord_stat_drop;
    int id, qty_updated;
    String name_updated, stat_updated, desc_updated, date_updated;
    private Button btn_add_vord, btn_back_vord;
    private severinaDB db;
    DatePickerDialog picker;

    public interface OnInputListener {
        //void UpdateInput(int id, String name, int qty, String desc, String stat, byte[] bytesImage);
        void UpdateInput(int id, String name, int qty, String desc, String date, String stat);
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
        ord_date_txt = view.findViewById(R.id.ord_date_txtv);
        ord_stat_drop = view.findViewById(R.id.ord_stat_dropv);
        ord_stat_txt  = view.findViewById(R.id.ord_stat_txtv);
        btn_add_vord = view.findViewById(R.id.btn_add_vord);
        btn_back_vord = view.findViewById(R.id.btn_back_vord);
        db = new severinaDB(getActivity());
        loadSpinnerDescData();
        loadSpinnerStatData();

        ord_date_txt.setInputType(InputType.TYPE_NULL);
        ord_date_txt.setOnClickListener(v -> {
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog

                    picker = new DatePickerDialog(getActivity(),
                            (view1, year1, monthOfYear, dayOfMonth) -> ord_date_txt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
                    picker.show();
                });

        Bundle args = getArguments();
        ord_id_txt.setText(args.getString("id"));
        ord_name_txt.setText(args.getString("name"));
        ord_qty_txt.setText(String.valueOf(args.getInt("qty")));
        ord_desc_txt.setText(args.getString("desc"));
        ord_date_txt.setText(args.getString("date"));
        ord_stat_txt .setText(args.getString("stat"));

        btn_back_vord.setOnClickListener(v -> {
            Log.d(TAG, "onClick: closing dialog");
            getDialog().dismiss();
        });
        btn_add_vord.setOnClickListener(v ->{
            Log.d(TAG, "onClick: capturing updates");
            if (!ord_name_txt.getText().toString().isEmpty() && !ord_qty_txt.getText().toString().isEmpty() && !ord_stat_txt.getText().toString().isEmpty()) {
                id = Integer.parseInt(ord_id_txt.getText().toString());
                name_updated = ord_name_txt.getText().toString();
                qty_updated =  Integer.parseInt(ord_qty_txt .getText().toString());
                desc_updated = ord_desc_txt.getText().toString();
                date_updated = ord_date_txt.getText().toString();
                stat_updated = ord_stat_txt .getText().toString();
                fragupdate.UpdateInput(id,name_updated, qty_updated,desc_updated, date_updated, stat_updated);
                getDialog().dismiss();
                getActivity().recreate();
            } else if (ord_name_txt.getText().toString().isEmpty()) {
                ord_name_txt.requestFocus();
                Toast.makeText(getContext(), "Error! Please fill out all the needed inputs.", Toast.LENGTH_SHORT).show();
            } else if (ord_qty_txt.getText().toString().isEmpty()) {
                ord_qty_txt.requestFocus();
                Toast.makeText(getContext(), "Error! Please fill out all the needed inputs.", Toast.LENGTH_SHORT).show();
            } else if (ord_date_txt.getText().toString().isEmpty()) {
                ord_date_txt.requestFocus();
                Toast.makeText(getContext(), "Error! Please fill out all the needed inputs.", Toast.LENGTH_SHORT).show();
            } else if (ord_stat_txt.getText().toString().isEmpty()) {
                ord_stat_txt.requestFocus();
                Toast.makeText(getContext(), "Error! Please fill out all the needed inputs.", Toast.LENGTH_SHORT).show();
            }
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
                ord_desc_txt.getText();
            }
        });
    }

    private void loadSpinnerStatData() {
        String[] choice = new String[]{"TO DELIVER", "DELIVERED"};
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
                ord_stat_txt.getText();
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

