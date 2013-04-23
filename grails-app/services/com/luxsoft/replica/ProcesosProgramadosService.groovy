package com.luxsoft.replica

import com.luxsoft.utils.Periodo;

import groovy.sql.Sql

/**
 * 
 */
class ProcesosProgramadosService {

    static transactional = false
	
	def dataSourceLookup

    def actualizarAtrasoMaximo() {
		def ds=dataSourceLookup.getDataSource('oficinasDataSource')
		def sql =new Sql(ds)
		sql.eachRow("""
			SELECT c.CREDITO_ID,c.CLAVE
			,CASE WHEN IFNULL(MAX(ROUND(TO_DAYS(CURRENT_DATE)-TO_DAYS(V.VTO),0)),0) <0	THEN 0 
			ELSE IFNULL(MAX(ROUND(TO_DAYS(CURRENT_DATE)-TO_DAYS(V.VTO),0)),0) END AS ATRASO
			FROM sx_ventas V JOIN SX_CLIENTES_CREDITO C ON(C.CLAVE=V.CLAVE) 
			WHERE DATE(V.FECHA) > '2012/01/01' AND V.ORIGEN='CRE'  
			AND V.TOTAL-IFNULL((SELECT SUM(B.IMPORTE) FROM sx_cxc_aplicaciones B WHERE B.CARGO_ID=V.CARGO_ID),0)<>0
			AND V.CARGO_ID NOT IN(SELECT X.CARGO_ID FROM sx_juridico X)   
			GROUP BY V.CLAVE
		""") {row ->
		
			def update="UPDATE SX_CLIENTES_CREDITO SET ATRASO_MAX=?,MODIFICADO=NOW() WHERE CLAVE=? "
			
			
			int updated=sql.executeUpdate(update, [row.ATRASO,row.CLAVE])
			println "Actualizando atraso  Clave: ${row.CLAVE} Atraso: ${row.ATRASO}  res: "+updated
			
			def sucursales=Sucursal.findAllByActivaAndNombreNotEqual('true','OFICINAS').collect({it.nombre})
			
			sucursales.each { sucursal->
				
				def audit=new AuditLog(
					entityName:'ClienteCredito'
					,entityId:row.CREDITO_ID
					,action:'UPDATE'
					,message:''
					,tableName:'SX_CLIENTES_CREDITO'
					,sucursalOrigen:'OFICINAS'
					,sucursalDestino:sucursal)
				audit.save(flush:true)
			}
		}
    }
	
	def actualizarVentasPorFacturista(def periodo){
		def task=new VentasPorFacturista(dataSourceLookup:dataSourceLookup)
		task.execute()
	}
	
		
}

