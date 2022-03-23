//Reference: https://github.com/mitchtabian/DialogFragmentToActivity

package com.example.testois.fragments;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
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

import com.example.testois.R;
import com.example.testois.ViewInventory;
import com.example.testois.severinaDB;

import java.io.IOException;

public class AddInventoryDiaFragment extends DialogFragment {

    private static final String TAG = "AddInventoryDiaFragment";
    private EditText inv_name_txt, inv_qty_txt, inv_desc_txt;
    private Button btn_add, btn_back, btn_insert, increment, decrement;
    private Bitmap image;

    private Bitmap imageToStore;
    private ImageView img_item;
    private Uri imgPath;
    private severinaDB sev;
    private SQLiteDatabase db;

    public interface OnInputListener {
       void sendInput(String name, String qty, String desc, Bitmap image);
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

            sev=new severinaDB(getContext());
        }catch(Exception e){
            e.printStackTrace();
        }


        /*
        btn_insert.setOnClickListener(v -> {
            String stringQuery = "Select from ImageTable where Name=\"MyImage\"";
            Cursor cursor = db.rawQuery(stringQuery, null);
            try {
                cursor.moveToFirst();
                byte[] bytesImage = cursor.getBlob(0);
                cursor.close();
                Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytesImage, 0, bytesImage.length);
                img_item.setImageBitmap(bitmapImage);
               Log.e(TAG, "Image Set");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });

         */

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
            Log.d(TAG, "onClick: capturing input");

            //METHOD OF PROGRAMMERS WORLD: https://www.youtube.com/watch?v=-MhB-Frk0ag
            //String stringFilePath = Environment.getExternalStorageDirectory().getPath()+"/Download/"+inv_name_txt.getText().toString()+".jpeg";
            //Bitmap bitmap = BitmapFactory.decodeFile(stringFilePath);
           // ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);

            String name = inv_name_txt.getText().toString();
            String qty = inv_qty_txt.getText().toString();
            String desc = inv_desc_txt.getText().toString();
            //byte[] bytesImage = byteArrayOutputStream.toByteArray();
            //fraglisten.sendInput(name, qty, desc, bytesImage);
            fraglisten.sendInput(name, qty, desc, imageToStore);
            getDialog().dismiss();
            getActivity().recreate();

        });

        //METHOD OF SAANI: https://www.youtube.com/watch?v=OBtEwSe4LEQ
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                     startActivityForResult(i,100);

                }catch(Exception e){
                    e.printStackTrace();
                }

                try {
                    Intent data = null;
                    imgPath=data.getData();
                    imageToStore = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imgPath);
                } catch (Exception e) {
                   e.printStackTrace();
                }
            }
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