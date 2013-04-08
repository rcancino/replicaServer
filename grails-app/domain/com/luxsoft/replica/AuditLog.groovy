package com.luxsoft.replica


import groovy.transform.ToString;



/**
 * AuditLog
 * A domain class describes the data object and it's mapping to the database
 */
@ToString
class AuditLog {
	
	//@Column(nullable=false,length = 40)
	//private String entityName;
	String entityName
	//@Column(nullable=false,length = 255)
	//private String entityId;
	String entityId
	//@Column(nullable=false,length=20)
	//private String action;
	String action
	//@Lob
	//@Column
	//private String message;
	String message
	
	//@Column(nullable=false,length=50)
	//private String tableName;
	String tableName
	
	//@Column(nullable=false,length=50,name="SUCURSAL_ORIGEN")
	//private String sucursalOrigen;
	String sucursalOrigen
	
	//@Column(nullable=true,length=50,name="SUCURSAL_DESTINO")
	//private String sucursalDestino;
	String sucursalDestino;
	
	//@Column(nullable=true,length=50)
	//private String ip;
	String ip
	
	Date replicado
	
	//Long logOrigen
	
	Date dateCreated
	Date lastUpdated
	

    static constraints = {
		//sucursal blank:false,maxLength:50
		entityName blank:false ,maxLength:40
		entityId nullable:false,maxLenth:255
		action blank:false,maxLength:20
		tableName nullable:false,maxLenth:50
		sucursalOrigen nullable:false,maxLength:50
		sucursalDestino nullable:true,maxLength:50
		ip nullable:true,maxLength:50
		message nullable:true, maxSize:100000
		replicado nullable:true
		//logOrigen nullable:true
    }
	
	static mapping = {
		//id generator: 'uuid',params: [separator: '-']
		entityName column: "entityName"
		entityId column:'entityId'
		tableName column:'tableName'
		//sucursalOrigen column:'sucursalOrigen'
		//sucursalDestino column:'sucursalDestino'
		dateCreated column:'dateCreated'
		lastUpdated column:'lastUpdated'
	}
    
	
	
}
