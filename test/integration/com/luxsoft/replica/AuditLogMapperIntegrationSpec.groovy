package com.luxsoft.replica

import org.springframework.jdbc.core.JdbcTemplate;

import spock.lang.Shared;
import grails.plugin.spock.IntegrationSpec
import groovy.sql.Sql

class AuditLogMapperIntegrationSpec extends IntegrationSpec {

	//@Shared sql=new Sql(tacubaDataSource);
	
	//@Shared
	
	
	def tacubaDataSource
	
	def setupSpec() {
		
	}

	def cleanupSpec() {
	}

	void "Probar el mapeo de AuditLog"() {
		given:"Un AuditLogMapper"
		JdbcTemplate jdbcTemplate=new JdbcTemplate(tacubaDataSource)
		def mapper=new AuditLogMapper()
		
		when:"Ejecutamos un select"
		def rows=jdbcTemplate.query("select * from audit_log",mapper)
		
		then:"Los registros se mapean a instancias de AuditLog"
		rows.size()==jdbcTemplate.queryForInt("select count(*) from audit_log")
		rows.each {it instanceof AuditLog}
	}
}