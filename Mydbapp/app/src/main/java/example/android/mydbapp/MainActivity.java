package example.android.mydbapp;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private Spinner spnId;
    private TextView txvId;
    private EditText etxName, etxPrice;
    private Button btnInsert, btnUpdate, btnDelete;

    private List<DtoItem> listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //レイアウトからビューを取得
        spnId = (Spinner) findViewById(R.id.amin_spnId);
        txvId = (TextView) findViewById(R.id.amin_txvId);
        etxName = (EditText) findViewById(R.id.amin_etxName);
        etxPrice = (EditText) findViewById(R.id.amin_etxPrice);
        btnInsert = (Button) findViewById(R.id.amin_btnInsert);
        btnUpdate = (Button) findViewById(R.id.amin_btnUpdate);
        btnDelete = (Button) findViewById(R.id.amin_btnDelete);

        //全レコード取得
        listItem = DaoItem.findAll(getApplicationContext());

        //取得したデータをSpinnerに設定
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getBaseContext(),
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.
                simple_spinner_dropdown_item);
        adapter.add(getText(R.string.amin_btnInsert).toString());
        for (DtoItem item : listItem) {
            adapter.add(String.valueOf(item.id));
        }
        spnId.setAdapter(adapter);

        //Spinnerの選択
        spnId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position == 0) {
                    //ボタンの有効・無効化
                    setButtonEnabled(true);

                    //画面更新
                    initData();
                }
                //レコードを選択した場合
                else {
                    //ボタンの有効・無効化
                    setButtonEnabled(false);

                    //画面更新(選択したレコードを表示)
                    txvId.setText(String.valueOf(listItem.get(position - 1).id));
                    etxName.setText(listItem.get(position - 1).name);
                    etxPrice.setText(String.valueOf(listItem.get(position - 1).price));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //新規登録
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //画面からレコード情報を取得してDTOにセット
                DtoItem item = new DtoItem();
                item.name = etxName.getText().toString();
                if (etxPrice.getText().toString().length() == 0) {
                    item.price = 0;
                } else {
                    item.price = Long.valueOf(etxPrice.getText().toString());
                }

                //レコード追加
                item.id = DaoItem.insert(getApplicationContext(), item);
                //画面更新
                initData();
                listItem.add(item);
                updateSpinner();
                Log.d("Log", "item.id=" + item.id);
            }
        });

        //更新
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //画面からレコード情報を取得してDTOにセット
                DtoItem item = new DtoItem();
                item.id = Long.valueOf(txvId.getText().toString());
                item.name = etxName.getText().toString();
                item.price = Long.valueOf(etxPrice.getText().toString());

                //レコード更新
                DaoItem.update(getApplicationContext(), item);
                Log.d("Log", "118行目までは来た");
                //画面更新
                /*listItem.set(spnId.getSelectedItemPosition() - 1, item);
                spnId.setSelection(0, true);
                */
            }
        });

        //削除
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //レコード削除
                DaoItem.delete(getApplicationContext(),
                        Long.valueOf(txvId.getText().toString()));

                //画面更新
                listItem.remove(spnId.getSelectedItemPosition() - 1);
                updateSpinner();
                spnId.setSelection(0, true);
            }
        });
    }

    //ボタンの有効・無効化
    private void setButtonEnabled(boolean isInsert){
        btnInsert.setEnabled(isInsert);
        btnUpdate.setEnabled(!isInsert);
        btnDelete.setEnabled(!isInsert);
    }

    //Spinnerの更新
    private void updateSpinner(){
        ArrayAdapter<String> adapter = (ArrayAdapter<String>)spnId.getAdapter();
        adapter.clear();
        adapter.add(getText(R.string.amin_btnInsert).toString());
        for(DtoItem item : listItem){
            adapter.add(String.valueOf(item.id));
        }
    }

    //表示データの初期化
    private void initData(){
        txvId.setText("");
        etxName.setText("");
        etxPrice.setText("0");
    }
}
