package com.example.WhoISscanner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.net.IDN;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        EditText edittext = (EditText)findViewById(R.id.site);
        edittext.setImeOptions(EditorInfo.IME_ACTION_DONE);

        Button button = (Button)findViewById(R.id.getData);
        button.setOnClickListener(myListener);
    }

    private ProgressDialog pd;
    private View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText editText = (EditText)findViewById(R.id.site);
            if (editText.getText().toString().length() != 0)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                pd = ProgressDialog.show(MainActivity.this, "Searching...", "looking for data");
                new ParseWorker().execute(editText.getText().toString());
            }
            else
            {
                Toast.makeText(MainActivity.this, "Enter site ..", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private class ParseWorker extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>>{

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
            ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
            try {
                ParseHelper ph = new ParseHelper();
                list = ph.getPage(IDN.toASCII(params[0]));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> hashMaps) {
            pd.dismiss();
            ListView listView = (ListView)findViewById(R.id.whoisList);
            SimpleAdapter myAdapter = new SimpleAdapter(MainActivity.this, hashMaps, R.layout.row,
                    new String[] {"param", "value"},
                    new int[] {R.id.whoisParam, R.id.whoisData});

            listView.setAdapter(myAdapter);
        }
    }
}
