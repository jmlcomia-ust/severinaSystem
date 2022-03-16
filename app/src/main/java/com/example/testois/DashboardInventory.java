package com.example.testois;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testois.adapter.CustomAdapterInv;
import com.example.testois.fragments.AddInventoryDiaFragment;

import java.util.Collections;
import java.util.List;

public class DashboardInventory extends AppCompatActivity implements Filterable, AddInventoryDiaFragment.OnInputListener {
    private static final String TAG = "DashboardInventory";
    private Toolbar toolbar;
    TextView emptyfield;
    severinaDB db;
    RecyclerView recyclerView;
    SearchView searchview;
    ImageView imageview;
    Button add_btn;
    String frag_name, frag_qty, frag_desc; byte[] frag_image;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.nav_search);
        SearchView searchView =
                (SearchView) searchItem.getActionView();

        getMenuInflater().inflate(R.menu.main_drawer_menu, menu);

        // Configure the search info and add any event listeners...

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CustomAdapterInv customAdapterInv;
        List<Inventory> items = db.getitemsList();
        switch (item.getItemId()) {
            case R.id.sort_ascending:
                // User chose the "Settings" item, show the app settings UI...
                Collections.sort(items, (Inventory o1, Inventory o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
                customAdapterInv = new CustomAdapterInv(items, DashboardInventory.this);
                recyclerView.setAdapter(customAdapterInv);
                recyclerView.setLayoutManager(new LinearLayoutManager(DashboardInventory.this));
                EditText tool_search = findViewById(R.id.search_inv);
                recyclerView.getAdapter().notifyDataSetChanged();
                return true;

            case R.id.sort_descending:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Collections.sort(items, (Inventory o1, Inventory o2) -> o1.getQuantity().compareToIgnoreCase(o2.getQuantity()));
                Collections.reverse(items);
                customAdapterInv = new CustomAdapterInv(items, DashboardInventory.this);
                recyclerView.setAdapter(customAdapterInv);
                recyclerView.setLayoutManager(new LinearLayoutManager(DashboardInventory.this));
                recyclerView.getAdapter().notifyDataSetChanged();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_inventory);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.inflateMenu(R.menu.main_menu);

        db = new severinaDB(this);
        List<Inventory> items = db.getitemsList();

        emptyfield = findViewById(R.id.emptyRv);
        add_btn = findViewById(R.id.add_item);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: opening Add Dialog Fragment for Inventory.");
                AddInventoryDiaFragment dialog = new AddInventoryDiaFragment();
                dialog.show(getSupportFragmentManager(), "AddInventoryDiaFragment");
            }
        });
        //searchview = findViewById(R.id.settings);
        imageview = findViewById(R.id.inv_img);
        recyclerView = findViewById(R.id.recyclerView);

        CustomAdapterInv customAdapterInv = new CustomAdapterInv(items, this);
        recyclerView.setAdapter(customAdapterInv);
        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardInventory.this));
        EditText tool_search = findViewById(R.id.search_inv);

        //WORKING SORT BUTTON
         tool_search.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Collections.sort(items, (Inventory o1, Inventory o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
                       CustomAdapterInv customAdapterInv = new CustomAdapterInv(items, DashboardInventory.this);
                       recyclerView.setAdapter(customAdapterInv);
                       recyclerView.setLayoutManager(new LinearLayoutManager(DashboardInventory.this));
                       EditText tool_search = findViewById(R.id.search_inv);
                       recyclerView.getAdapter().notifyDataSetChanged();
                   }
               });

         //*/

        // attach setOnQueryTextListener
        // to search view defined above
        //searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {}

    }

    @Override
    public Filter getFilter() {
        return null;
    }
    //public void sendInput(String name, String qty, String desc, byte[] bytesImage) {
        public void sendInput(String name, String qty, String desc) {
        //Log.d(TAG, "sendInput: got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc + "\ngot image:" + bytesImage);
            Log.d(TAG, "sendInput: got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc);
        frag_name = name;
        frag_qty = qty;
        frag_desc = desc;
       // frag_image = bytesImage;
        setInputToListView();
    }
    private void setInputToListView() {
        try {
            db = new severinaDB(DashboardInventory.this);
            //Inventory inventory = new Inventory(frag_name,frag_qty, frag_desc, frag_image);
            Inventory inventory = new Inventory(frag_name,frag_qty, frag_desc);
            db.addItem(inventory);
            Toast.makeText(this, "Item Added Successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
        }
    }
}