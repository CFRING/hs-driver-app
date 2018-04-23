package com.amap.api.maps2d.selfdefineoverlay;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polyline;
import com.hongshi.wuliudidi.R;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;



public class RouteOverlay {
	protected List<Marker> stationMarkers = new ArrayList<Marker>();
	protected List<Polyline> allPolyLines = new ArrayList<Polyline>();
	protected Marker startMarker;
	protected Marker endMarker;
	protected LatLng startPoint;
	protected LatLng endPoint;
	protected AMap mAMap;
	private Context mContext;
	private Bitmap startBit, endBit, busBit, walkBit, driveBit;
	private AssetManager am;

	public RouteOverlay(Context context) {
		mContext = context;
		am = mContext.getResources().getAssets();
	}

	/**
	 * 清除绘制
	 */
	public void removeFromMap() {
		if (startMarker != null) {
			startMarker.remove();

		}
		if (endMarker != null) {
			endMarker.remove();
		}
		for (Marker marker : stationMarkers) {
			marker.remove();
		}
		for (Polyline line : allPolyLines) {
			line.remove();
		}
		destroyBit();
	}

	private void destroyBit() {
		if (startBit != null) {
			startBit.recycle();
			startBit = null;
		}
		if (endBit != null) {
			endBit.recycle();
			endBit = null;
		}
		if (busBit != null) {
			busBit.recycle();
			busBit = null;
		}
		if (walkBit != null) {
			walkBit.recycle();
			walkBit = null;
		}
		if (driveBit != null) {
			driveBit.recycle();
			driveBit = null;
		}
	}

	protected BitmapDescriptor getStartBitmapDescriptor() {
		return getBitDes(startBit, "amap_start.png");
	}

	protected BitmapDescriptor getEndBitmapDescriptor() {
		return getBitDes(endBit, "amap_end.png");
	}

	protected BitmapDescriptor getBusBitmapDescriptor() {
		return getBitDes(busBit, "amap_bus.png");
	}

	protected BitmapDescriptor getWalkBitmapDescriptor() {
		return getBitDes(walkBit, "amap_man.png");
	}

	protected BitmapDescriptor getDriveBitmapDescriptor() {
		return getBitDes(driveBit, "amap_car.png");
	}

	private BitmapDescriptor getBitDes(Bitmap bitmap, String fileName) {
		InputStream stream;
		try {
			stream = am.open(fileName);
			bitmap = BitmapFactory.decodeStream(stream);
			bitmap = AMapServicesUtil.zoomBitmap(bitmap, 1);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {

		}
		return BitmapDescriptorFactory.fromBitmap(bitmap);
	}

	protected void addStartAndEndMarker() {
		startMarker = mAMap.addMarker((new MarkerOptions())
				.position(startPoint)
				//.icon(getStartBitmapDescriptor())
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_point))
				.title("\u8D77\u70B9"));
		// startMarker.showInfoWindow();

		endMarker = mAMap.addMarker((new MarkerOptions())
				.position(endPoint)
				//.icon(getEndBitmapDescriptor())
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_point))
				.title("\u7EC8\u70B9"));
		// mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint,
		// getShowRouteZoom()));
	}

	public void zoomToSpan() {
		if (startPoint != null && startPoint != null) {
			if (mAMap == null)
				return;
			LatLngBounds bounds = getLatLngBounds();
			mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
		}
	}

	protected LatLngBounds getLatLngBounds() {
		LatLngBounds.Builder b = LatLngBounds.builder();
		b.include(new LatLng(startPoint.latitude, startPoint.longitude));
		b.include(new LatLng(endPoint.latitude, endPoint.longitude));
		return b.build();
	}

	protected int getWalkColor() {
		return Color.parseColor("#6db74d");
	}

	protected int getBusColor() {
		return Color.parseColor("#537edc");
	}

	protected int getDriveColor() {
		return Color.parseColor("#537edc");
	}

	// protected int getShowRouteZoom() {
	// return 15;
	// }
}
