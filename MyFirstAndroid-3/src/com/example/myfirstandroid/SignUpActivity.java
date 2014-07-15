package com.example.myfirstandroid;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

public final class SignUpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_in, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		
			case R.id.action_login:
				Intent intent = new Intent(this, LogInActivity.class);
		    	startActivity(intent);
			
			default:return super.onOptionsItemSelected(item);
		}
	}
	
	public void RadioAdapter(View view){
    	RadioButton radio = (RadioButton)view;
    	radio.setSelected(!radio.isSelected());
    }
}
