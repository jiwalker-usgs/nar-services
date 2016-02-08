package gov.usgs.cida.nar.service;

import gov.usgs.cida.nar.domain.TimeSeriesAvailability;
import gov.usgs.cida.nar.domain.TimeSeriesCategory;
import gov.usgs.cida.nar.domain.TimeStepDensity;
import gov.usgs.cida.nar.mybatis.dao.MflowDao;
import gov.usgs.cida.nar.mybatis.model.Mflow;
import gov.usgs.cida.nar.mybatis.model.WaterYearAndMonthInterval;
import gov.usgs.cida.nar.mybatis.model.WaterYearInterval;
import gov.usgs.cida.nar.util.DateUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.joda.time.Interval;
import org.joda.time.LocalDateTime;

/**
 *
 * @author Jordan Walker <jiwalker@usgs.gov>
 */
public class MflowService implements NARService<Mflow> {

	private MflowDao dao;
	
	private List<String> siteQwId;
	private String startDate;
	private String endDate;
	
	public MflowService() {
		this(new MflowDao());
	}
	
	public MflowService(MflowDao dao) {
		this.dao = dao;
		this.siteQwId = null;
		this.startDate = null;
		this.endDate = null;
	}
	
	public List<Mflow> request(List<String> siteQwId, String startDate, String endDate) {
		Integer startWy = DateUtil.getWaterYear(startDate);
		Integer endWy = DateUtil.getWaterYear(endDate);
		return dao.getMflow(siteQwId, startWy, endWy);
	}

	@Override
	public List request() {
		return request(siteQwId, startDate, endDate);
	}

	@Override
	public void setSiteQwId(List<String> siteQwId) {
		this.siteQwId = siteQwId;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public TimeStepDensity getTimeStepDensity() {
		return TimeStepDensity.MONTHLY;
	}

	@Override
	public TimeSeriesCategory getTimeSeriesCategory() {
		return TimeSeriesCategory.FLOW;
	}
	private boolean allTimeIntervalFieldsInitialized(WaterYearAndMonthInterval interval) {
		return !(
			null == interval
			|| null == interval.getStartYear()
			|| null == interval.getEndYear()
			|| null == interval.getStartMonth()
			|| null == interval.getEndMonth()
			);
	}
	
	@Override
	public List<TimeSeriesAvailability> getAvailability() {
		List<TimeSeriesAvailability> availability = new ArrayList<>();
		WaterYearAndMonthInterval timeInterval = this.dao.getAvailability(this.siteQwId.get(0));
		if(allTimeIntervalFieldsInitialized(timeInterval)) {
			LocalDateTime startTime = new LocalDateTime(timeInterval.getStartYear(),timeInterval.getStartMonth(), 1, 0, 0);
			LocalDateTime endTime = new LocalDateTime(timeInterval.getEndYear(),timeInterval.getEndMonth(), 1, 0, 0);
			TimeSeriesAvailability tsa = new TimeSeriesAvailability(
				this.getTimeSeriesCategory(),
				this.getTimeStepDensity(),
				startTime,
				endTime,
				null
			);
			availability.add(tsa);
		}
		return availability;
	}
}
