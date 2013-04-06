<%@ page import="com.luxsoft.replica.EntityConfiguration" %>



			<div class="control-group fieldcontain ${hasErrors(bean: entityConfigurationInstance, field: 'name', 'error')} ">
				<label for="name" class="control-label"><g:message code="entityConfiguration.name.label" default="Name" /></label>
				<div class="controls">
					<g:textField name="name" maxlength="50" value="${entityConfigurationInstance?.name}"/>
					<span class="help-inline">${hasErrors(bean: entityConfigurationInstance, field: 'name', 'error')}</span>
				</div>
			</div>

			<div class="control-group fieldcontain ${hasErrors(bean: entityConfigurationInstance, field: 'table', 'error')} ">
				<label for="table" class="control-label"><g:message code="entityConfiguration.table.label" default="Table" /></label>
				<div class="controls">
					<g:textField name="table" maxlength="50" value="${entityConfigurationInstance?.table}"/>
					<span class="help-inline">${hasErrors(bean: entityConfigurationInstance, field: 'table', 'error')}</span>
				</div>
			</div>

			<div class="control-group fieldcontain ${hasErrors(bean: entityConfigurationInstance, field: 'pk', 'error')} ">
				<label for="pk" class="control-label"><g:message code="entityConfiguration.pk.label" default="Pk" /></label>
				<div class="controls">
					<g:textField name="pk" maxlength="50" value="${entityConfigurationInstance?.pk}"/>
					<span class="help-inline">${hasErrors(bean: entityConfigurationInstance, field: 'pk', 'error')}</span>
				</div>
			</div>

			<div class="control-group fieldcontain ${hasErrors(bean: entityConfigurationInstance, field: 'insertSql', 'error')} ">
				<label for="insertSql" class="control-label"><g:message code="entityConfiguration.insertSql.label" default="Insert Sql" /></label>
				<div class="controls">
					<g:textArea name="insertSql" cols="40" rows="5" maxlength="5000" value="${entityConfigurationInstance?.insertSql}"/>
					<span class="help-inline">${hasErrors(bean: entityConfigurationInstance, field: 'insertSql', 'error')}</span>
				</div>
			</div>

			<div class="control-group fieldcontain ${hasErrors(bean: entityConfigurationInstance, field: 'updateSql', 'error')} required">
				<label for="updateSql" class="control-label"><g:message code="entityConfiguration.updateSql.label" default="Update Sql" /><span class="required-indicator">*</span></label>
				<div class="controls">
					<g:textArea name="updateSql" cols="40" rows="5" maxlength="5000" required="" value="${entityConfigurationInstance?.updateSql}"/>
					<span class="help-inline">${hasErrors(bean: entityConfigurationInstance, field: 'updateSql', 'error')}</span>
				</div>
			</div>

			<div class="control-group fieldcontain ${hasErrors(bean: entityConfigurationInstance, field: 'excludeInsertColumns', 'error')} ">
				<label for="excludeInsertColumns" class="control-label"><g:message code="entityConfiguration.excludeInsertColumns.label" default="Exclude Insert Columns" /></label>
				<div class="controls">
					<g:textField name="excludeInsertColumns" maxlength="250" value="${entityConfigurationInstance?.excludeInsertColumns}"/>
					<span class="help-inline">${hasErrors(bean: entityConfigurationInstance, field: 'excludeInsertColumns', 'error')}</span>
				</div>
			</div>

			<div class="control-group fieldcontain ${hasErrors(bean: entityConfigurationInstance, field: 'excludeUpdateColumns', 'error')} ">
				<label for="excludeUpdateColumns" class="control-label"><g:message code="entityConfiguration.excludeUpdateColumns.label" default="Exclude Update Columns" /></label>
				<div class="controls">
					<g:textField name="excludeUpdateColumns" maxlength="250" value="${entityConfigurationInstance?.excludeUpdateColumns}"/>
					<span class="help-inline">${hasErrors(bean: entityConfigurationInstance, field: 'excludeUpdateColumns', 'error')}</span>
				</div>
			</div>

