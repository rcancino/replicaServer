
<%@ page import="com.luxsoft.replica.EntityConfiguration" %>
<!doctype html>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="kickstart" />
	<g:set var="entityName" value="${message(code: 'entityConfiguration.label', default: 'EntityConfiguration')}" />
	<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>

<section id="show-entityConfiguration" class="first">

	<table class="table">
		<tbody>
		
			<tr class="prop">
				<td valign="top" class="name"><g:message code="entityConfiguration.name.label" default="Name" /></td>
				
				<td valign="top" class="value">${fieldValue(bean: entityConfigurationInstance, field: "name")}</td>
				
			</tr>
		
			<tr class="prop">
				<td valign="top" class="name"><g:message code="entityConfiguration.table.label" default="Table" /></td>
				
				<td valign="top" class="value">${fieldValue(bean: entityConfigurationInstance, field: "table")}</td>
				
			</tr>
		
			<tr class="prop">
				<td valign="top" class="name"><g:message code="entityConfiguration.pk.label" default="Pk" /></td>
				
				<td valign="top" class="value">${fieldValue(bean: entityConfigurationInstance, field: "pk")}</td>
				
			</tr>
		
			<tr class="prop">
				<td valign="top" class="name"><g:message code="entityConfiguration.insertSql.label" default="Insert Sql" /></td>
				
				<td valign="top" class="value">${fieldValue(bean: entityConfigurationInstance, field: "insertSql")}</td>
				
			</tr>
		
			<tr class="prop">
				<td valign="top" class="name"><g:message code="entityConfiguration.updateSql.label" default="Update Sql" /></td>
				
				<td valign="top" class="value">${fieldValue(bean: entityConfigurationInstance, field: "updateSql")}</td>
				
			</tr>
		
			<tr class="prop">
				<td valign="top" class="name"><g:message code="entityConfiguration.excludeInsertColumns.label" default="Exclude Insert Columns" /></td>
				
				<td valign="top" class="value">${fieldValue(bean: entityConfigurationInstance, field: "excludeInsertColumns")}</td>
				
			</tr>
		
			<tr class="prop">
				<td valign="top" class="name"><g:message code="entityConfiguration.excludeUpdateColumns.label" default="Exclude Update Columns" /></td>
				
				<td valign="top" class="value">${fieldValue(bean: entityConfigurationInstance, field: "excludeUpdateColumns")}</td>
				
			</tr>
		
			<tr class="prop">
				<td valign="top" class="name"><g:message code="entityConfiguration.dateCreated.label" default="Date Created" /></td>
				
				<td valign="top" class="value"><g:formatDate date="${entityConfigurationInstance?.dateCreated}" /></td>
				
			</tr>
		
			<tr class="prop">
				<td valign="top" class="name"><g:message code="entityConfiguration.lastUpdated.label" default="Last Updated" /></td>
				
				<td valign="top" class="value"><g:formatDate date="${entityConfigurationInstance?.lastUpdated}" /></td>
				
			</tr>
		
		</tbody>
	</table>
</section>

</body>

</html>
