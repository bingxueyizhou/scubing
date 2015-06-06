package com.scufy.scubing.app;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

import com.scufy.scubing.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CMDTools extends Activity{
	//control
	private TextView main_content;
	private EditText main_input;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mapp_cmd);
		
		initialize();
		
		setListener();
	}
	
	private void initialize(){
		main_content = (TextView)findViewById(R.id.cmd_tV_content);
		main_input   = (EditText)findViewById(R.id.cmd_eT_input);
		
		reValueText(main_content.getText().toString());
	}
	
	private void reValueText(String s){
		SpannableString msp = new SpannableString(s);
		Log.i("cmd_info",s);
		msp.setSpan(new TypefaceSpan("msyh.TTF"), 0, s.length(), 
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		main_content.setText(msp);
	}

	private void setListener() {
		main_input.setOnKeyListener(OnEnter);
	}
	
	/**
	 * exc linux shell
	 * @param c
	 * @return
	 */
	private String exec(String c){
		try {  
            String[] cmdA = { "/bin/sh", "-c", c};  
            Process process = Runtime.getRuntime().exec(cmdA);
            process.waitFor();
            LineNumberReader br = new LineNumberReader(new InputStreamReader(  
                    process.getInputStream()));  
            StringBuffer sb = new StringBuffer();  
            String line;  
            while ((line = br.readLine()) != null) {  
                System.out.println(line);  
                sb.append(line).append("\n");  
            }  
            return sb.toString();  
        } catch (Exception e) {  
            e.printStackTrace();
            return e.toString();
        }
	}
	
	private OnKeyListener OnEnter = new OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			
			if(keyCode == KeyEvent.KEYCODE_ENTER){  
				InputMethodManager imm = (InputMethodManager)
						v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
				  
				if(imm.isActive()){  
					imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );  
				}
				Toast.makeText(CMDTools.this, main_input.getText().toString(),
						Toast.LENGTH_SHORT).show();
				
				reValueText(exec(main_input.getText().toString()));
				return true;
			} 
			return false;
		}
	};
}

/**
 lv = (ListView)findViewById(R.id.lV_allTools);
		
String[] str_tools_list = new String[]{"�豸��Ϣ","ping����"};
//String[] str=new String[3];
ArrayAdapter<String> lv_adapter = new ArrayAdapter<String>(this,  
        android.R.layout.simple_list_item_1, str_tools_list);
lv.setAdapter(lv_adapter);

lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	/*
	arg1�ǵ�ǰitem��view��ͨ�������Ի�ø����еĸ��������
	����arg1.textview.settext("asd");
	arg2�ǵ�ǰitem��ID�����id���������������е�д�������Լ����塣
	arg3�ǵ�ǰ��item��listView�е����λ�ã�
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		
	}
});
 */
