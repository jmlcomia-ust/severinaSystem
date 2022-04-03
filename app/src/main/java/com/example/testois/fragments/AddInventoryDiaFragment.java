//Reference: https://github.com/mitchtabian/DialogFragmentToActivity

package com.example.testois.fragments;

import static android.app.Activity.RESULT_OK;

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
import androidx.appcompat.widget.AppCompatImageView;
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
import java.io.InputStream;

public class AddInventoryDiaFragment extends DialogFragment {

    private static final String TAG = "AddInventoryDiaFragment";
    private Uri selectedImageUri;
    private EditText inv_name_txt, inv_qty_txt, inv_desc_txt, inv_thres_txt;
    private Button btn_add, btn_back, btn_insert, increment, decrement;
    private Bitmap defaultimage; private Drawable default_img;
    private Bitmap imageToStore;
    private AppCompatImageView imgView;
    private severinaDB db;
    private SQLiteDatabase sql;

    public interface OnInputListener {
        // void sendInput(String name, String qty, String desc, int thres, Bitmap image);
        void sendInput(String name, int qty, String desc, int thres);
    }
    public OnInputListener fraglisten;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_inventory_dia, container, false);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        // Get field from view

        //ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        try {
            inv_name_txt = view.findViewById(R.id.inv_name_txt);
            inv_qty_txt = view.findViewById(R.id.inv_qty_txt);
            inv_desc_txt = view.findViewById(R.id.inv_desc_txt);
            inv_thres_txt = view.findViewById(R.id.inv_thres_txt);
            imgView = view.findViewById(R.id.img_item);
            btn_insert = view.findViewById(R.id.insert_img_item);
            btn_add = view.findViewById(R.id.btn_add_inv);
            btn_back = view.findViewById(R.id.btn_back_inv);
            increment = view.findViewById(R.id.increment);
            decrement = view.findViewById(R.id.decrement);


            db = new severinaDB(getContext());
        } catch (Exception e) {
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
        btn_add.setOnClickListener(v -> {
            //imageToStore = severinaDB.decodeUri(getActivity(), selectedImageUri, 400);
            if (!inv_name_txt.getText().toString().isEmpty() && !inv_qty_txt.getText().toString().isEmpty() && !inv_desc_txt.getText().toString().isEmpty() && !inv_thres_txt.getText().toString().isEmpty()) {
                Log.d(TAG, "onClick: capturing input");
                String name = inv_name_txt.getText().toString();
                int qty = Integer.parseInt(inv_qty_txt.getText().toString());
                String desc = inv_desc_txt.getText().toString();
                int thres = Integer.parseInt(inv_thres_txt.getText().toString());
                //byte[] imageInByte = severinaDB.getImageBytes(imageToStore);
                //Bitmap imageInBit = severinaDB.getImage(imageInByte);
                //fraglisten.sendInput(name, qty, desc, thres, imageInBit);
                fraglisten.sendInput(name, qty, desc, thres);
                getDialog().dismiss();
                getActivity().recreate();
            } else if (inv_name_txt.getText().toString().isEmpty()) {
                inv_name_txt.requestFocus();
            } else if (inv_qty_txt.getText().toString().isEmpty()) {
                inv_qty_txt.requestFocus();
            } else if (inv_desc_txt.getText().toString().isEmpty()) {
                inv_desc_txt.requestFocus();
            } else if (inv_thres_txt.getText().toString().isEmpty()) {
                inv_thres_txt.requestFocus();
            } else {
                Toast.makeText(getContext(), "Please fill out all the needed inputs.", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
/*
        //METHOD OF SAANI: https://www.youtube.com/watch?v=OBtEwSe4LEQ
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    openImageChooser();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        try {
            super.onActivityResult(requestcode, resultcode, data);
            if (resultcode == RESULT_OK) {
                if (requestcode == 100) {
                    selectedImageUri = data.getData();
                    if (null != selectedImageUri) {
                        imageToStore = severinaDB.decodeUri(getActivity(), selectedImageUri, 400);  //application of image resizing for fast upload to DB
                        imgView.setImageBitmap(imageToStore);
                        imgView.buildDrawingCache();
                    }
                }
            }
        }catch(Exception e){ Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show(); }
    }

    // Choose an image from Gallery
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);

    }

 */

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