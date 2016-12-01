package com.xl.tool;
import android.support.v7.app.*;
import android.content.*;
import android.app.Dialog;

import com.xl.game.tool.*;
import android.widget.*;
import android.view.*;
import java.util.*;
import com.xl.wocao.*;

public class ProgressDialog extends Dialog
{

		@Override
		public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId)
		{
				// TODO: Implement this method
		}
		
    private TextView text;
		public ProgressDialog(Context context)
		{
				super(context);
				
		}
		
		public static Dialog show(android.content.Context context, java.lang.CharSequence title, java.lang.CharSequence message)
		{
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				LinearLayout layout =  (LinearLayout)ViewTool.getView(context,R.layout.dlg_progress);
        TextView text=(TextView)layout.findViewById(R.id.progress_text);
				builder.setView(layout);
				builder.setTitle(title);
				if(text!=null)
            text.setText(message);
				return builder.show();
		}
    
    
		
		
}
