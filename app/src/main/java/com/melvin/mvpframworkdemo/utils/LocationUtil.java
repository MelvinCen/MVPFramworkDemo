package com.melvin.mvpframworkdemo.utils;

/**
 * 百度定位封装基础类
 */
public class LocationUtil {
//	private static LocationUtil INSTANCE = null;
//	private Context context;
//	private LocationClient mLocClient = null;
//	private MyLocationListenner myListener = new MyLocationListenner();
//	private LocationListener locationListener;
//
//	public static LocationUtil getInstance() {
//		if (INSTANCE == null) {
//			INSTANCE = new LocationUtil();
//		}
//		return INSTANCE;
//	}
//
//	private LocationUtil() {
//		this.context = MyApp.getAppContext();
//	}
//
//	public void startLocation() {
//		if(mLocClient!=null)
//			return;
//		// 定位初始化
//		mLocClient = new LocationClient(context);
//		mLocClient.registerLocationListener(myListener);
//
//		LocationClientOption option = new LocationClientOption();
//		option.setCoorType("bd09ll");       // 设置坐标类型,默认gcj02,bd09ll
//		option.setScanSpan(1000);            //扫描
//		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//		option.setOpenGps(true);// 打开gps
//		option.setIsNeedAddress(true);//设置是否需要地址信息，默认不需要
//		option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//
//		mLocClient.setLocOption(option);
//		mLocClient.start();
//	}
//
//	public void closeLocation() {
//		if(mLocClient != null)
//			mLocClient.stop();
//		mLocClient = null;
//		locationListener = null;
//	}
//
//	/**
//	 * 定位SDK监听函数
//	 */
//	public class MyLocationListenner implements BDLocationListener {
//
//		@Override
//		public void onReceiveLocation(BDLocation location) {
//			if (location == null)
//				return;
//
//			if(location.getLatitude() == 4.9E-324 && location.getLongitude() == 4.9E-324) {   //百度定位失败
//				return;
//			}
//
//			StringBuffer sb = new StringBuffer(256);
//			sb.append("time : ");
//			sb.append(location.getTime());
//			sb.append("\nerror code : ");
//			sb.append(location.getLocType());
//			sb.append("\nlatitude : ");
//			sb.append(location.getLatitude());
//			sb.append("\nlontitude : ");
//			sb.append(location.getLongitude());
//			sb.append("\nradius : ");
//			sb.append(location.getRadius());
//			sb.append("\ndescribe : ");
//			sb.append(location.getLocationDescribe());
//
//			if (location.getLocType() == BDLocation.TypeGpsLocation ||
//					location.getLocType() == BDLocation.TypeNetWorkLocation){ // GPS定位结果, 网络定位结果
//				sb.append("\naddr : ");
//				sb.append(location.getAddrStr());
//
//				if(locationListener != null) {
//					locationListener.locationSuccess(location.getLongitude(), location.getLatitude());
//				}
//			}
//
//			Log.e("BaiduLocationApiDem", sb.toString());
//		}
//	}
//
//	public void setLocationListener(LocationListener locationListener) {
//		this.locationListener = locationListener;
//	}
//
//	public interface LocationListener {
//		void locationSuccess(double lontitude, double latitude);
//	}
}
