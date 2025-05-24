<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:print code="technician.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.number-maintenance-status.completed"/>
		</th>
		<td>
			<acme:print value="${statusCompleted}"/>
		</td>
	</tr>
		<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.number-maintenance-status.pending"/>
		</th>
		<td>
			<acme:print value="${statusPending}"/>
		</td>
	</tr>
		<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.number-maintenance-status.progress"/>
		</th>
		<td>
			<acme:print value="${statusPending}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.nearest-next-inspection"/>
		</th>
		<td>
			<acme:print value="${nearestNextInspection}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.most-tasks-aircarfts"/>
		</th>
		<td>
			<acme:print value="${mostTasksAircrafts}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.cost-statistics.avg"/>
		</th>
		<td>
			<acme:print value="${avgCost}"/>
		</td>
	</tr>
		<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.cost-statistics.max"/>
		</th>
		<td>
			<acme:print value="${maxCost}"/>
		</td>
	</tr>
		<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.cost-statistics.min"/>
		</th>
		<td>
			<acme:print value="${minCost}"/>
		</td>
	</tr>
		<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.cost-statistics.dev"/>
		</th>
		<td>
			<acme:print value="${devCost}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.duration-statistics.avg"/>
		</th>
		<td>
			<acme:print value="${avgDur}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.duration-statistics.max"/>
		</th>
		<td>
			<acme:print value="${maxDur}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.duration-statistics.min"/>
		</th>
		<td>
			<acme:print value="${minDur}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.duration-statistics.dev"/>
		</th>
		<td>
			<acme:print value="${devDur}"/>
		</td>
	</tr>	
</table>
<acme:return/>