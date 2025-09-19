package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {

    public interface CityDialogListener {
        void addCity(City city);
        void editCity(int position, City city);
    }

    private static final String ARG_NAME = "arg_name";
    private static final String ARG_PROVINCE = "arg_province";
    private static final String ARG_POSITION = "arg_position";

    private CityDialogListener listener;
    private int position = -1; // -1 => add mode

    // Use for edit:
    public static AddCityFragment newInstance(String name, String province, int position) {
        AddCityFragment frag = new AddCityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_PROVINCE, province);
        args.putInt(ARG_POSITION, position);
        frag.setArguments(args);
        return frag;
    }

    // Use for add: just new AddCityFragment()

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CityDialogListener) {
            listener = (CityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement CityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        // If arguments exist, we are in edit mode — prefill fields
        if (getArguments() != null && getArguments().containsKey(ARG_NAME)) {
            String name = getArguments().getString(ARG_NAME);
            String province = getArguments().getString(ARG_PROVINCE);
            position = getArguments().getInt(ARG_POSITION, -1);
            editCityName.setText(name);
            editProvinceName.setText(province);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(position == -1 ? "Add a city" : "Edit city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton(position == -1 ? "Add" : "Save", (dialog, which) -> {
                    String cityName = editCityName.getText().toString().trim();
                    String provinceName = editProvinceName.getText().toString().trim();

                    if (cityName.isEmpty() || provinceName.isEmpty()) {
                        // simple guard — could show a Toast or validation UI
                        Toast.makeText(getContext(), "Enter both name and province", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (position == -1) {
                        // Add
                        listener.addCity(new City(cityName, provinceName));
                    } else {
                        // Edit → create a NEW City and pass it back to Activity
                        listener.editCity(position, new City(cityName, provinceName));
                    }
                })
                .create();
    }
}
