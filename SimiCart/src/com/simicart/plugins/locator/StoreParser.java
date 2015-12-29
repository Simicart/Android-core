package com.simicart.plugins.locator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.simicart.plugins.locator.entity.SpecialObject;
import com.simicart.plugins.locator.entity.StoreObject;

public class StoreParser {
	public List<StoreObject> list;
	private String result;

	public List<StoreObject> getResult(JSONObject jsonObject) {
		try {
			result = jsonObject.getString("status");
			if (result.equals("SUCCESS")) {
				// JSONObject jsobject = jsonObject.getJSONObject("data");
				// JSONArray jsonArray = jsonObject.getJSONArray("data");
				JSONArray array = jsonObject.getJSONArray("data");
				if (array != null && array.length() != 0) {
					list = new ArrayList<StoreObject>();
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						StoreObject storeObject = new StoreObject();
						List<SpecialObject> list_spe = new ArrayList<SpecialObject>();
						List<SpecialObject> list_holy = new ArrayList<SpecialObject>();
						storeObject.setAddress(object.getString("address"));
						storeObject.setCity(object.getString("city"));
						storeObject.setCountry(object.getString("country"));
						// haita
						storeObject.setCountryName(object
								.getString("country_name"));
						// end
						storeObject.setStoreID(object
								.getString("storelocator_id"));
						storeObject.setZipcode(object.getString("zipcode"));
						storeObject.setState(object.getString("state"));
						storeObject.setState_id(object.getString("state_id"));
						storeObject.setEmail(object.getString("email"));
						storeObject.setPhone(object.getString("phone"));
						storeObject.setFax(object.getString("fax"));
						storeObject.setName(object.getString("name"));
						storeObject.setDescription(object
								.getString("description"));
						storeObject.setStatus(object.getString("status"));
						storeObject.setSort(object.getString("sort"));
						storeObject.setLink(object.getString("link"));
						DecimalFormat decim = new DecimalFormat("#.##");
						String dist_travelled = decim
								.format(Double.parseDouble(object
										.getString("distance")) / 1000);
						storeObject.setDistance(dist_travelled);
						storeObject.setLatitude(object.getString("latitude"));
						storeObject.setLongtitude(object
								.getString("longtitude"));
						storeObject.setMonday_status(object
								.getString("monday_status"));
						storeObject.setMonday_open(object
								.getString("monday_open"));
						storeObject.setMonday_close(object
								.getString("monday_close"));
						storeObject.setTuesday_status(object
								.getString("tuesday_status"));
						storeObject.setTuesday_open(object
								.getString("tuesday_open"));
						storeObject.setTuesday_close(object
								.getString("tuesday_close"));
						storeObject.setWednesday_status(object
								.getString("wednesday_status"));
						storeObject.setWednesday_open(object
								.getString("wednesday_open"));
						storeObject.setWednesday_close(object
								.getString("wednesday_close"));
						storeObject.setThursday_status(object
								.getString("thursday_status"));
						storeObject.setThursday_open(object
								.getString("thursday_open"));
						storeObject.setThursday_close(object
								.getString("thursday_close"));
						storeObject.setFriday_status(object
								.getString("friday_status"));
						storeObject.setFriday_open(object
								.getString("friday_open"));
						storeObject.setFriday_close(object
								.getString("friday_close"));
						storeObject.setSaturday_status(object
								.getString("saturday_status"));
						storeObject.setSaturday_open(object
								.getString("saturday_open"));
						storeObject.setSaturday_close(object
								.getString("saturday_close"));
						storeObject.setSunday_status(object
								.getString("sunday_status"));
						storeObject.setSunday_open(object
								.getString("sunday_open"));
						storeObject.setSunday_close(object
								.getString("sunday_close"));
						storeObject.setZoom_level(object
								.getString("zoom_level"));
						storeObject.setImage_icon(object.getString("image"));
						JSONArray array_specal = object
								.getJSONArray("special_days");
						JSONArray array_holy = null;
						try {
							array_holy = object.getJSONArray("holiday_days");
						} catch (Exception e) {
							// TODO: handle exception
						}
						if (array_specal != null && array_specal.length() != 0) {
							for (int ii = 0; ii < array_specal.length(); ii++) {
								JSONObject speObject = array_specal
										.getJSONObject(ii);
								SpecialObject specialObject = new SpecialObject();
								specialObject.setDate(speObject
										.getString("date"));
								specialObject.setTime_open(speObject
										.getString("time_open"));
								specialObject.setTime_close(speObject
										.getString("time_close"));
								list_spe.add(specialObject);
							}
							storeObject.setList_special(list_spe);
						}
						// if(object.get("holiday_days") instanceof JSONObject){
						// JSONObject holy_object =
						// object.getJSONObject("holiday_days");
						// Iterator<Object> keys = holy_object.keys();
						// while (keys.hasNext()) {
						// String key = String.valueOf(keys.next());
						// JSONObject object_key =
						// holy_object.getJSONObject(key);
						// SpecialObject holyObject = new SpecialObject();
						// holyObject.setId(key);
						// holyObject.setDate(object_key.getString("date"));
						// list_holy.add(holyObject);
						// }
						// storeObject.setList_holiday(list_holy);
						// }
						if (array_holy != null && array_holy.length() != 0) {
							for (int iii = 0; iii < array_holy.length(); iii++) {
								JSONObject holyObject = array_holy
										.getJSONObject(iii);
								SpecialObject specialObject = new SpecialObject();
								specialObject.setDate(holyObject
										.getString("date"));

								list_holy.add(specialObject);
							}
							storeObject.setList_holiday(list_holy);
						}
						list.add(storeObject);
					}
				}
			}
		} catch (Exception e) {

		}

		return list;
	}
}
