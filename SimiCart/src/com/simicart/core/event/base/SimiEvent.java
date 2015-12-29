package com.simicart.core.event.base;

import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;

import com.simicart.core.common.Utils;

public class SimiEvent {
	protected ArrayList<ItemMaster> itemsList = UtilsEvent.itemsList;

	public void disPatchEvent(String eventName, Object[] objects,
			boolean hasMethod) {
		ArrayList<ItemMaster> items = checkEventForEventName(eventName);

		for (ItemMaster itemMaster : items) {
			try {
				String method = itemMaster.getMethod();
				String fullName = itemMaster.getPackageName() + "."
						+ itemMaster.getClassName();
				if (!Utils.validateString(method)) {
					method = "";
				}
				if (!Utils.validateString(fullName)) {
					fullName = "";
				}

				Class<?> change = Class.forName(fullName);

				int size = objects.length;

				if (size == 0) {
					if (hasMethod) {
						change.getConstructor(method.getClass()).newInstance(
								method);
					}
				} else {
					Object[] tmp_object = null;
					if (hasMethod) {
						tmp_object = new Object[size + 1];
						tmp_object[0] = method;
						for (int i = 0; i < size; i++) {
							tmp_object[i + 1] = objects[i];
						}

					} else {
						tmp_object = objects;
					}
					Class<?>[] list_class = new Class<?>[tmp_object.length];
					for (int i = 0; i < tmp_object.length; i++) {
						list_class[i] = tmp_object[i].getClass();
					}
					change.getConstructor(list_class).newInstance(tmp_object);
				}
			} catch (Exception e) {
				Log.e("SimiEvent  :" + eventName, "Exception " + e.getMessage());
			}
		}

	}

	public ItemMaster getItemForEvent(String eventName) {
		ArrayList<ItemMaster> items = checkEventForEventName(eventName);
		return getItemUrgentFromList(items);
	}

	public ArrayList<ItemMaster> checkEventForEventName(String eventName) {
		ArrayList<ItemMaster> items = new ArrayList<ItemMaster>();
		for (int i = 0; i < itemsList.size(); i++) {
			ItemMaster item = itemsList.get(i);
			String sku = item.getSku();
			String name_event = item.getName();
			if (Utils.validateString(sku) && EventListener.isEvent(sku)) {

				if (eventName
						.equals("com.simicart.core.checkout.block.CartBlock")) {
					Log.e("SimiEvent checkEventForEventName  :", name_event);
				}

				if (eventName.equals(name_event)) {

					items.add(item);
				}
			}
		}
		return items;
	}

	public ItemMaster getItemUrgentFromList(ArrayList<ItemMaster> items) {
		if (null != items) {
			Collections.sort(items);
			if (items.size() > 0) {
				return items.get(0);
			}
		}
		return null;
	}

}
