package com.example.th_lab7;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<User> users;
    ArrayList<User> listUser = new ArrayList<User>();
    ListView lv;
    User user_all;
    EditText name;
    int idU;
    Button btnAdd;
    Button btnRemove;
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_name);
        lv = (ListView) findViewById(R.id.lv_name);
        name = (EditText) findViewById(R.id.edit_name);

        AppDatabase db =
                Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "TH-Lab7_1")
                        .allowMainThreadQueries()
                        .build();

        UserDao userDao = db.userDao();
        User user = new User();

        userDao.insertUser(user);

        users = userDao.getAll();
        listUser = (ArrayList<User>) users;

        ArrayAdapter<User> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listUser);
        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                idU = listUser.get(i).uid;
                int a[] = {idU};
                user_all = userDao.loadAllByIds(a).get(0);
                name.setText(user_all.toString());
            }
        });

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                if (userName.equals("")){
                    Toast.makeText(MainActivity.this,"Nhập tên cần thêm",Toast.LENGTH_SHORT).show();
                }
                else {
                    User u = new User();
                    u.firstName = userName;
                    u.lastName = userName;
                    userDao.insertUser(u);

                    listUser.clear();

                    users = userDao.getAll();
                    listUser.addAll(users);
                    name.setText("");
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });

        btnRemove = (Button) findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_all == null){
                    Toast.makeText(MainActivity.this,"Chọn tên muốn xóa",Toast.LENGTH_SHORT).show();
                }
                else {
                    userDao.delete(user_all);

                    listUser.clear();

                    users = userDao.getAll();
                    listUser.addAll(users);
                    name.setText("");
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("");
                user_all = null;
            }

        });


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
}