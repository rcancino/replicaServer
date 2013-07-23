
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="kickstart" />
<title>Mantenimiento a Solicitudes de deposito</title>
<g:set var="layout_nomainmenu" value="${true}" scope="request" />
<g:set var="layout_nosecondarymenu" value="${true}" scope="request" />
</head>

<body>

	<content tag="header">
		<h1>M</h1>
	</content>


	<div class="row">
		<h3>
			${cmd.sucursal.nombre }
			Del:
			${cmd.fechaInicial.format('dd/MM/yyyy') }
			al
			${cmd.fechaFinal.format('dd/MM/yyyy')}
		</h3>
	</div>

	<div class="row">
		<div class="span3 ">
			<ul class="nav nav-pills nav-stacked">
				<li class="active"><a href="#cambiarSucursalPeriodoDialog"
					data-toggle="modal">Cambiar</a></li>
				<li class=""><g:link action="cancelacionDeSolicitudes">Cancleaciones</g:link>
				</li>
			</ul>
			<div id="cambiarSucursalPeriodoDialog" class="modal hide fade"
		role="dialog" aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
			<h4>Seleccione sucursal y periodo</h4>
		</div>
		<div class="modal-body">
			<fieldset>
				<g:form class="form-horizontal" action="cambiarSucursalPeriodo">
					<fieldset>
						<f:with bean="${cmd}">
							<f:field property="fechaInicial" />
							<f:field property="fechaFinal" />
						</f:with>
						<div class="form-actions">
							<button type="submit" class="btn btn-primary">
								<i class="icon-ok icon-white"></i>
								<g:message code="default.button.submit.label" default="Ejecutar" />
							</button>
						</div>
					</fieldset>
				</g:form>
			</fieldset>
		</div>
	</div>
		</div>

		<div class="span9">
			<g:render template="solicitudesGrid" />
		</div>
	</div>



	

	<%-- 
	<div class="row">
		
	</div>
	--%>
	<content tag="footer">
 	</content>

</body>

</html>
