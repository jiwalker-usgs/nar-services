package gov.usgs.cida.nar.transform;

import gov.usgs.cida.nude.resultset.ResultSetUtils;
import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.column.ColumnGrouping;
import gov.usgs.cida.nude.column.SimpleColumn;
import gov.usgs.cida.nude.filter.FilterStageBuilder;
import gov.usgs.cida.nude.filter.NudeFilterBuilder;
import gov.usgs.cida.nude.resultset.inmemory.IteratorWrappingResultSet;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class WaterYearTransformTest {
	private static final Logger log = LoggerFactory.getLogger(WaterYearTransform.class);
	
	public WaterYearTransformTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
		waterYearColumn = new SimpleColumn("DATE");
		
		inputColumns = new ColumnGrouping(Arrays.asList(new Column[] {
			new SimpleColumn("SITE_QW_ID"),
			new SimpleColumn("CONSTIT"),
			new SimpleColumn("MODTYPE"),
			new SimpleColumn("DATE"),
			new SimpleColumn("procedure")
		}));
		
		inputSampleDataset = ResultSetUtils.createTableRows(inputColumns, new String[][] {
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1979-12-31T18:00:00.000-06:00","1.143605948E7"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1980-12-31T18:00:00.000-06:00","4585894.2"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1981-12-31T18:00:00.000-06:00","7674767.59"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1982-12-31T18:00:00.000-06:00","8882042.96"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1983-12-31T18:00:00.000-06:00","1.325656857E7"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1984-12-31T18:00:00.000-06:00","5864574.53"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1985-12-31T18:00:00.000-06:00","8600366.26"}
		});
		
		expectedSampleDataset = ResultSetUtils.createTableRows(inputColumns, new String[][] {
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1980","1.143605948E7"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1981","4585894.2"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1982","7674767.59"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1983","8882042.96"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1984","1.325656857E7"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1985","5864574.53"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1986","8600366.26"}
		});
		
		inputWaterYearDataset = ResultSetUtils.createTableRows(inputColumns, new String[][] {
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1979-01-01T00:00:00.000Z","1.325656857E7"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1979-09-30T23:59:59.000Z","1.143605948E7"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1979-10-01T00:00:00.000Z","4585894.2"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1979-12-31T00:00:00.000Z","7674767.59"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1980-01-01T18:00:00.000Z","8882042.96"}
		});
		
		expectedWaterYearDataset = ResultSetUtils.createTableRows(inputColumns, new String[][] {
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1979","1.325656857E7"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1979","1.143605948E7"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1980","4585894.2"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1980","7674767.59"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","annual_flow","1980","8882042.96"}
		});
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of transform method, of class WaterYearTransform.
	 */
	@Test
	public void testTransform() {
		log.debug("transform");
		TableRow row = new TableRow(waterYearColumn, "1979-12-31T18:00:00.000-06:00");
		WaterYearTransform instance = new WaterYearTransform(waterYearColumn);
		String expResult = "1980";
		String result = instance.transform(row);
		assertEquals(expResult, result);
	}

	/**
	 * Test of transform method, of class WaterYearTransform.
	 * @throws java.sql.SQLException
	 */
	@Test
	public void testSampleDataset() throws SQLException {
		log.debug("sampleDataset");
		ResultSet expected = new IteratorWrappingResultSet(expectedSampleDataset.iterator());
		ResultSet in = new IteratorWrappingResultSet(inputSampleDataset.iterator());
		ResultSet actual = new NudeFilterBuilder(inputColumns)
				.addFilterStage(new FilterStageBuilder(inputColumns)
						.addTransform(waterYearColumn, new WaterYearTransform(waterYearColumn))
						.buildFilterStage())
				.buildFilter().filter(in);
		assertTrue(ResultSetUtils.checkEqualRows(expected, actual));
	}
	
	/**
	 * Test of transform method, of class WaterYearTransform.
	 * @throws java.sql.SQLException
	 */
	@Test
	public void testActualWaterYearThreshold() throws SQLException {
		log.debug("waterYearThreshold");
		ResultSet expected = new IteratorWrappingResultSet(expectedWaterYearDataset.iterator());
		ResultSet in = new IteratorWrappingResultSet(inputWaterYearDataset.iterator());
		ResultSet actual = new NudeFilterBuilder(inputColumns)
				.addFilterStage(new FilterStageBuilder(inputColumns)
						.addTransform(waterYearColumn, new WaterYearTransform(waterYearColumn))
						.buildFilterStage())
				.buildFilter().filter(in);
		assertTrue(ResultSetUtils.checkEqualRows(expected, actual));
	}
	
	public static Column waterYearColumn = null;
	
	public static ColumnGrouping inputColumns = null;
	public static Iterable<TableRow> inputSampleDataset = null;
	public static Iterable<TableRow> expectedSampleDataset = null;
	
	public static Iterable<TableRow> inputWaterYearDataset = null;
	public static Iterable<TableRow> expectedWaterYearDataset = null;
}
