<html>

<head>
	<title>Sistemas </title>
	<meta name="layout" content="kickstart" />
	<g:set var="layout_nosecondarymenu" value="true"/>
</head>

<body>

	<content tag="header">
		<h3>Rutinas de mantenimiento</h3>
 	</content>
 	
 	
 	<div class="container">
 		<div class="row">
			<div class="span3">
				<ul class="nav nav-tabs nav-stacked">
					<li class="controller${params.controller != 'solicitudDeDepositos' ? " active" : ""}">
						<g:link action="cancelacionDeSolicitudes">Solicitud de Depositos</g:link></li>
					<li class=""><g:link action="cancelacionDeSolicitudes">Embarques</g:link></li>
				</ul>
				
			</div>
		</div>
		<%-- <g:render template="sucursalPeriodoDialog" model="[sucursalPeriodoCmd:sucursalPeriodoCmd]"/> --%>
 	</div>
 	
 	<content tag="footer">
 	</content>

</body>

</html>
