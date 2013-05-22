<div class="">
	<ul class="nav nav-tabs" data-role="listview" data-split-icon="gear" data-filter="true">
		<%-- 
		<g:each status="i" var="c" in="${grailsApplication.controllerClasses.sort { it.logicalPropertyName } }">
			<li class="controller${params.controller == c.logicalPropertyName ? " active" : ""}">
				<g:link controller="${c.logicalPropertyName}" action="index">
					<g:message code="${c.logicalPropertyName}.label" default="${c.logicalPropertyName.capitalize()}"/>
				</g:link>
			</li>
		</g:each>
		--%>
		<li>
			<g:link controller="home" >
				Inicio
			</g:link>
		</li>
		<li>
			<g:link controller="replica" >
				Replica
			</g:link>
		</li>
		<li>
			<g:link controller="quartz" >
				Quartz
			</g:link>
		</li>
		<li>
			<g:link controller="sistemasAdmin" >
				Sistemas
			</g:link>
		</li>
	</ul>
</div>
