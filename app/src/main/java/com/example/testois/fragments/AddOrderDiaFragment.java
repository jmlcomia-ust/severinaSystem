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


public class AddOrderDiaFragment extends DialogFragment {

    private static final String TAG = "AddOrderDiaFragment";
    private EditText ord_name_txt, ord_qty_txt , ord_stat_txt ;
    private Button btn_add, btn_back, btn_insert;

    private severinaDB sev;
    private SQLiteDatabase db;

    public interface OnInputListener {

        void sendInput(String name, String qty, String stat);
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
            ord_stat_txt = view.findViewById(R.id.ord_stat_txt);
            btn_add = view.findViewById(R.id.btn_add_ord);
            btn_back = view.findViewById(R.id.btn_back_ord);
            sev = new severinaDB(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        btn_back.setOnClickListener(v -> {
            Log.d(TAG, "onClick: closing dialog");
            getDialog().dismiss();
        });

        btn_add.setOnClickListener(v -> {
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
                fraglisten = (AddOrderDiaFragment.OnInputListener) getActivity();

            }catch (ClassCastException e){
                Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
            }
        }
    }