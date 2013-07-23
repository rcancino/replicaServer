<%--Input field para campos de tipo fecha --%>

<div class="input-prepend">
	<%-- <bs:datePicker id="${id}" name="${property}" precision="day"  value="${value}" noSelection="['': '']" />--%>
	<input class="datepicker" type="text" id="${property}" name="${property}" value="${formatDate(format:'dd/MM/yyyy',date:value)}"/>
</div>
<r:script>
$("#${property}").datepicker({
    	 dateFormat: 'dd/mm/yy',
         showOtherMonths: true,
         selectOtherMonths: true,
         showOn:'focus',
         showAnim:'fold',
         minDate:'01/10/2012',
         maxDate:'31/12/2015',
         navigationAsDateFormat:false,
         showButtonPanel: true,
         changeMonth:true,
         changeYear:true,
         closeText:'Cerrar'
      });
</r:script>