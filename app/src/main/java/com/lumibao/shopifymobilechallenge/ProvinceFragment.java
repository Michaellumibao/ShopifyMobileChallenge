package com.lumibao.shopifymobilechallenge;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProvinceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProvinceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProvinceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private HashMap<String, Integer> ordersByProvince;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProvinceFragment() {
        // Required empty public constructor
    }

    // New instance receives ordersByProvince from main activity
    public static ProvinceFragment newInstance(HashMap<String, Integer> ordersByProvince) {
        ProvinceFragment fragment = new ProvinceFragment();
        Bundle args = new Bundle();
        args.putSerializable("ordersByProvince", ordersByProvince);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.ordersByProvince = (HashMap<String, Integer>) getArguments().getSerializable("ordersByProvince");
            Log.d("app", "ordersByProvince?");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_province, container, false);
        ListView province_list = view.findViewById(R.id.orders_province_list);

        List<String> orderCountByProvince = new ArrayList<>();
        for (String province: ordersByProvince.keySet()) {
            Log.d("app", province + "province");
            orderCountByProvince.add(Integer.toString(ordersByProvince.get(province)) + " orders in " + province + ".");
        }
        ArrayAdapter<String> province_list_adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                orderCountByProvince);
        province_list.setAdapter(province_list_adapter);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
