package com.androidclass.water;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {
	private static Button button_sbm; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onClickButtonListener();
        onClickButtonListener1();
        onClickButtonListener2();
        onClickButtonListener3();
        
        
    }

    public void onClickButtonListener(){
    	button_sbm=(Button)findViewById(R.id.button1);
    	button_sbm.setOnClickListener(
    			new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent("com.androidclass.water.WMSS_UA");
						startActivity(intent);
					}
				}
    			);
    }
    
    public void onClickButtonListener1(){
    	button_sbm=(Button)findViewById(R.id.button2);
    	button_sbm.setOnClickListener(
    			new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent("com.androidclass.water.WMSS_UOW");
						startActivity(intent);
					}
				}
    			);
    }

    public void onClickButtonListener2(){
    	button_sbm=(Button)findViewById(R.id.button3);
    	button_sbm.setOnClickListener(
    			new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent("com.androidclass.water.WMSS_UA");
						startActivity(intent);
					}
				}
    			);
    }

 

    public void onClickButtonListener3(){
    	button_sbm=(Button)findViewById(R.id.button4);
    	button_sbm.setOnClickListener(
    			new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent("com.androidclass.water.WMSS_NSS");
						startActivity(intent);
					}
				}
    			);
    }
	
	private Button findViewById(OnClickListener onClickListener) {
	// TODO Auto-generated method stub
	return null;
}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
