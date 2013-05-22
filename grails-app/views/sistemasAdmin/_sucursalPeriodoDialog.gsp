<%@ page import="com.luxsoft.replica.SucursalPeriodoCommand" %>
<div id="selectorDeSucursalPeriodoDialog" class="modal hide fade" role="dialog"
	aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">&times;</button>
		<h4>Seleccione sucursal y periodo</h4>
	</div>
	<div class="modal-body">
		<fieldset>
			<g:form class="form-horizontal" action="${accion}">
				<fieldset>
					<f:with bean="${new SucursalPeriodoCommand()}">
						
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
