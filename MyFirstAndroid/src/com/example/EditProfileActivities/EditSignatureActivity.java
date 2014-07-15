package com.example.EditProfileActivities;

import com.example.myfirstandroid.R;

import com.example.entity.PersonModel;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class EditSignatureActivity extends Activity {
	private EditText editSignatureText;
	private TextView signatureHintTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_edit_signature);
		findViews();
		setIntents();
		setListeners();
	}
	@Override
	protected void onResume() {
		super.onResume();
		setInfo();
	}
	private void findViews(){
		editSignatureText = (EditText)findViewById(R.id.editSignatureText);
		signatureHintTextView = (TextView)findViewById(R.id.editNameHintText);
	}
	private void setIntents(){
		
	}
	private void setListeners(){
		editSignatureText.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			int selectionStart;
			int selectionEnd;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				temp = s;
				
			}
			@Override
			public void afterTextChanged(Editable s) {
				int lengthLeft = PersonModel.SIGNATURE_MAX_LENGTH - s.length();
				signatureHintTextView.setText("»¹Ê£Óà"+lengthLeft+"×Ö...");
				selectionStart = editSignatureText.getSelectionStart();
				selectionEnd = editSignatureText.getSelectionEnd();
				if(temp.length()>PersonModel.SIGNATURE_MAX_LENGTH){
					s.delete(selectionStart-1, selectionEnd);
					selectionStart = selectionEnd;
					editSignatureText.setText(s);
					editSignatureText.setSelection(selectionStart);
				}
				PersonModel.signature = s.toString();
			}
		});
	}
	private void setInfo(){
		editSignatureText.setText(PersonModel.signature);
	}

}
