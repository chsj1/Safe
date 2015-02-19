package com.njupt.safe.receiver;

import com.baidu.location.LocationClient;
import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.SpeechSynthesizerListener;
import com.baidu.speechsynthesizer.publicutility.SpeechError;
import com.njupt.safe.R;
import com.njupt.safe.activity.LostProtectActivity;
import com.njupt.safe.db.BlackNumberDao;
import com.njupt.safe.engine.GPSInfoService;
import com.njupt.safe.utils.MD5;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SmsReciever extends BroadcastReceiver implements
		SpeechSynthesizerListener {
	private SharedPreferences sp;
	private DevicePolicyManager devicePolicyManager;
	private static MediaPlayer mediaPlayer;
	private BlackNumberDao blackNumberDao;

	private Vibrator mVibrator01 = null;
	private LocationClient mLocClient;
	private String mData;

	// 用于实现语音信箱的功能...
	public SpeechSynthesizer speechSynthesizer;
	public String smsBody;
	public String smsAddress;
	
	public static MediaPlayer getMediaPlayerInstance(Context context) {
		if (mediaPlayer == null) {
			mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
		}
		mediaPlayer.setVolume(1.0f, 1.0f);
		mediaPlayer.setLooping(true);

		return mediaPlayer;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		//语音信箱功能的相关设置
		speechSynthesizer = new SpeechSynthesizer(context,
                "holder", this);
        // 此处需要将setApiKey方法的两个参数替换为你在百度开发者中心注册应用所得到的apiKey和secretKey
        speechSynthesizer.setApiKey("f7Q2MxcTmYdprGIsjewHVyfx", "KWXUOoev1BXfbT6HCIw3bGIU0UCyeKpk");
        speechSynthesizer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        setVolumeControlStream(AudioManager.STREAM_MUSIC);

		System.out.println("------>sp:拦截到短信..");

		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

		blackNumberDao = new BlackNumberDao(context);

		boolean isprotected = sp.getBoolean("isprotected", false);
		
		boolean isVoiceSms = sp.getBoolean("isVoiceSms", false);
		
		if (isprotected  || isVoiceSms) {
			devicePolicyManager = (DevicePolicyManager) context
					.getSystemService(Context.DEVICE_POLICY_SERVICE);

			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			for (Object pdu : pdus) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
				String body = smsMessage.getDisplayMessageBody();
				smsBody = body;
				
				String address = smsMessage.getDisplayOriginatingAddress();
				smsAddress = address;
				
				String safe_number = sp.getString("safe_number", "");

				if ("#*location*#".equals(body)) {
					GPSInfoService service = GPSInfoService
							.getInstance(context);
					service.registenerLocationChangeListener();
					String last_location = service.getLastLocation();

					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(safe_number, null, "你的手机的位置是  :"
							+ last_location, null, null);

					abortBroadcast();

				} else if ("#*lockscreen*#".equals(body)) {
					devicePolicyManager.lockNow();

					String pwd = sp.getString("pwd", "");

					devicePolicyManager.resetPassword(pwd, 0);

					abortBroadcast();
				} else if ("#*delete*#".equals(body)) {
					devicePolicyManager.wipeData(0);

					abortBroadcast();
				} else if ("#*alarm*#".equals(body)) {
					mediaPlayer = getMediaPlayerInstance(context);
					mediaPlayer.start();

					abortBroadcast();
				} else if (body.contains("6+1") || body.contains("cctv")) {
					abortBroadcast();
				} else if ("#*stopalarm*#".equals(body)) {
					mediaPlayer = getMediaPlayerInstance(context);

					if (mediaPlayer.isLooping() || mediaPlayer.isPlaying()) {
						mediaPlayer.pause();
					}

					abortBroadcast();
				}

				System.out.println("收到的短信的内容是: " + body);

				boolean isBlackNumber = blackNumberDao.isBlackNumber(address);
				if (isBlackNumber) {
					abortBroadcast();
				}
			}
		}
		
		
		if(isVoiceSms){
			System.out.println("--------->isVoiceSms: " + isVoiceSms);
			new Thread(new Runnable() {

                @Override
                public void run() {
                    setParams();
                    
                    int ret = speechSynthesizer.speak("收到来自"+ smsAddress +"的短信," + "短信内容是: " + smsBody +"本次语音信箱服务由黄俊东大神开发的心飞翔手机卫士提供.再次感谢你的使用.");
                    if (ret != 0) {
                        System.out.println("开始合成器失败：ret" + ret);
                    }
                }
            }).start();
		}
	}

	@Override
	public void onStartWorking(SpeechSynthesizer synthesizer) {
		System.out.println("---->开始工作，请等待数据...");
	}

	@Override
	public void onSpeechStart(SpeechSynthesizer synthesizer) {
		System.out.println("----->朗读开始");
	}

	@Override
	public void onSpeechResume(SpeechSynthesizer synthesizer) {
		System.out.println("---->朗读继续");
	}

	@Override
	public void onSpeechProgressChanged(SpeechSynthesizer synthesizer,
			int progress) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeechPause(SpeechSynthesizer synthesizer) {
		System.out.println("-------->朗读已暂停");
	}

	@Override
	public void onSpeechFinish(SpeechSynthesizer synthesizer) {
		System.out.println("-------->朗读已停止");
	}

	@Override
	public void onNewDataArrive(SpeechSynthesizer synthesizer,
			byte[] dataBuffer, int dataLength) {

		System.out.println("----->新的音频数据: " + dataLength);
	}

	@Override
	public void onError(SpeechSynthesizer synthesizer, SpeechError error) {

		System.out.println("--------->发生错误：" + error.errorDescription + "("
				+ error.errorCode + ")");
	}

	@Override
	public void onCancel(SpeechSynthesizer synthesizer) {
		System.out.println("------>已取消");
	}

	@Override
	public void onBufferProgressChanged(SpeechSynthesizer synthesizer,
			int progress) {
		// TODO Auto-generated method stub

	}
	
	private void setParams() {
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "5");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_ENCODE, "1");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_RATE, "4");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_LANGUAGE, "ZH");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_NUM_PRON, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_ENG_PRON, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PUNC, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_BACKGROUND, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_STYLE, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TERRITORY, "0");
    }

}
