package com.luxsoft.replica

import com.luxsoft.replica.Sucursal
import groovy.sql.Sql

/**
 * ValidacionController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class ValidacionController {
	
	def dataSourceLookup
	def dataSource
	
	def index(){
		render 'OK validando compras'  
	}

	def validarCompras(){
		
		def s=Sucursal.findByNombre('CALLE4')
		def ds=dataSourceLookup.getDataSource(s.dataSourceName)
		def sql=new Sql(ds)
		def query="""
				SELECT c.compra_id,d.COMPRADET_ID,c.FOLIO,d.CLAVE,d.SOLICITADO,d.DEPURADO,
				(SELECT IFNULL(SUM(I.CANTIDAD),0)FROM sx_inventario_com i where i.COMPRADET_ID=d.COMPRADET_ID ) as RECIBIDO 
				,(d.SOLICITADO-d.depurado)-(SELECT IFNULL(SUM(I.CANTIDAD),0)FROM sx_inventario_com i where i.COMPRADET_ID=d.COMPRADET_ID ) AS SALDO
				FROM sx_compras2 c join sx_compras2_det d on (c.COMPRA_ID=d.COMPRA_ID)
				where date(c.FECHA) between '2013/03/01' and '2013/03/31'
				AND C.SUCURSAL_ID=2 AND ( DEPURADO<>0 OR 
				((d.SOLICITADO-d.depurado)-(SELECT IFNULL(SUM(I.CANTIDAD),0)FROM sx_inventario_com i where i.COMPRADET_ID=d.COMPRADET_ID )) <>0)
				"""
		def rowsSucursal=sql.rows(query)
		
		def oficinas=new Sql(dataSource)
		def sqlOficinas=new Sql(oficinas)
		
		def rowsOficinas=sqlOficinas.rows(query)
		
		def res=[]
		
		rowsSucursal.each {row->
			def d1=row.DEPURADO
			def found=rowsOficinas.find {it.COMPRADET_ID==row.COMPRADET_ID}
			println "CompraDet:${row.COMPRADET_ID} Depurado Sucursal:${d1} oficinas:${found.DEPURADO}"
			if(found){
				if(d1!=found.DEPURADO){
					def line="Depurado Sucursal:${d1} oficinas:${found.DEPURADO}"
					res.add(line)
				}
			}else
				res.add("NO ENCONTRO EN OFICINAS: "+row.COMPRADET_ID)
			
		}
		
		render "Registros en ${s.nombre}: ${rowsSucursal.size()} Oficinas:${rowsOficinas.size()} ${res}"
		
	}
	
}
