package com.example.spaceoneaircraft;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import android.app.ActionBar;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorActivity extends Activity {
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setManufacturingYear();
        }

    };
    private EditText mName, mManufacturer, mManufacturingYear;
    private String name, manufacturer, manufacturingYear;
    private int id;
    private Menu action;
    private Bitmap bitmap;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mName = findViewById(R.id.name);
        mManufacturer = findViewById(R.id.manufacturer);
        mManufacturingYear = findViewById(R.id.manufacturing_year);

        mManufacturingYear.setFocusableInTouchMode(false);
        mManufacturingYear.setFocusable(false);
        mManufacturingYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditorActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        name = intent.getStringExtra("name");
        manufacturer = intent.getStringExtra("manufacturer");
        manufacturingYear = intent.getStringExtra("manufacturing year");

        setDataFromIntentExtra();
    }

    private void setDataFromIntentExtra() {

        if (id != 0) {

            readMode();
            getSupportActionBar().setTitle("Edit " + name);

            mName.setText(name);
            mManufacturer.setText(manufacturer);
            mManufacturingYear.setText(manufacturingYear);

        } else {
            getSupportActionBar().setTitle("Add a Pet");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        action = menu;
        action.findItem(R.id.menu_save).setVisible(false);

        if (id == 0) {

            action.findItem(R.id.menu_edit).setVisible(false);
            action.findItem(R.id.menu_delete).setVisible(false);
            action.findItem(R.id.menu_save).setVisible(true);

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();

                return true;
            case R.id.menu_edit:
                //Edit

                editMode();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mName, InputMethodManager.SHOW_IMPLICIT);

                action.findItem(R.id.menu_edit).setVisible(false);
                action.findItem(R.id.menu_delete).setVisible(false);
                action.findItem(R.id.menu_save).setVisible(true);

                return true;
            case R.id.menu_save:
                //Save

                if (id == 0) {

                    if (TextUtils.isEmpty(mName.getText().toString()) ||
                            TextUtils.isEmpty(mManufacturer.getText().toString()) ||
                            TextUtils.isEmpty(mManufacturingYear.getText().toString())) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                        alertDialog.setMessage("Please complete the field!");
                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {

                        postData("insert");
                        action.findItem(R.id.menu_edit).setVisible(true);
                        action.findItem(R.id.menu_save).setVisible(false);
                        action.findItem(R.id.menu_delete).setVisible(true);

                        readMode();

                    }

                } else {

                    updateData("update", id);
                    action.findItem(R.id.menu_edit).setVisible(true);
                    action.findItem(R.id.menu_save).setVisible(false);
                    action.findItem(R.id.menu_delete).setVisible(true);

                    readMode();
                }

                return true;
            case R.id.menu_delete:

                AlertDialog.Builder dialog = new AlertDialog.Builder(EditorActivity.this);
                dialog.setMessage("Delete this pet?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteData("delete", id);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setManufacturingYear() {
        String myFormat = "dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mManufacturingYear.setText(sdf.format(myCalendar.getTime()));
    }

    private void postData(final String key) {

        readMode();

        String name = mName.getText().toString().trim();
        String manufacturer = mManufacturer.getText().toString().trim();
        String manufacturingYear = mManufacturingYear.getText().toString().trim();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Aircrafts> call = apiInterface.insertAircraft(key, name, manufacturer, manufacturingYear);

        call.enqueue(new Callback<Aircrafts>() {
            @Override
            public void onResponse(Call<Aircrafts> call, Response<Aircrafts> response) {

                Log.i(EditorActivity.class.getSimpleName(), response.toString());

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")) {
                    finish();
                } else {
                    Toast.makeText(EditorActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Aircrafts> call, Throwable t) {
                Toast.makeText(EditorActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }

        });

    }

    private void updateData(final String key, final int id) {

        readMode();

        String name = mName.getText().toString().trim();
        String manufacturer = mManufacturer.getText().toString().trim();
        String manufacturingYear = mManufacturingYear.getText().toString().trim();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Aircrafts> call = apiInterface.updateAircraft(key, id, name, manufacturer, manufacturingYear);

        call.enqueue(new Callback<Aircrafts>() {
            @Override
            public void onResponse(Call<Aircrafts> call, Response<Aircrafts> response) {

                Log.i(EditorActivity.class.getSimpleName(), response.toString());

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")) {
                    Toast.makeText(EditorActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditorActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Aircrafts> call, Throwable t) {
                Toast.makeText(EditorActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteData(final String key, final int id) {

        readMode();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Aircrafts> call = apiInterface.deleteAircraft(key, id);

        call.enqueue(new Callback<Aircrafts>() {
            @Override
            public void onResponse(Call<Aircrafts> call, Response<Aircrafts> response) {


                Log.i(EditorActivity.class.getSimpleName(), response.toString());

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")) {
                    Toast.makeText(EditorActivity.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditorActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Aircrafts> call, Throwable t) {
                Toast.makeText(EditorActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    void readMode() {

        mName.setFocusableInTouchMode(false);
        mManufacturer.setFocusableInTouchMode(false);
        mName.setFocusable(false);
        mManufacturer.setFocusable(false);

        mManufacturingYear.setEnabled(false);
    }

    private void editMode() {

        mName.setFocusableInTouchMode(true);
        mManufacturer.setFocusableInTouchMode(true);

        mManufacturingYear.setEnabled(true);

    }

}
