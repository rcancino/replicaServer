
<%@ page import="com.luxsoft.replica.EntityConfiguration" %>
<!doctype html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="kickstart" />
	<g:set var="entityName" value="${message(code: 'entityConfiguration.label', default: 'EntityConfiguration')}" />
	<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>

<body>
	
<section id="list-entityConfiguration" class="first">

	<table class="table table-bordered">
		<thead>
			<tr>
			
				<g:sortableColumn property="name" title="${message(code: 'entityConfiguration.name.label', default: 'Name')}" />
				<g:sortableColumn property="table" title="${message(code: 'entityConfiguration.table.label', default: 'Table')}" />
				<g:sortableColumn property="pk" title="${message(code: 'entityConfiguration.pk.label', default: 'Pk')}" />
				<g:sortableColumn property="insertSql" title="${message(code: 'entityConfiguration.insertSql.label', default: 'Insert Sql')}" />
				<g:sortableColumn property="updateSql" title="${message(code: 'entityConfiguration.updateSql.label', default: 'Update Sql')}" />
				<g:sortableColumn property="excludeInsertColumns" title="${message(code: 'entityConfiguration.excludeInsertColumns.label', default: 'Exclude Insert Columns')}" />
			
			</tr>
		</thead>
		<tbody>
		<g:each in="${entityConfigurationInstanceList}" status="i" var="entityConfigurationInstance">
			<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
			
				<td><g:link action="show" id="${entityConfigurationInstance.id}">${fieldValue(bean: entityConfigurationInstance, field: "name")}</g:link></td>
			
				<td>${fieldValue(bean: entityConfigurationInstance, field: "table")}</td>
			
				<td>${fieldValue(bean: entityConfigurationInstance, field: "pk")}</td>
			
				<td>${fieldValue(bean: entityConfigurationInstance, field: "insertSql")}</td>
			
				<td>${fieldValue(bean: entityConfigurationInstance, field: "updateSql")}</td>
			
				<td>${fieldValue(bean: entityConfigurationInstance, field: "excludeInsertColumns")}</td>
			
			</tr>
		</g:each>
		</tbody>
	</table>
	<div class="pagination">
		<bs:paginate total="${entityConfigurationInstanceTotal}" />
	</div>
</section>

</body>

</html>
