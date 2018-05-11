package com.lumibao.shopifymobilechallenge;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.lumibao.shopifymobilechallenge.dummy.DummyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
        implements ProvinceFragment.OnFragmentInteractionListener, YearOrdersFragment.OnListFragmentInteractionListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private ViewPagerAdapter mViewPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    // Shopify API
    final String SHOPIFY_URL = "https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";

    List<OrderModel> orders = new ArrayList<>();
    List<String> provinces = new ArrayList<>();
    HashMap<String, Integer> ordersInProvince = new HashMap<>();
    int ordersIn2017;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get JSONObject from Shopify API
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(SHOPIFY_URL, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Initialize JSONArray
                JSONArray ordersJson = null;
                try {
                    //Get all 50 orders from the API
                    ordersJson = response.getJSONArray("orders");
                } catch (JSONException e) {
                }

                //Initialize array of Orders
                for (int i = 0; i < ordersJson.length(); i++) {
                    try {
                        OrderModel order = OrderModel.fromJson((JSONObject) ordersJson.get(i));
                        if (order != null) {
                            orders.add(order);
                            Log.i("app", order.toString());
                        } else {
                            Log.w("AbnormalOrder", "Order id: " + Integer.toString(((JSONObject) ordersJson.get(i)).getInt("id")));
                        }
                    } catch (JSONException e) {

                    }
                }

                Log.d("app", "Success");
                calculateOrdersByProvince();
                calculateOrdersIn2017();

                // Populate Province List
//                ListView province_list = findViewById(R.id.province_list);
//                List<String> orderCountByProvince = new ArrayList<>();
//
//                Log.i("!!!", Integer.toString(ordersInProvince.keySet().size()));
//
//                for (String province: ordersInProvince.keySet()) {
//                    Log.i("!!!", province + "province");
//                    orderCountByProvince.add("Orders in " + province + ": " + Integer.toString(ordersInProvince.get(province)));
//                }
//                ArrayAdapter<String> province_list_adapter = new ArrayAdapter<String>(
//                        MainActivity.this,
//                        android.R.layout.simple_list_item_1,
//                        orderCountByProvince);
//                province_list.setAdapter(province_list_adapter);

                //Set up fragments
                mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

                mViewPagerAdapter.addFragment(ProvinceFragment.newInstance(ordersInProvince), "Orders by Province");
                mViewPagerAdapter.addFragment(new YearOrdersFragment(), "Orders by Year");

                // Set up the ViewPager with the sections adapter.
                mViewPager = (ViewPager) findViewById(R.id.container);
                mViewPager.setAdapter(mViewPagerAdapter);

                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);


                mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });
    }

    private void calculateOrdersIn2017() {

        ordersIn2017 = 0;
        for (OrderModel order : orders) {
            if (order.getYear() == 2017) {
                ordersIn2017++;
            }
        }
        Log.d("app", "Orders in 2017: " + Integer.toString(ordersIn2017));
    }

    private void calculateOrdersByProvince() {
        for (OrderModel order : orders) {
            Log.d("app", "id: " + Integer.toString(order.getOrderNumber()));

            if (!provinceIsPresent(order.getProvince())) {
                provinces.add(order.getProvince());
                ordersInProvince.put(order.getProvince(), 1);
            } else {
                Log.d("app", "INSIDE PRESENT PROVINCE");
                ordersInProvince.replace(order.getProvince(), ordersInProvince.get(order.getProvince()) + 1);
            }
        }

        for (String province : ordersInProvince.keySet()) {
            Log.d("app", province + " : " + Integer.toString(ordersInProvince.get(province)));
        }
    }

    private boolean provinceIsPresent(String province) {
        for (String presentProvince : provinces) {
            Log.d("app", "province: " + province);
            Log.d("app", "presentProvinces: " + presentProvince);

            if (province != null) {
                if (province.equalsIgnoreCase(presentProvince)) {
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
    }
}
