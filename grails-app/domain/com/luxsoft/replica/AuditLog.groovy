package com.luxsoft.replica

import groovy.transform.ToString;

import java.util.Date;

/**
 * AuditLog
 * A domain class describes the data object and it's mapping to the database
 */
@ToString
class AuditLog {

	//String id
	String sucursal
	String entityName
	String entityId
	String action
	String message
	String tableName
	
	Date dateCreated
	Date lastUpdated
	Date replicado
	Long logOrigen
	
	//	static belongsTo	= []	// tells GORM to cascade commands: e.g., delete this object if the "parent" is deleted.
	//	static hasOne		= []	// tells GORM to associate another domain object as an owner in a 1-1 mapping
	//	static hasMany		= []	// tells GORM to associate other domain objects for a 1-n or n-m mapping
	//	static mappedBy		= []	// specifies which property should be used in a mapping
		
	   

    static constraints = {
		sucursal blank:false,maxLength:50
		entityName blank:false ,maxLength:40
		entityId nullable:false,maxLenth:255
		action blank:false,maxLength:20
		tableName nullable:false,maxLenth:50
		message nullable:true, maxSize:100000
		replicado nullable:true
		logOrigen nullable:true
    }
	
	static mapping = {
		//id generator: 'uuid',params: [separator: '-']
	}
    
	
	/*
	 * Methods of the Domain Class
	 */
//	@Override	// Override toString for a nicer / more descriptive UI 
//	public String toString() {
//		return "${name}";
//	}
}
