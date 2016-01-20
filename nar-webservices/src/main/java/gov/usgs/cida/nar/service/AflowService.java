package gov.usgs.cida.nar.service;

import gov.usgs.cida.nar.mybatis.dao.AflowDao;
import gov.usgs.cida.nar.mybatis.model.Aflow;
import java.util.List;
import org.geotools.gml3.v3_2.gco.GCO;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 *
 * @author Jordan Walker <jiwalker@usgs.gov>
 */
public class AflowService {

	public AflowService() {
		
	}
	
	public List<Aflow> request(String siteQwId, String startDate, String endDate) {
		AflowDao dao = new AflowDao();
		DateTimeFormatter dateTimeParser = ISODateTimeFormat.dateTimeParser();
		Integer startWy = null;
		Integer endWy = null;
		if (startDate != null) {
			startWy = dateTimeParser.parseDateTime(startDate).plusMonths(3).getYear();
		}
		if (endDate != null) {
			endWy = dateTimeParser.parseDateTime(endDate).plusMonths(3).getYear();
		}
		return dao.getAflow(siteQwId, startWy, endWy);
	}
}