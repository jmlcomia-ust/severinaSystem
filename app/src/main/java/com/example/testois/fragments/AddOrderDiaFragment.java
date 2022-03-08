//Reference: https://github.com/mitchtabian/DialogFragmentToActivity

package com.example.testois.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.testois.R;
import com.example.testois.severinaDB;


public class AddOrderDiaFragment extends DialogFragment {
    private static final String TAG = "AddOrderDiaFragment";

    public interface OnInputListener {
        void sendInput(String name, String qty, String stat);
    }
    public OnInputListener fraglisten;
    private EditText ord_name_txt, ord_qty_txt, ord_stat_txt;
    private Button btn_add, btn_back;
    private severinaDB db;
    private SQLiteDatabase sql;

    public AddOrderDiaFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AddOrderDiaFragment newInstance(String name, String quantity, String stat) {
        AddOrderDiaFragment frag = new AddOrderDiaFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("quantity", quantity);
        args.putString("stat", stat);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_order_dia, container, false);
        // Get field from view
        ord_name_txt = view.findViewById(R.id.ord_name_txt);
        ord_qty_txt = view.findViewById(R.id.ord_qty_txt);
        ord_stat_txt = view.findViewById(R.id.ord_stat_txt);
        btn_add = view.findViewById(R.id.btn_add_ord);
        btn_back = view.findViewById(R.id.btn_back_ord);

        btn_back.setOnClickListener(v -> {
            Log.d(TAG, "onClick: closing dialog");
            getDialog().dismiss();
        });
        btn_add.setOnClickListener(v ->{
            Log.d(TAG, "onClick: capturing input");
            String name = ord_name_txt.getText().toString();
            String qty = ord_qty_txt.getText().toString();
            String stat = ord_stat_txt.getText().toString();
            fraglisten.sendInput(name, qty, stat);
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