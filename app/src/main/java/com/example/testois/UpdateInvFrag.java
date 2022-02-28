package com.example.testois;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class UpdateInvFrag extends DialogFragment {

    private static final String TAG = "UpdateInvFrag";

    public interface OnInputListener{
        void sendInput(String input);
    }
    public OnInputListener mOnInputListener;

    //widgets
    private EditText item_desc;
    private TextView item_name, item_qty;
    private ImageView img_item;
    private Button btn_update, btn_back;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_inv, container, false);

        item_name = view.findViewById(R.id.item_name);
        item_qty = view.findViewById(R.id.item_qt);
        item_desc = view.findViewById(R.id.item_desc);
        btn_update = view.findViewById(R.id.btn_update);
        btn_back = view.findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();
            }
        });


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: capturing input");

                //ContentValues to replace item_values and values as input ---->

                String item_description = item_desc.getText().toString();
                String item_na = item_name.getText().toString();
                String item_quant = item_qty.getText().toString();
//                if(!input.equals("")){
//                    //Easiest way: just set the value
//                    ((MainActivity)getActivity()).mInputDisplay.setText(input);
//
//                }

                //"Best Practice" but it takes longer
          //      mOnInputListener.sendInput(input);                        <----

            //    getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (OnInputListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }
}
