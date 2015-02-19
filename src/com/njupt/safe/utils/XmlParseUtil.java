package com.njupt.safe.utils;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.njupt.safe.bean.UpdataBean;

public class XmlParseUtil {

	public static UpdataBean getUpdataInfo(InputStream inputStream) {
		XmlPullParser parser = Xml.newPullParser();
		UpdataBean bean = new UpdataBean();
		
		try {
			parser.setInput(inputStream,"UTF-8");
			int type = parser.getEventType();
			while(type != XmlPullParser.END_DOCUMENT){
				switch (type) {
				case XmlPullParser.START_TAG:
					if("version".equals(parser.getName())){
						bean.setVersion(parser.nextText());
					}else if("description".equals(parser.getName())){
						bean.setDes(parser.nextText());
					}else if("apkurl".equals(parser.getName())){
						bean.setApkurl(parser.nextText());
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
