package tekkraft.df;

import android.app.Activity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.graphics.Color;
import android.graphics.Typeface;

public class HttpclientActivity extends Activity {

	public LinearLayout page1 = null;
	public ScrollView page2 = null;
	public int currentView = 0;
	public Typeface tf = null;
	

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.tf = Typeface.createFromAsset(getAssets(), "fonts/Poly-Regular.ttf");
        uiPage1();

        setContentView(this.page1);
        currentView = 1;
    }
    
    @Override
    public void onBackPressed() {
    	if(currentView == 1) {
    		this.finish();
    	} else {
	        uiPage1();
	        setContentView(this.page1);
	        currentView = 1;
	        this.page2 = null;
    	}
	    return;
    }

    public void uiPage1() {
    	this.page1 = new LinearLayout(this);
    	this.page1.setOrientation(LinearLayout.VERTICAL);
    	this.page1.setBackgroundColor(Color.WHITE);
    	final TextView lblTerm = new TextView(this);
    	lblTerm.setText("Type the word or phrase to define");
    	lblTerm.setId(1);
    	lblTerm.setGravity(Gravity.CENTER_HORIZONTAL);
    	lblTerm.setTextColor(Color.argb(0xFF, 0x00, 0x80, 0x80));
    	lblTerm.setTypeface(tf);
    	lblTerm.setTextSize(22);
    	this.page1.addView(lblTerm);
    	
    	final EditText editTerm = new EditText(this);
    	editTerm.setId(2);
    	editTerm.setSingleLine();
    	editTerm.setTextColor(Color.argb(0xFF, 0x00, 0x80, 0x80));
    	editTerm.setTypeface(tf);
    	editTerm.setTextSize(22);
    	this.page1.addView(editTerm);
    	
    	final Button btnDefine = new Button(this);
    	btnDefine.setId(3);
    	btnDefine.setText("Define!");
    	btnDefine.setTextColor(Color.argb(0xFF, 0x00, 0x80, 0x80));
    	btnDefine.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                getRequest(editTerm);
            }
        });
    	btnDefine.setTypeface(tf);
    	btnDefine.setTextSize(22);
    	this.page1.addView(btnDefine);
    	
    	final TextView lblNotification = new TextView(this);
    	lblNotification.setId(4);
    	lblNotification.setTextColor(Color.argb(0xFF, 0xAA, 0x00, 0x00));
    	lblNotification.setPadding(5, 0, 0, 0);
    	lblNotification.setTypeface(tf);
    	lblNotification.setTextSize(22);
    	this.page1.addView(lblNotification);

    	final RelativeLayout rl = new RelativeLayout(this);
    	rl.setBackgroundColor(Color.WHITE);
    	rl.setId(5);
    	RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    	rl.setLayoutParams(lp);
    	this.page1.addView(rl);
    	
    	final TextView lblAbout = new TextView(this);
    	lblAbout.setId(6);
    	lblAbout.setTextColor(Color.argb(0xFF, 0x00, 0x80, 0x80));
    	//lblAbout.setPadding(5, 0, 0, 0);
    	lblAbout.setTypeface(tf);
    	lblAbout.setTextSize(16);
    	lblAbout.setText("DefinePlug is free, open source.\n");
    	lblAbout.append("http://code.google.com/p/defineplug-android \n");
    	lblAbout.append("Powered by STANDS4 API\n");
    	lblAbout.append("Send feedback to defineplug@gmail.com");
    	lblAbout.setAutoLinkMask(0);
    	Linkify.addLinks(lblAbout, Linkify.ALL);
    	lblAbout.setGravity(Gravity.CENTER_HORIZONTAL);
    	RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, lblAbout.getId());
    	//this.page1.addView(lblAbout);
    	rl.addView(lblAbout, lp2);

    	/*
    	Typeface tf1 = Typeface.createFromAsset(getAssets(), "fonts/GenBkBasR.ttf");
    	//Typeface tf2 = Typeface.createFromAsset(getAssets(), "fonts/Poly-Regular.ttf");
    	Typeface tf3 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Thin.ttf");

    	final TextView lbl1 = new TextView(this);
    	lbl1.setTypeface(tf1);
    	lbl1.setTextSize(24);
    	lbl1.setTextColor(Color.argb(0xFF, 0x00, 0x80, 0x80));
    	lbl1.setText("DefinePlug - Bounty Hunter");
    	this.page1.addView(lbl1);

    	final TextView lbl2 = new TextView(this);
    	lbl2.setTypeface(tf);
    	lbl2.setTextSize(24);
    	lbl2.setTextColor(Color.argb(0xFF, 0x00, 0x80, 0x80));
    	lbl2.setText("DefinePlug - Bounty Hunter");
    	this.page1.addView(lbl2);

    	final TextView lbl3 = new TextView(this);
    	lbl3.setTypeface(tf3, Typeface.BOLD);
    	lbl3.setTextSize(24);
    	lbl3.setTextColor(Color.argb(0xFF, 0x00, 0x80, 0x80));
    	lbl3.setText("DefinePlug - Bounty Hunter");
    	this.page1.addView(lbl3);
    	*/
    }

	public void uiPage2() {
    	this.page2 = new ScrollView(this);
    	this.page2.setBackgroundColor(Color.WHITE);
	}

    public void getRequest(EditText txtUrl){
        String term = txtUrl.getText().toString();
        if(term.isEmpty()) {
        	TextView lblNotification = (TextView)findViewById(4);
        	lblNotification.setText("Please type a word or phrase first, and then tap the Define button.");
        } else {
        	new GetDefinitionTask().execute(this, term);
        }
    }
}