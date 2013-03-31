package com.luxsoft.replica

import grails.plugin.spock.IntegrationSpec
import groovy.sql.Sql
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException
import spock.lang.*;

/**
* Pruebas para las conecciones remotas
*
**/
class ConnectionsIntegrationSpec extends IntegrationSpec {

	def dataSourceLookup 

	def setup() {
	}

	def cleanup() {
	}

	def "Coneccion con tacuba"(){
		given:"Una referencia DataSource"
		def ds=dataSourceLookup.getDataSource('tacubaDataSource')
		
		when:"Ejecutamos un query simple"
		Sql sql=new Sql(ds)
		def row=sql.firstRow("select * from SX_PRODUCTOS")
		println 'Row: '+row
		then:"Obtenemos un registro"
		row.containsKey('PRODUCTO_ID')
		row.get("DESCRIPCION")
		!row.isEmpty()
	}

	def "Buscar un DataSource inexitente"(){
		
		when:" Buscamos el DataSource"
		dataSourceLookup.getDataSource("falseDataSource")
		then: "No DataSource should be found"
		DataSourceLookupFailureException e=thrown()


	}
}