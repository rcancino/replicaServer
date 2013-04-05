package com.luxsoft.replica

import java.util.List;

/**
 * EntityConfiguration
 * A domain class describes the data object and it's mapping to the database
 */
class EntityConfiguration {	
	
	String name
	String table
	String pk
	String insertSql
	String updateSql
	String excludeInsertColumns
	String excludeUpdateColumns
	
	Date	dateCreated
	Date	lastUpdated
	
	
	
    static mapping = {
    }
    
	static constraints = {
		name(nullable:false,maxSize:50,unique:true)
		table(nullable:false,maxSize:50)
		pk(nullable:false,maxSize:50)
		insertSql(nullable:true,maxSize:5000)
		updateSql(blank:false,maxSize:5000)
		excludeInsertColumns nullable:true,maxSize:250
		excludeUpdateColumns nullable:true,maxSize:250
    }
	

}
