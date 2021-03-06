package andy.study.dailyrecord;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import andy.study.dailyrecord.dao.DataManager;
import andy.study.dailyrecord.model.Record;
import andy.study.dailyrecord.util.ActivityAgent;
import andy.study.dailyrecord.util.ConfigLoader;

public class MainActivity extends Activity {

	private Button button;
	
	private DataManager dm;
	
	private LinearLayout mainTextView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityAgent.getInstance().addActivity(this);
        
        dm = new DataManager(this);
        mainTextView = (LinearLayout) this.findViewById(R.id.mainView);
        button = (Button)this.findViewById(R.id.addRecord);
        button.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, AddRecord.class);
				startActivity(intent);
			}
		});
        
        initMainTextView();        
        // 查询已添加项
//        List<Map<String, Object>> recordList = dm.findRecord("SELECT * from " + ConfigLoader.RECORD_TABLE, null);
//        for (Map<String, Object> map : recordList) {
//        	Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
//        	String content = "";
//        	while (iterator.hasNext()) {
//        		Entry<String, Object> next = iterator.next();
//        		content += next.getKey() + "=" + next.getValue() + ",";
//        	}
//        	Log.i(ConfigLoader.TAG, content);
//        }
        
    }

    public void initMainTextView() {
    	for (int i=0;i<10;i++) {
	    	TextView textView = new TextView(this);
	    	textView.setText("what's the fuck...");
	    	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	    	textView.setLayoutParams(layoutParams);
	    	textView.setBackgroundColor(Color.RED);
	    	textView.setTextSize(50);
	    	mainTextView.addView(textView);
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private boolean isExist = false;
	private boolean hasTask = false;
	
	/** 点击两下退出程序 */
	ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
	private Runnable runnabel = new Runnable() {
		public void run() {
			isExist = false;
			hasTask = false;
		}
	};
	
	/** 点击两下退出程序 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i(ConfigLoader.TAG, "OO U want exist now");
			if (!isExist) {
				isExist = true;
				Toast.makeText(getApplicationContext(), "再按一次退出键退出程序哟O(∩_∩)O~", Toast.LENGTH_SHORT).show();
				if(!hasTask) {
					hasTask = true;
					exec.schedule(runnabel, 3000, TimeUnit.MILLISECONDS);
				}				
			} else {
				Log.i(ConfigLoader.TAG, "OO U really want exist now");
				//添加退出代码
				ActivityAgent.getInstance().onTerminate();
			}
		}
		return true;
	}
}
