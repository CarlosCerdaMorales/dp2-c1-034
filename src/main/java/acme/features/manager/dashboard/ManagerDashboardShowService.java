
package acme.features.manager.dashboard;

import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.FlightStatus;
import acme.entities.leg.Leg;
import acme.forms.ManagerDashboard;
import acme.forms.statistics.StatsManager;
import acme.realms.Manager;

@GuiService
public class ManagerDashboardShowService extends AbstractGuiService<Manager, ManagerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		ManagerDashboard managerDashboard;
		Integer rankingManagerByExperience;
		Integer yearsToRetire;
		Double ratioOnTimeLegs;
		Double ratioDelayedLegs;
		Airport mostPopular;
		Airport leastPopular;
		Map<FlightStatus, Long> numberLegsByStatus;
		StatsManager statistics;

		int id = super.getRequest().getPrincipal().getActiveRealm().getId();
		Date currentDate = MomentHelper.getCurrentMoment();
		yearsToRetire = this.yearsToRetire(this.repository.findManagerBirthday(id), currentDate);
		ratioOnTimeLegs = this.ratiusStatusLegs("ON_TIME", id);
		ratioDelayedLegs = this.ratiusStatusLegs("DELAYED", id);
		rankingManagerByExperience = this.repository.findManagersIndexSortedByYearsOfExperience(id);
		mostPopular = this.mostPopularAirport(id);
		leastPopular = this.leastPopularAirport(id);
		numberLegsByStatus = this.getLegsCountGroupedByStatus(id);
		statistics = this.repository.findStatisticsByManagerId(id);

		managerDashboard = new ManagerDashboard();
		managerDashboard.setRanking(rankingManagerByExperience);
		managerDashboard.setYearsToRetire(yearsToRetire);
		managerDashboard.setRatioOnTimeLegs(ratioOnTimeLegs);
		managerDashboard.setRatioDelayedLegs(ratioDelayedLegs);
		managerDashboard.setMostPopularAirport(mostPopular);
		managerDashboard.setLeastPopularAirport(leastPopular);
		managerDashboard.setStatistcsAboutFlights(statistics);
		managerDashboard.setNumberOfLegsBasedOnStatus(numberLegsByStatus);

		super.getBuffer().addData(managerDashboard);

	}

	@Override
	public void unbind(final ManagerDashboard managerDashboard) {
		Dataset dataset;

		dataset = super.unbindObject(managerDashboard, //
			"rankingManagerByExperience", "yearsToRetire", // 
			"ratioOnTimeLegs", "ratioDelayedLegs");
		dataset.put("mostPopular", managerDashboard.getMostPopularAirport() == null ? null : managerDashboard.getMostPopularAirport().getAirportName());
		dataset.put("leastPopular", managerDashboard.getLeastPopularAirport() == null ? null : managerDashboard.getLeastPopularAirport().getAirportName());

		Map<FlightStatus, Long> statuses = managerDashboard.getNumberOfLegsBasedOnStatus();
		dataset.put("numberOfOnTime", statuses.get(FlightStatus.ON_TIME));
		dataset.put("numberOfDelayed", statuses.get(FlightStatus.DELAYED));
		dataset.put("numberOfCancelled", statuses.get(FlightStatus.CANCELLED));
		dataset.put("numberOfLanded", statuses.get(FlightStatus.LANDED));

		dataset.put("costAverageFlights", managerDashboard.getStatistcsAboutFlights().getAverage());
		dataset.put("costMinFlights", managerDashboard.getStatistcsAboutFlights().getMinimum());
		dataset.put("costMaxFlights", managerDashboard.getStatistcsAboutFlights().getMaximum());
		dataset.put("costDeviationFlights", managerDashboard.getStatistcsAboutFlights().getStandardDeviation());

		super.getResponse().addData(dataset);
	}

	private Integer yearsToRetire(final Date birthday, final Date currentDate) {

		Calendar calendar = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();

		//Gets year of birthday
		calendar.setTime(birthday);
		int birthYear = calendar.get(Calendar.YEAR);
		int retirementYear = birthYear + 65;

		//Gets current year
		calendar2.setTime(currentDate);
		int currentYear = calendar2.get(Calendar.YEAR);

		return retirementYear - currentYear;

	}

	private Airport mostPopularAirport(final int id) {
		List<Flight> flights = this.repository.findAllFlightsByManagerId(id);
		Map<Airport, Integer> airportFrequency = new HashMap<>();

		for (Flight flight : flights) {
			Airport departure = flight.getDeparture();
			Airport arrival = flight.getArrival();

			if (departure != null)
				airportFrequency.put(departure, airportFrequency.getOrDefault(departure, 0) + 1);

			if (arrival != null)
				airportFrequency.put(arrival, airportFrequency.getOrDefault(arrival, 0) + 1);
		}
		Airport mostPopular = null;
		int maxCount = Integer.MIN_VALUE;

		for (Map.Entry<Airport, Integer> entry : airportFrequency.entrySet()) {
			int count = entry.getValue();
			Airport airport = entry.getKey();

			if (count > maxCount) {
				maxCount = count;
				mostPopular = airport;
			}
		}
		return mostPopular;
	}

	private Airport leastPopularAirport(final int id) {
		List<Flight> flights = this.repository.findAllFlightsByManagerId(id);
		Map<Airport, Integer> airportFrequency = new HashMap<>();

		for (Flight flight : flights) {
			Airport departure = flight.getDeparture();
			Airport arrival = flight.getArrival();

			if (departure != null)
				airportFrequency.put(departure, airportFrequency.getOrDefault(departure, 0) + 1);

			if (arrival != null)
				airportFrequency.put(arrival, airportFrequency.getOrDefault(arrival, 0) + 1);
		}

		Airport leastPopular = null;

		int minCount = Integer.MAX_VALUE;

		for (Map.Entry<Airport, Integer> entry : airportFrequency.entrySet()) {
			int count = entry.getValue();
			Airport airport = entry.getKey();

			if (count < minCount) {
				minCount = count;
				leastPopular = airport;
			}
		}
		return leastPopular;
	}

	private Double ratiusStatusLegs(final String status, final int id) {
		List<Leg> totalLegs = this.repository.findAllLegs(id);
		List<Leg> statusLegs = this.repository.findLegsByStatus(status, id);

		if (totalLegs == null || statusLegs == null || totalLegs.size() == 0)
			return null;

		return (double) statusLegs.size() / totalLegs.size();
	}

	private Map<FlightStatus, Long> getLegsCountGroupedByStatus(final int managerId) {
		List<Object[]> rawData = this.repository.countLegsGroupedByStatus(managerId);
		Map<FlightStatus, Long> result = new EnumMap<>(FlightStatus.class);

		for (Object[] row : rawData) {
			FlightStatus status = (FlightStatus) row[0];
			Long count = (Long) row[1];
			result.put(status, count);
		}

		return result;
	}

}
