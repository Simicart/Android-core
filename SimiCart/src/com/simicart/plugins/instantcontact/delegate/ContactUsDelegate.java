package com.simicart.plugins.instantcontact.delegate;

import java.util.List;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.plugins.instantcontact.entity.ContactUsEntity;

public interface ContactUsDelegate extends SimiDelegate{
	
	public List<ContactUsEntity> getListContactUs();

}
