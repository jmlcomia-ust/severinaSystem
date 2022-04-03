package com.example.testois.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Environment;
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
import com.example.testois.adapter.CustomViewAdapInv;
import com.example.testois.severinaDB;

import java.util.List;


public class UpdateInventoryDiaFragment extends DialogFragment {
    private static final String TAG = "UpdateInvFrag";
    public UpdateInventoryDiaFragment.OnInputListener fragupdate;
    CustomViewAdapInv customViewAdapInv;
    private EditText inv_id_txt, inv_name_txt, inv_qty_txt, inv_desc_txt, inv_thres_txt;
    private ImageView inv_imgu;
   // private Bitmap imageToStore;
   // private Uri selectedImageUri;
    int id;
    String name_updated, desc_updated;int qty_updated, thres_updated; byte[] img_updated;
    private Button btn_update, btn_back, increment, decrement, btn_insert;
    private SQLiteDatabase sql;

    public interface OnInputListener {
        //void UpdateInput(String name, String qty, String desc, int thres, Bitmap image);
        void UpdateInput(int id, String name, int qty, String desc, int thres);

    }
/*
    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        try {
            super.onActivityResult(requestcode, resultcode, data);
            if (resultcode == RESULT_OK) {
                if (requestcode == 100) {
                    selectedImageUri = data.getData();
                    if (null != selectedImageUri) {
                        //imageToStore = severinaDB.decodeUri(getActivity(), selectedImageUri, 400);  //application of image resizing for fast upload to DB
                        inv_imgu.setImageBitmap(imageToStore);
                    }
                }
            }
        }catch(Exception e){ Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show(); }
    }


    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
    }
 */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_inv, container, false);
        // Get field from view
        inv_id_txt = view.findViewById(R.id.inv_id_txtu);
        inv_name_txt = view.findViewById(R.id.inv_name_txtu);
        inv_imgu = view.findViewById(R.id.inv_imgu);
        inv_qty_txt = view.findViewById(R.id.inv_qty_txtu);
        inv_desc_txt = view.findViewById(R.id.inv_desc_txtu);
        inv_thres_txt = view.findViewById(R.id.inv_thres_txtu);
        btn_update = view.findViewById(R.id.btn_update);
        btn_insert = view.findViewById(R.id.insert_photo);
        btn_back = view.findViewById(R.id.btn_back);
        increment = view.findViewById(R.id.increment);
        decrement = view.findViewById(R.id.decrement);

        Bundle args = getArguments();
            inv_id_txt.setText(String.valueOf(args.getInt("ids")));
            inv_name_txt.setText(args.getString("name"));
            inv_qty_txt.setText(String.valueOf(args.getInt("qty")));
            inv_desc_txt.setText(args.getString("desc"));
            inv_thres_txt.setText(String.valueOf(args.getInt("thres")));
            //inv_imgu.setImageBitmap(severinaDB.getImage(args.getByteArray("image")));


        // FUNCTION TO SET EXISTING DATA TO EDITTEXTS> setData();

        increment.setOnClickListener(v -> {
            int quantity = Integer.parseInt(inv_qty_txt.getText().toString().trim());
            quantity++;
            inv_qty_txt.setText(String.valueOf(quantity));

        });
        decrement.setOnClickListener(v -> {
            int quantity = Integer.parseInt(inv_qty_txt.getText().toString().trim());
            quantity--;
            inv_qty_txt.setText(String.valueOf(quantity));
        });
        btn_back.setOnClickListener(v -> {
            Log.d(TAG, "onClick: closing dialog");
            getDialog().dismiss();
        });
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
         */

        btn_update.setOnClickListener(v ->{
            Log.d(TAG, "onClick: capturing updates");

            id = Integer.parseInt(inv_id_txt.getText().toString().trim());
            name_updated = inv_name_txt.getText().toString().toUpperCase().trim();
            qty_updated = Integer.parseInt(inv_qty_txt.getText().toString().trim());
            desc_updated = inv_desc_txt.getText().toString().toUpperCase().trim();
            thres_updated = Integer.parseInt(inv_thres_txt.getText().toString().trim());
            //imageToStore = severinaDB.decodeUri(getActivity(), selectedImageUri, 400);

            //putBitmap to imageview
           // inv_imgu.setImageBitmap(imageToStore);

            //img_updated = severinaDB.getImageBytes(imageToStore);

            //inv_imgu.getDrawingCache();
            //fragupdate.UpdateInput(name_updated, qty_updated, desc_updated,  thres_updated, imageToStore);
            fragupdate.UpdateInput(id,name_updated, qty_updated, desc_updated, thres_updated);

            getDialog().dismiss();
            getActivity().recreate();
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            fragupdate = (UpdateInventoryDiaFragment.OnInputListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }
  //  public void refreshAdapter(){
      //  adapter.notifyDataSetChanged();
    //}
}
