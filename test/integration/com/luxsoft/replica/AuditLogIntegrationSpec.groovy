package com.luxsoft.replica

import grails.plugin.spock.IntegrationSpec
import spock.lang.*

class AuditLogIntegrationSpec extends IntegrationSpec {

	
	void "Salvar un AuditLog"() {
		given:"Un AuditLog nuevo"
		def auditLog=new AuditLog().build(sucursal:"TACUBA",entityName:"Existencia",action:"INSERT")
		when:"La instancia es salvada"
		auditLog.save()
		then:"La instancia es exitosamente persistida en la base de datos"
		auditLog.errors.errorCount==0
		auditLog.id
		AuditLog.get(auditLog.id)
	}
}