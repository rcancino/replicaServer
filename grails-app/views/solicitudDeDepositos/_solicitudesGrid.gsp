<div class="gridPanel">
	<table class="table table-bordered">
		<thead>
			<tr>
				<g:sortableColumn property="SOL_ID" title="SOL_ID" />
			</tr>
		</thead>
		<tbody>
			<g:each in="${rows}" status="i" var="row">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
					<td>row.SOL_ID</td>
				</tr>
			</g:each>
		</tbody>
	</table>
	<div class="pagination">
		<bs:paginate total="${rowsTotal}" />
	</div>

</div>