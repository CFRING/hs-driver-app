package com.amap.api.maps2d.selfdefineoverlay;

import java.util.List;

import android.content.Context;


import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveStep;

public class DrivingRouteOverlay extends RouteOverlay {
	private DrivePath drivePath;

	public DrivingRouteOverlay(Context context, AMap amap, DrivePath path,
			LatLonPoint start, LatLonPoint end) {
		super(context);
		this.mAMap = amap;
		this.drivePath = path;
		startPoint = AMapServicesUtil.convertToLatLng(start);
		endPoint = AMapServicesUtil.convertToLatLng(end);
	}

	/**
	 * 绘制节点和线�?
	 */
	public void addToMap() {
		List<DriveStep> drivePaths = drivePath.getSteps();
		for (int i = 0; i < drivePaths.size(); i++) {
			DriveStep driveStep = drivePaths.get(i);
			LatLng latLng = AMapServicesUtil.convertToLatLng(driveStep
					.getPolyline().get(0));
			if (i < drivePaths.size() - 1) {
				if (i == 0) {
					Polyline oneLine = mAMap.addPolyline(new PolylineOptions()
							.add(startPoint, latLng).color(getDriveColor())
							.width(getBuslineWidth()));// 把起始点和第�?��步行的起点连接起�?
					allPolyLines.add(oneLine);
				}
				LatLng latLngEnd = AMapServicesUtil.convertToLatLng(driveStep
						.getPolyline().get(driveStep.getPolyline().size() - 1));
				LatLng latLngStart = AMapServicesUtil
						.convertToLatLng(drivePaths.get(i + 1).getPolyline()
								.get(0));
				if (!(latLngEnd.equals(latLngStart))) {
					Polyline breakLine = mAMap
							.addPolyline(new PolylineOptions()
									.add(latLngEnd, latLngStart)
									.color(getDriveColor())
									.width(getBuslineWidth()));// 把前�?��步行段的终点和后�?��步行段的起点连接起来
					allPolyLines.add(breakLine);
				}
			} else {
				LatLng latLng1 = AMapServicesUtil.convertToLatLng(driveStep
						.getPolyline().get(driveStep.getPolyline().size() - 1));
				Polyline endLine = mAMap.addPolyline(new PolylineOptions()
						.add(latLng1, endPoint).color(getDriveColor())
						.width(getBuslineWidth()));// 把最终点和最后一个步行的终点连接起来
				allPolyLines.add(endLine);
			}
/**
 * 去掉地图上的中转点
 */
//			Marker driveMarker = mAMap.addMarker(new MarkerOptions()
//					.position(latLng)
//					.title("\u65B9\u5411:" + driveStep.getAction()
//							+ "\n\u9053\u8DEF:" + driveStep.getRoad())
//					.snippet(driveStep.getInstruction()).anchor(0.5f, 0.5f)
//					.icon(getDriveBitmapDescriptor()));
//			stationMarkers.add(driveMarker);
			Polyline driveLine = mAMap.addPolyline(new PolylineOptions()
					.addAll(AMapServicesUtil.convertArrList(driveStep
							.getPolyline())).color(getDriveColor())
					.width(getBuslineWidth()));
			allPolyLines.add(driveLine);
		}
		addStartAndEndMarker();
	}

	protected float getBuslineWidth() {
		return 5;
	}
}
