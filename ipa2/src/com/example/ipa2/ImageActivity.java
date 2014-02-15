package com.example.ipa2;





import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ImageActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.ipa2.MESSAGE";
	public final static String EXTRA_MESSAGE2 = "com.example.ipa2.MESSAGE2";
	Intent intent;
	boolean graysc = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		
		//RadioButton rb0 = (RadioButton) findViewById(R.id.radio0);
		//RadioButton rb1 = (RadioButton) findViewById(R.id.radio1);
	}
	
	public void updateval(View v) {
		int index = Integer.parseInt(""+v.getTag());
		intent = new Intent(this, MainActivity.class);
		intent.putExtra(EXTRA_MESSAGE, index);
		intent.putExtra(EXTRA_MESSAGE2, graysc);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	      case R.id.grayscale:
	    	  graysc = true;
	    	  return true;
	      case R.id.blur:
	    	  graysc = false;
	    	  return true;
	      default:
	            return super.onOptionsItemSelected(item);
	      
	    
	    }
	}

}
