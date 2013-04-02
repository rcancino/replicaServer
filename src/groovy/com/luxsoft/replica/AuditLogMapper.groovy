package com.luxsoft.replica

import java.sql.ResultSet
import java.sql.SQLException
import java.util.Date

import org.springframework.jdbc.core.RowMapper

class AuditLogMapper implements RowMapper<AuditLog>{

	/**
	 * String sucursal
	String entityName
	String entityId
	String action
	String message
	String tableName
	Date replicado
	Long logOrigen
	 * @param rs
	 * @param arg1
	 * @return
	 * @throws SQLException
	 */
	@Override
	public AuditLog mapRow(ResultSet rs, int arg1) throws SQLException {
		AuditLog audit=new AuditLog()
		//audit.sucursal=rs.getString("sucursal")
		audit.entityName=rs.getString("entityName")
		audit.entityId=rs.getString("entityId")
		audit.action=rs.getString("action")
		audit.tableName=rs.getString("tableName")
		//audit.logOrigen=rs.getString("id")
		return audit;
	}	

}
