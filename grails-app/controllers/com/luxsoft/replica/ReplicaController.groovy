package com.luxsoft.replica

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import groovy.sql.Sql

/**
 * HomeReplicaController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class ReplicaController {
	
	def tacubaDataSource
	def oficinasDataSource

	def index(){
		redirect action:"importarAuditLog"
	}
	
	def test(){
		//SimpleJdbcInsert insert=new SimpleJdbcInsert(oficinasDataSource).withTableName("SX_LINEAS")
		// insert.compile();
		Sql target=new Sql(oficinasDataSource)
		def res="UPDATE SX_LINEAS SET "
		target.eachRow("select * from sx_lineas", {meta->
			//res+=meta
			//meta.keySet.each {res+=it}
			//[1..meta.getColumnCount()].each{ res+=it}
			String colId=""
			for(int i=1;i<=meta.getColumnCount();i++){
				String ccol=meta.getColumnLabel(i)
				if(ccol.endsWith("ID"))
					continue
				res+=ccol+"=:"+ccol
				if(i<meta.getColumnCount()+1)
					res+=","
				
			}	
		  }) {}
		
		render res
	}
	
	def importarAuditLog(){
		
		Sql sql=new Sql(tacubaDataSource)
		Sql target=new Sql(oficinasDataSource)
		
		sql.eachRow("select * from audit_log where replicado is null order by id") {
			def model=getModel(it.entityName)
			if(model){
				
				def origenSql="select * from $model.table where $model.columnId=?"
				def row=sql.firstRow(origenSql, [it.entityId])
				if(it.action=='INSERT'){
					println 'Insert: '+row
					SimpleJdbcInsert insert=new SimpleJdbcInsert(oficinasDataSource).withTableName(it.tableName)
					insert.execute(row)
					sql.execute("UPDATE AUDIT_LOG SET REPLICADO=NOW() WHERE ID=? ", [it.id])
					//target.executeInsert(row, model.insertSql)
					//def e=target.firstRow("select * from sx_existencias where year=2013 and mes=3 and sucursal_id=2 and producto_id=6381")
					//println e
				}else if(it.action=='UPDATE'){
					println 'Update: '+row
					target.executeUpdate(row, model.updateSql)
					sql.execute("UPDATE AUDIT_LOG SET REPLICADO=NOW() WHERE ID=? ", [it.id])
				}else if(it.action=='DELETE'){
					println 'Delete: '+row
				}
				//println "Importando" +origenSql
			}
			
		}
		render 'Audit Log importados: '
	}
	
	
	def getModel(String name){
		if(name=="Existencia")
			return new EntityModel(name:name,table:"SX_EXISTENCIAS"
				,columnId:"INVENTARIO_ID"
				,insertSql:"""INSERT INTO SX_EXISTENCIAS
					(INVENTARIO_ID,CANTIDAD,CLAVE,COSTO,COSTOP,COSTOU,CREADO,CREADO_USERID,DESCRIPCION,FACTORU,FECHA,TX_IMPORTADO,KILOS,MES,MODIFICADO,NACIONAL,RECORTE,RECORTE_COMENTARIO,RECORTE_FECHA,TX_REPLICADO,UNIDAD,MODIFICADO_USERID,version,YEAR,SUCURSAL_ID
					,PRODUCTO_ID)
			
				"""
				,updateSql:"""
					UPDATE SX_EXISTENCIAS SET
					SUCURSAL_ID=:SUCURSAL_ID,PRODUCTO_ID=:PRODUCTO_ID
					,CLAVE=:CLAVE,MES=:MES,YEAR=:YEAR,CANTIDAD=:CANTIDAD
					,UNIDAD=:UNIDAD
					,FACTORU=:FACTORU,FECHA=:FECHA
					,COSTO=:COSTO
					,COSTOP=:COSTOP,COSTOU=:COSTOP,DESCRIPCION=:DESCRIPCION
					,CREADO=:CREADO,CREADO_USERID=:CREADO_USERID,MODIFICADO=:MODIFICADO
					,MODIFICADO_USERID=:MODIFICADO_USERID
					,version=:version,KILOS=:KILOS,NACIONAL=:NACIONAL,RECORTE=:RECORTE
					,TX_IMPORTADO=:TX_IMPORTADO,TX_REPLICADO=:TX_REPLICADO,RECORTE_COMENTARIO=:RECORTE_COMENTARIO,RECORTE_FECHA=:RECORTE_FECHA 
					WHERE INVENTARIO_ID=:INVENTARIO_ID
				"""
				)
		else 
			return null;
	}

}

class EntityModel{
	String name
	String table
	String columnId
	String insertSql
	String updateSql
}
