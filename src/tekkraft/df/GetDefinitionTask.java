package tekkraft.df;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GetDefinitionTask extends AsyncTask<Object, Void, String> {

	private HttpclientActivity mContext = null;
	private String query_url = "http://www.abbreviations.com/services/v1/defs.aspx?tokenid=tk1678&word=";
	private String url = null;
	private String term = null;

	@Override
	protected String doInBackground(Object... arg0) {
		mContext = (HttpclientActivity)arg0[0];
		term = (String)arg0[1];
		String query;
		try {
			query = URLEncoder.encode(term, "utf-8");
		} catch (UnsupportedEncodingException e) {
			return "Failed!" + e.toString();
		}
		url = query_url + query;
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		try{
			HttpResponse response = client.execute(request);
			String result = HttpHelper.request(response);
			return result;
		} catch (Exception ex) {
			return "Failed!" + ex.toString();
		}
	}

	@Override
	protected void onPostExecute(String result) {
		try {
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document doc = db.parse(new InputSource(new StringReader(result)));
	        doc.getDocumentElement().normalize();
	
	        NodeList nodeList = doc.getElementsByTagName("result");
	        int resultCount = nodeList.getLength(); 
	        if(resultCount == 0) {
	        	TextView lblNotification = (TextView)mContext.findViewById(4);
	        	lblNotification.setText("Sorry, I could not find a definition for this term.");
	        } else {
	            NodeList termNodeList = doc.getElementsByTagName("term");
	            NodeList defNodeList = doc.getElementsByTagName("definition");
	            
	            //now setup the results page
	            mContext.uiPage2();
	
	            final LinearLayout pane = new LinearLayout(mContext);
	            pane.setOrientation(LinearLayout.VERTICAL);
	            mContext.page2.addView(pane);
	
	        	for(int i = 0; i < resultCount; i++) {
	        		Node tNode = termNodeList.item(i);
	                TextView txtTerm = new TextView(mContext);
	                txtTerm.setText(tNode.getChildNodes().item(0).getNodeValue());
	                txtTerm.setTypeface(Typeface.DEFAULT_BOLD);
	                txtTerm.setTextColor(Color.argb(0xFF, 0, 0x80, 0x80));
	                txtTerm.setTextSize(20);
	                txtTerm.setPadding(5, 0, 0, 0);
	                txtTerm.setTypeface(mContext.tf);
	                pane.addView(txtTerm);
	
	                Node dNode = defNodeList.item(i);
	                TextView txtDef = new TextView(mContext);
	                txtDef.setText(dNode.getChildNodes().item(0).getNodeValue() + "\n");
	                txtDef.setTypeface(Typeface.DEFAULT);
	                txtDef.setTextColor(Color.BLACK);
	                txtDef.setTextSize(18);
	                txtDef.setPadding(10, 0, 0, 0);
	                txtDef.setTypeface(mContext.tf);
	                pane.addView(txtDef);
	
	        		/*
	        		Node eNode = exNodeList.item(i);
	        		resultText.append(eNode.getChildNodes().item(0).getNodeValue() + "\n");
	        		Node pNode = posNodeList.item(i);
	        		resultText.append(pNode.getChildNodes().item(0).getNodeValue() + "\n");
					*/
	        	}
	        	mContext.setContentView(mContext.page2);
	            mContext.currentView = 2;
	        }
		} catch (Exception exp) {
        	TextView lblNotification = (TextView)mContext.findViewById(4);
        	lblNotification.setText("Failed: " + exp.toString());
		}

	}
}
