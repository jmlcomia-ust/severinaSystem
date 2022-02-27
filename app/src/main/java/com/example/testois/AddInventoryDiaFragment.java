package com.example.testois;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddInventoryDiaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
    public class AddInventoryDiaFragment extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText inv_name_txt, inv_qty_txt, inv_desc_txt;
    private Button btn_add, btn_back;
    private severinaDB severinadb;
    private SQLiteDatabase db;

    public AddInventoryDiaFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AddInventoryDiaFragment newInstance(String name, String quantity, String description) {
        AddInventoryDiaFragment frag = new AddInventoryDiaFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("quantity", quantity);
        args.putString("description", description);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_inventory_dia, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        inv_name_txt = view.findViewById(R.id.inv_name_txt);
        inv_name_txt.setOnEditorActionListener(this);
        inv_qty_txt = view.findViewById(R.id.inv_qty_txt);
        inv_qty_txt.setOnEditorActionListener(this);
        inv_desc_txt = view.findViewById(R.id.inv_desc_txt);
        inv_desc_txt.setOnEditorActionListener(this);

        btn_add = view.findViewById(R.id.btn_add);
        btn_back = view.findViewById(R.id.btn_back);

        btn_back.setOnClickListener(v -> {
           dismiss();
        });
        btn_add.setOnClickListener(v ->{

        });

        // Fetch arguments from bundle and set title
        String name = getArguments().getString("name", "Enter Name:");
        getDialog().setTitle(name);

        String quantity = getArguments().getString("quantity", "Enter Quantity:");
        getDialog().setTitle(quantity);

        String description = getArguments().getString("description", "Enter Description:");
        getDialog().setTitle(description);
        // Show soft keyboard automatically and request focus to field
        inv_name_txt.requestFocus();
        inv_qty_txt.requestFocus();
        inv_desc_txt.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text back to activity through the implemented listener
            AddInventoryDiaListener listener = (AddInventoryDiaListener) getActivity();
            listener.onFinishEditDialog(inv_name_txt.getText().toString(), inv_qty_txt.getText().toString(), inv_desc_txt.getText().toString());
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }
    public interface AddInventoryDiaListener{
     void onFinishEditDialog(String toString, String toString1, String toString2);
    }
}