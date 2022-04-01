package com.example.testois.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testois.Inventory;
import com.example.testois.R;
import com.example.testois.adapter.CustomViewAdapInv;
import com.example.testois.severinaDB;

import java.util.List;

public class DeleteInventoryDiagFragment extends DialogFragment {
    private static final String TAG = "DeleteInvFrag";
    public DeleteInventoryDiagFragment.OnInputListener fragdel;
    severinaDB db = new severinaDB(getActivity());
    CustomViewAdapInv adapter ;
    public interface OnInputListener {
        void DeleteInput(String id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_inventory_diag, container, false);
        // Get field from view
        TextView message = view.findViewById(R.id.message);
        TextView inv_data = view.findViewById(R.id.inv_data);
        Button btn_del = view.findViewById(R.id.btn_delete_delfrag);
        Button btn_back = view.findViewById(R.id.btn_back_delfrag);

        Bundle args = getArguments();
        String inv_data_id = args.getString("id");
        String inv_data_name = args.getString("name");
        String inv_data_qty = args.getString("qty");
        inv_data.setText("ID: "+inv_data_id.trim()+ "\nITEM NAME: "+inv_data_name.toUpperCase()+"\nQTY: "+inv_data_qty+" PCS. ");

        btn_del.setOnClickListener(v -> {
            Log.d(TAG, "onClick: capturing deletion");
            //String stringFilePath = Environment.getExternalStorageDirectory().getPath()+"/Download/"+inv_name_txt.getText().toString()+".jpeg";
            //Bitmap bitmap = BitmapFactory.decodeFile(stringFilePath);
            //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);


            //fragupdate.UpdateInput(name_updated, qty_updated, desc_updated, img_updated);
            fragdel.DeleteInput(inv_data_id.trim());
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
            fragdel = (DeleteInventoryDiagFragment.OnInputListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }
}
