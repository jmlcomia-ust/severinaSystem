//Reference: https://github.com/mitchtabian/DialogFragmentToActivity

package com.example.testois.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testois.R;
import com.example.testois.utilities.severinaDB;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;


public class AddOrderDiaFragment extends DialogFragment {

    private static final String TAG = "AddOrderDiaFragment";
    private EditText ord_name_txt, ord_qty_txt, ord_date_txt;
    private TextView ord_stat_txt, ord_desc_txt;
    Spinner ord_desc_drop, ord_stat_drop;
    Button btn_add, btn_back, btn_insert;
    private severinaDB db;
    DatePickerDialog picker;


    public interface OnInputListener {

        void sendInput(String name, int qty, String desc, String date, String stat);
    }

    public OnInputListener fraglisten;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_order_dia, container, false);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        ord_name_txt = view.findViewById(R.id.ord_name_txt);
        ord_qty_txt = view.findViewById(R.id.ord_qty_txt);
        ord_desc_drop = view.findViewById(R.id.ord_desc_drop);
        ord_desc_txt = view.findViewById(R.id.ord_desc_txt);
        ord_stat_drop = view.findViewById(R.id.ord_stat_drop);
        ord_date_txt = view.findViewById(R.id.ord_date_txt);
        ord_stat_txt = view.findViewById(R.id.ord_stat_txt);
        btn_add = view.findViewById(R.id.btn_add_ord);
        btn_back = view.findViewById(R.id.btn_back_ord);
        db = new severinaDB(getContext());

            /*
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String date = sdf.format(System.currentTimeMillis());
            String dayToday    = (String) DateFormat.format("dd", Long.parseLong(date)); // 20
            String monthToday  = (String) DateFormat.format("MM", Long.parseLong(date)); // 06
            String yearToday   = (String) DateFormat.format("yyyy", Long.parseLong(date)); // 2013
            */
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

                /*
                if(day < Integer.parseInt(dayToday)){
                    ord_stat_txt.setText("");
                    ord_stat_txt.requestFocus();
                    ord_stat_txt.setError("Date should be picked from today onwards.");
                }
                 */
        });


        btn_back.setOnClickListener(v -> {
            Log.d(TAG, "onClick: closing dialog");
            getDialog().dismiss();
        });

        btn_add.setOnClickListener(v -> {
            if (!ord_name_txt.getText().toString().isEmpty() && !ord_qty_txt.getText().toString().isEmpty() && !ord_stat_txt.getText().toString().isEmpty()) {
                Log.d(TAG, "onClick: capturing input");
                String name = ord_name_txt.getText().toString().toUpperCase();
                int qty = Integer.parseInt(ord_qty_txt.getText().toString());
                String item = ord_desc_txt.getText().toString().toUpperCase();
                String date = ord_date_txt.getText().toString().toUpperCase();
                String stat = ord_stat_txt.getText().toString().toUpperCase();
                fraglisten.sendInput(name, qty, item, date, stat);
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
                fraglisten = (AddOrderDiaFragment.OnInputListener) getActivity();
                //checkInventory = (AddOrderDiaFragment.CheckInventory) getActivity();

            }catch (ClassCastException e){
                Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
            }
        }
    }