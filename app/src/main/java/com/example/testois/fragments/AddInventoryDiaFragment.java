//Reference: https://github.com/mitchtabian/DialogFragmentToActivity

package com.example.testois.fragments;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testois.Inventory;
import com.example.testois.R;
import com.example.testois.ViewInventory;
import com.example.testois.severinaDB;

import java.io.IOException;

public class AddInventoryDiaFragment extends DialogFragment {

    private static final String TAG = "AddInventoryDiaFragment";
    private EditText inv_name_txt, inv_qty_txt, inv_desc_txt;
    private Button btn_add, btn_back, btn_insert, increment, decrement;
    private Bitmap defaultimage; private Drawable default_img;
    private Bitmap imageToStore;
    private ImageView img_item;
    private Uri imgPath;
    private severinaDB db;
    private SQLiteDatabase sql;

    public interface OnInputListener {
        // void sendInput(String name, String qty, String desc, Bitmap image);
        void sendInput(String name, String qty, String desc);
    }
    public OnInputListener fraglisten;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_inventory_dia, container, false);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.FullScreenDialogStyle);
        // Get field from view

        //ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        try{
            inv_name_txt = view.findViewById(R.id.inv_name_txt);
            inv_qty_txt = view.findViewById(R.id.inv_qty_txt);
            inv_desc_txt = view.findViewById(R.id.inv_desc_txt);
            img_item = view.findViewById(R.id.img_item);
            btn_insert = view.findViewById(R.id.insert_img_item);
            btn_add = view.findViewById(R.id.btn_add_inv);
            btn_back = view.findViewById(R.id.btn_back_inv);
            increment = view.findViewById(R.id.increment);
            decrement = view.findViewById(R.id.decrement);


            db=new severinaDB(getContext());
        }catch(Exception e){
            e.printStackTrace();
        }

        increment.setOnClickListener(v -> {
            int quantity = Integer.parseInt(inv_qty_txt.getText().toString());
            quantity++;
            inv_qty_txt.setText(String.valueOf(quantity));
        });

        decrement.setOnClickListener(v -> {
            int quantity = Integer.parseInt(inv_qty_txt.getText().toString());
            quantity--;
            inv_qty_txt.setText(String.valueOf(quantity));
        });

        btn_back.setOnClickListener(v -> {
            Log.d(TAG, "onClick: closing dialog");
            getDialog().dismiss();
        });
        btn_add.setOnClickListener(v ->{
            if(!inv_name_txt.getText().toString().isEmpty() && !inv_qty_txt.getText().toString().isEmpty() && !inv_desc_txt.getText().toString().isEmpty())
            {
                Log.d(TAG, "onClick: capturing input");
                String name = inv_name_txt.getText().toString();
                String qty = inv_qty_txt.getText().toString();
                String desc = inv_desc_txt.getText().toString();
                fraglisten.sendInput(name, qty, desc);
                getDialog().dismiss();
                getActivity().recreate();
            }
            else if(inv_name_txt.getText().toString().isEmpty()){ inv_name_txt.requestFocus(); }
            else if(inv_qty_txt.getText().toString().isEmpty()){ inv_qty_txt.requestFocus(); }
            else if(inv_desc_txt.getText().toString().isEmpty()){inv_desc_txt.requestFocus(); }
            else{
                Toast.makeText(getContext(), "Please fill out all the needed inputs.", Toast.LENGTH_SHORT).show();
            }

            /*
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(i,100);
            if(imageToStore != null){
                String name = inv_name_txt.getText().toString();
                String qty = inv_qty_txt.getText().toString();
                String desc = inv_desc_txt.getText().toString();
                fraglisten.sendInput(name, qty, desc, imageToStore);
                getDialog().dismiss();
                getActivity().recreate();
            }else{
             defaultimage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_default_img);
             */
            });

        //METHOD OF SAANI: https://www.youtube.com/watch?v=OBtEwSe4LEQ
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(i,100);
                    imageToStore = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imgPath);

                } catch (Exception e) { e.printStackTrace(); }
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        try {
            super.onActivityResult(requestcode, resultcode, data);
            if (requestcode == 100 && resultcode == 0 && data != null && data.getData() != null) {
                imgPath = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imgPath);
            }
        }catch(Exception e){ Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show(); }
    }

    public void storeImage(View v){
        db = new severinaDB(getActivity());
        try {
            if(!inv_name_txt.getText().toString().isEmpty() && !inv_qty_txt.getText().toString().isEmpty() && !inv_desc_txt.getText().toString().isEmpty() && imageToStore!= null){
                db.storeImage(new Inventory(inv_name_txt.getText().toString(), inv_qty_txt.getText().toString(), inv_desc_txt.getText().toString(), imageToStore));
            }else{
                db.storeImage(new Inventory(inv_name_txt.getText().toString(), inv_qty_txt.getText().toString(), inv_desc_txt.getText().toString(), defaultimage));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
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