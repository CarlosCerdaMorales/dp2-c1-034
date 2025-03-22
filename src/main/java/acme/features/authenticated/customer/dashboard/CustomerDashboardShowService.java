
package acme.features.authenticated.customer.dashboard;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.forms.CustomerDashboard;
import acme.realms.Customer;

@GuiService
public class CustomerDashboardShowService extends AbstractGuiService<Customer, CustomerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getRequest().getPrincipal().getActiveRealm().getId();
		CustomerDashboard customerDashboard;

		List<String> lastFiveDestinations;
		Double moneySpentInBookingsLastYear;
		String bookingsGroupedByTravelClass;
		String costsOfBookingsLastFiveYears;
		String numberOfPassengersInBookings;

		Date oneYearAgo = this.yearDelta(1);
		Date fiveYearsAgo = this.yearDelta(5);

		lastFiveDestinations = this.repository.getLastFiveDestinations(id).stream().limit(5).toList();
		moneySpentInBookingsLastYear = this.repository.getMoneySpentInBookingsLastYear(id, oneYearAgo);
		List<Object[]> bookingsGroupedByTravelClassR = this.repository.getBookingsGroupedByTravelClass(id);
		List<Object[]> costsOfBookingsLastFiveYearsR = this.repository.getCostsOfBookingsLastFiveYears(id, fiveYearsAgo);
		List<Object[]> numberOfPassengersInBookingsR = this.repository.getNumberOfPassengersInBookings(id);
		Map<String, Long> numberOfPassengersInBookingsM = this.passengerCountToMap(numberOfPassengersInBookingsR);

		bookingsGroupedByTravelClass = this.bookingCountToString(bookingsGroupedByTravelClassR);
		costsOfBookingsLastFiveYears = this.statisticsBreakdown(costsOfBookingsLastFiveYearsR);
		numberOfPassengersInBookings = this.calculateStatistics(numberOfPassengersInBookingsM);

		customerDashboard = new CustomerDashboard();
		customerDashboard.setLastFiveDestinations(lastFiveDestinations);
		customerDashboard.setMoneySpentInBookingsLastYear(moneySpentInBookingsLastYear);
		customerDashboard.setBookingsGroupedByTravelClass(bookingsGroupedByTravelClass);
		customerDashboard.setCostsOfBookingsLastFiveYears(costsOfBookingsLastFiveYears);
		customerDashboard.setNumberOfPassengersInBookings(numberOfPassengersInBookings);

		super.getBuffer().addData(customerDashboard);

	}

	@Override
	public void unbind(final CustomerDashboard customerDashboard) {
		Dataset dataset;

		dataset = super.unbindObject(customerDashboard, "lastFiveDestinations", "moneySpentInBookingsLastYear", "bookingsGroupedByTravelClass", "costsOfBookingsLastFiveYears", "numberOfPassengersInBookings");

		super.getResponse().addData(dataset);
	}

	private String bookingCountToString(final List<Object[]> par) {
		StringBuilder res = new StringBuilder();
		for (Object[] o : par) {
			String travelClass = o[0].toString();
			String count = o[1].toString();
			if (res.length() > 0)
				res.append(", ");
			res.append(travelClass).append(": ").append(count);
		}
		return res.toString();
	}

	private Date yearDelta(final Integer years) {
		return MomentHelper.deltaFromCurrentMoment(-years, ChronoUnit.YEARS);
	}

	private String statisticsBreakdown(final List<Object[]> par) {
		StringBuilder res = new StringBuilder();
		for (Object[] o : par) {
			Double avg = (Double) o[0];
			Double min = (Double) o[1];
			Double max = (Double) o[2];
			Double stddev = (Double) o[3];

			String avgFormatted = String.format("%.2f", avg);
			String minFormatted = String.format("%.2f", min);
			String maxFormatted = String.format("%.2f", max);
			String stddevFormatted = String.format("%.2f", stddev);

			if (res.length() > 0)
				res.append(", ");

			res.append("AVG: ").append(avgFormatted).append(", MIN: ").append(minFormatted).append(", MAX: ").append(maxFormatted).append(", STDDEV: ").append(stddevFormatted);
		}
		return res.toString();
	}

	private Map<String, Long> passengerCountToMap(final List<Object[]> par) {
		Map<String, Long> res = new HashMap<>();
		for (Object[] o : par) {
			String key = "Booking " + o[0].toString();
			Long value = (Long) o[1];
			res.put(key, value);
		}
		return res;
	}

	private String calculateStatistics(final Map<String, Long> data) {
		if (data == null || data.isEmpty())
			throw new IllegalArgumentException("El mapa no puede estar vacío");

		List<Long> values = new ArrayList<>(data.values());
		int n = values.size();

		Long max = Collections.max(values);
		Long min = Collections.min(values);

		double sum = 0;
		for (Long value : values)
			sum += value;
		double avg = sum / n;

		double varianceSum = 0;
		for (Long value : values)
			varianceSum += Math.pow(value - avg, 2);
		double stddev = Math.sqrt(varianceSum / n);

		String avgFormatted = String.format("%.2f", avg);
		String minFormatted = String.format("%.2f", (double) min);
		String maxFormatted = String.format("%.2f", (double) max);
		String stddevFormatted = String.format("%.2f", stddev);

		return String.format("AVG: %s, MIN: %s, MAX: %s, STDDEV: %s", avgFormatted, minFormatted, maxFormatted, stddevFormatted);
	}

}
