package com.luxsoft.replica

import org.springframework.batch.item.ItemWriter;

import java.util.List;

class AuditLogWriter implements ItemWriter<AuditLog>{

	@Override
	public void write(List<? extends AuditLog> items) throws Exception {
		items.each{
			//it.save();
			println 'Salvando audit log: '+it
		}
		
	}

}
