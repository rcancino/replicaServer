package com.luxsoft.replica
		

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
//import com.luxsoft.replica.ImportadorJob

import groovy.sql.Sql

/**
 * HomeReplicaController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class ReplicaController {
	
	def replicaService 
	
	def tacubaDataSource
	def oficinasDataSource
	
	

	def index(){
		redirect action:"importarAuditLog"
	}
	/*
	def test2(){
		render replicaService.findUpdateQuery('tacubaDataSource','SX_FICHAS')
	}*/
	
	def instalarConfiguraciones(){
		replicaService.instalarConfiguraciones()
		redirect controller:'entityConfiguration', action:'list'
	}
	
	def test(){
		//SimpleJdbcInsert insert=new SimpleJdbcInsert(oficinasDataSource).withTableName("SX_VENTAS")
		//insert.compile();
		//render insert.getInsertString() 
		
		Sql target=new Sql(tacubaDataSource)
		def res="UPDATE SX_INVENTARIO_TRS SET "
		target.eachRow("select * from SX_INVENTARIO_TRS where 1=2 ", {meta->
			//res+=meta
			//meta.keySet.each {res+=it}
			//[1..meta.getColumnCount()].each{ res+=it}
			String colId=""
			for(int i=1;i<=meta.getColumnCount();i++){
				String ccol=meta.getColumnLabel(i)
				res+=ccol+"=:"+ccol
				if(i<meta.getColumnCount()+1)
					res+=","
				
			}
			res+=" WHERE $colId=:$colId"	
		  }) {}
		
		render res
	}
	
	def importarAuditLog(){
		replicaService.importarAuditLog('tacubaDataSource', 'oficinasDataSource')
		
		render 'Audit Log importados: '
	}
	
	
	

}


