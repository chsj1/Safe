package com.njupt.safe.engine;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.njupt.safe.bean.UpdataBean;
import com.njupt.safe.bean.UpdateInfo;

import android.util.Xml;


public class UpdateInfoService {

	
	public UpdateInfo getUpdateInfo(String path) throws Exception{
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode() == 200){
			InputStream is = conn.getInputStream();
			return parserUpdateInfo(is);
		}
		return null;
	}

	/**
	 * �������������ϢΪUpdateInfo����
	 * @param is
	 * @return
	 * @throws Exception 
	 */
	private UpdateInfo parserUpdateInfo(InputStream is) throws Exception {
		XmlPullParser parser = Xml.newPullParser();
		UpdateInfo bean = new UpdateInfo();
		
		try {
			parser.setInput(is,"UTF-8");
			int type = parser.getEventType();
			while(type != XmlPullParser.END_DOCUMENT){
				switch (type) {
				case XmlPullParser.START_TAG:
					if("version".equals(parser.getName())){
						bean.setVersion(parser.nextText());
					}else if("description".equals(parser.getName())){
						bean.setDescription(parser.nextText());
					}else if("apkurl".equals(parser.getName())){
						bean.setUrl(parser.nextText());
					}
					break;
				}
				type = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
}
