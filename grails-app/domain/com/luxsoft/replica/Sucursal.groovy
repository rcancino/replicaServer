package com.luxsoft.replica

import groovy.transform.ToString;

import java.util.Date;

/**
 * Sucursal
 * A domain class describes the data object and it's mapping to the database
 */
@ToString(excludes="dateCreated,lastUpdated")
class Sucursal {

	String nombre
	String dataSourceName
	String dataSourceUrl
	boolean activa=true
	
	
	Date	dateCreated
	Date	lastUpdated
	
//	static belongsTo	= []	// tells GORM to cascade commands: e.g., delete this object if the "parent" is deleted.
//	static hasOne		= []	// tells GORM to associate another domain object as an owner in a 1-1 mapping
//	static hasMany		= []	// tells GORM to associate other domain objects for a 1-n or n-m mapping
//	static mappedBy		= []	// specifies which property should be used in a mapping 
	
    static mapping = {
    }
    
	static constraints = {
		nombre(blank:false,unique:true)
		dataSourceName nullable:true
		dataSourceUrl nullable:true 
    }
	
	/*
	 * Methods of the Domain Class
	 */
//	@Override	// Override toString for a nicer / more descriptive UI 
//	public String toString() {
//		return "${name}";
//	}
}
