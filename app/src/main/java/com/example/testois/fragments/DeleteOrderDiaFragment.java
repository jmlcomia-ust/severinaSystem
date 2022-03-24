package com.example.testois.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.testois.Orders;
import com.example.testois.R;
import com.example.testois.adapter.CustomViewAdapInv;
import com.example.testois.severinaDB;


public class DeleteOrderDiaFragment extends DialogFragment {
    private static final String TAG = "DeleteOrdFrag";
    public DeleteOrderDiaFragment.OnInputListener fragdel;
    public interface OnInputListener {
        void DeleteInput(String id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_order_dia, container, false);
        TextView message_ord = view.findViewById(R.id.message);
        TextView ord_data = view.findViewById(R.id.ord_data);
        Button btn_del = view.findViewById(R.id.btn_delete_delfrag);
        Button btn_back = view.findViewById(R.id.btn_back_delfrag);

        Bundle args = getArguments();
        String ord_data_id = args.getString("id");
        String ord_data_name = args.getString("name");
        String ord_data_qty = args.getString("qty");
        message_ord.setText("Are you sure you want to delete this item:");
        ord_data.setText("ID: "+ord_data_id.trim()+ ", Item Name: "+ord_data_name+", Qty: "+ord_data_qty+". ");

        btn_del.setOnClickListener(v -> {
            Log.d(TAG, "onClick: capturing deletion");
            fragdel.DeleteInput(ord_data_id.trim());
            getDialog().dismiss();
            getActivity().recreate();
        });

        btn_back.setOnClickListener(v -> { Log.d(TAG, "Closing: frag"); getDialog().dismiss(); });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragdel = (DeleteOrderDiaFragment.OnInputListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }
}
