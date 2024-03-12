package de.uni_hamburg.isa.cityguard.cityguardserver.processing;


import com.uber.h3core.LengthUnit;
import com.uber.h3core.util.LatLng;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.AuthenticationRepository;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.ReportRepository;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.UserRepository;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.Category;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.CgUser;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SpamDetectionService {

	private final ReportRepository reportRepository;
	private final SpatialIndexingService spatialIndexingService;
	private final UserRepository userRepository;
	private final AuthenticationRepository authenticationRepository;

	private List<Report> getSimilarReports(Report report){
		CgUser user=report.getUser();
		Category category=report.getCategory();
		LocalDateTime dateTimeEntered =report.getDateTime();
		LocalDateTime dateTimeAltered=dateTimeEntered.minusMinutes(category.getSpamDetectionFrequencyInMinutes());
		return reportRepository.findSimilarReportsFromSameUser(user,category,dateTimeAltered,dateTimeEntered);

	}


	private List<Report> getSimilarReportsInTheRadius(Report report, List<Report> similarReports){
		List<Report> similarReportsInTheRadius = new ArrayList<>();
		Category cat= report.getCategory();
		LatLng newReportLatLng= new LatLng(report.getLatitude(),report.getLongitude());
		for (Report r:similarReports) {
			LatLng latLng= new LatLng(r.getLatitude(), r.getLongitude());
			double agDistance=spatialIndexingService.distance(latLng,newReportLatLng, LengthUnit.m);
			if (agDistance< cat.getAggregationRadiusMeters()){
				similarReportsInTheRadius.add(r);
			}

		}
		return  similarReportsInTheRadius;

	}


	public void handleSimilarReportsInTheRadius(Report report){
		List<Report> similarReports= getSimilarReports(report);
		List<Report> similarReportsInTheRadius= getSimilarReportsInTheRadius(report,similarReports);
		for (Report r:similarReportsInTheRadius)
		{
			r.setSpam(true);
			if (Objects.equals(r.getId(), report.getId())) {
				r.setSpam(false);
			}
		}

		List<Report> userReports = reportRepository.findByUser(report.getUser()).stream()
				.filter(r -> r.getDateTime().isAfter(report.getDateTime().minusDays(30))).toList();
		int spam_amount = userReports.stream().filter(Report::isSpam).toList().size();
		if (spam_amount >= report.getCategory().getSpamAlertThreshold()) {
			reportRepository.deleteAll(reportRepository.findByUser(report.getUser()));
		}
	}






}
