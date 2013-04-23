package com.luxsoft.procesos

import groovy.sql.Sql
import com.luxsoft.replica.AuditLog
import com.luxsoft.replica.Sucursal;

class ActualizadorDeClientesJob {
    
	def dataSourceLookup
	
	static transactional = true
	
	static triggers = {
      simple repeatInterval: 20000l // execute job once in 5 seconds
		//simple repeatCount:0,startDelay:5000 //LrepeatInterval: 20000l // execute job once in 5 seconds
    }

    def execute() {
		
		def pendientes=AuditLog.findAllByEntityNameAndSucursalDestinoAndReplicadoIsNull('Cliente','TODAS')
		println 'Clientes pendientes: ***********'+pendientes
		
		pendientes.each{auditRow->
			
			def sucursales=Sucursal.findAllByActivaAndNombreNotEqual('true','OFICINAS').collect({it.nombre})
			
			sucursales.each { sucursal->
				
				new AuditLog(
					entityName:'Cliente'
					,entityId:auditRow.entityId
					,action:'UPDATE'
					,tableName:'SX_CLIENTES'
					,sucursalOrigen:'OFICINAS'
					,sucursalDestino:sucursal).save()
				
				//Localizando ClienteCredito
				def credito=getOficinasSql().firstRow("select CREDITO_ID from SX_CLIENTES where CLIENTE_ID=?",[auditRow.entityId])
				
				if(credito){ 
					new AuditLog(
						entityName:'ClienteCredito'
						,entityId:credito.CREDITO_ID
						,action:'UPDATE'
						,tableName:'SX_CLIENTES_CREDITO'
						,sucursalOrigen:'OFICINAS'
						,sucursalDestino:sucursal).save()
				}
			}
			auditRow.replicado=new Date()
			//auditRow.save(flush:true)
		}
		
    }
	
	private Sql sql
	
	private Sql getOficinasSql(){
		if(sql==null){
			sql=new Sql(dataSourceLookup.getDataSource('oficinasDataSource'))
		}
		return sql
	}
}
