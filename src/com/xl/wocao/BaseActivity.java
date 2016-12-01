package com.xl.wocao;
import android.support.v7.app.*;
import android.os.*;
import android.view.*;
/*
activity基础类
加入强制检查更新


*/

public class BaseActivity extends AppCompatActivity implements OnUpdateListener
{

		@Override
		public void onCheckOk()
		{
				
		}

		@Override
		public void onCheckError()
		{
				// TODO: Implement this method
		}
		
		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
				// TODO: Implement this method

				supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
				// TODO: Implement this method
				super.onCreate(savedInstanceState);
				Update.setCheckListener(this);
				Update.check(this);
				
				
		}
		
    
		
		
		
		
}
