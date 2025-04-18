
package acme.features.customer.dashboard;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.LongSummaryStatistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;
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
		CustomerDashboard customerDashboard = new CustomerDashboard();
		List<Booking> bookings = this.repository.findAllBookingsFromCustomer(id);

		if (!bookings.isEmpty()) {
			Pageable topFive = PageRequest.of(0, 5);
			Date oneYearAgo = this.yearDelta(1);
			Date fiveYearsAgo = this.yearDelta(5);

			Collection<Flight> flights = this.repository.findLastFiveFlightsByCustomerId(id, topFive);
			List<Booking> recentBookings = this.repository.findBookingsFromLastYear(id, oneYearAgo);
			List<Object[]> groupByTC = this.repository.countBookingsByTravelClass(id);
			List<Booking> bookingsLastFive = this.repository.findBookingsFromLastFiveYears(id, fiveYearsAgo);
			List<Booking> bookingsFromCustomer = this.repository.findAllBookingsFromCustomer(id);

			List<String> lastFiveDestinations = flights.stream().map(f -> f.getArrival().getCity()).toList();
			Double totalSpent = recentBookings.stream().map(Booking::bookingPrice).filter(b -> b != null).mapToDouble(Money::getAmount).sum();
			String bookingsByTravelClass = this.bookingCountToString(groupByTC);
			DoubleSummaryStatistics stats4 = bookingsLastFive.stream().map(Booking::bookingPrice).filter(b -> b != null).mapToDouble(m -> m.getAmount()).summaryStatistics();
			List<Long> passengerCounts = bookingsFromCustomer.stream().map(b -> this.repository.getPassengersNumberFromBooking(b.getId())).toList();
			LongSummaryStatistics stats5 = passengerCounts.stream().mapToLong(Long::longValue).summaryStatistics();

			customerDashboard.setLastFiveDestinations(lastFiveDestinations);
			customerDashboard.setMoneySpentInBookingsLastYear(totalSpent);
			customerDashboard.setBookingsGroupedByTravelClass(bookingsByTravelClass);
			customerDashboard.setCostsOfBookingsLastFiveYears(this.returnStringForDouble(stats4, bookingsLastFive));
			customerDashboard.setNumberOfPassengersInBookings(this.returnStringForLong(stats5, passengerCounts));
		}

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

	private String returnStringForDouble(final DoubleSummaryStatistics stats, final List<Booking> bookings) {
		double avg = stats.getAverage();
		double variance = bookings.stream().map(Booking::bookingPrice).mapToDouble(m -> Math.pow(m.getAmount() - avg, 2)).average().orElse(0.);
		double stdev = Math.sqrt(variance);
		return String.format("Total: %d, AVG: %.2f, MIN: %.2f, MAX: %.2f, STDDEV: %.2f", stats.getCount(), stats.getAverage(), stats.getMin(), stats.getMax(), stdev);
	}

	private String returnStringForLong(final LongSummaryStatistics stats, final List<Long> passengerCounts) {
		double avg = stats.getAverage();
		double variance = passengerCounts.stream().mapToDouble(count -> Math.pow(count - avg, 2)).average().orElse(0.);
		double stdev = Math.sqrt(variance);
		return String.format("Total: %d, AVG: %.2f, MIN: %d, MAX: %d, STDDEV: %.2f", stats.getCount(), stats.getAverage(), stats.getMin(), stats.getMax(), stdev);
	}

}
