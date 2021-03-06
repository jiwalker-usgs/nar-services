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
 * @author thongsav
 */
public class ToDayDateTransformTest {
	private static final Logger log = LoggerFactory.getLogger(ToDayDateTransformTest.class);
	
	public ToDayDateTransformTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
		dateColumn = new SimpleColumn("DATE");
		
		inputColumns = new ColumnGrouping(Arrays.asList(new Column[] {
			new SimpleColumn("SITE_QW_ID"),
			new SimpleColumn("CONSTIT"),
			new SimpleColumn("MODTYPE"),
			new SimpleColumn("DATE"),
			new SimpleColumn("procedure")
		}));
		
		inputSampleDataset = ResultSetUtils.createTableRows(inputColumns, new String[][] {
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","daily_flow","1979-12-31T18:00:00.000-06:00","1.143605948E7"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","daily_flow","1980-02-03T18:00:00.000-00:00","4585894.2"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","daily_flow","1980-02-04T18:00:00.000-00:00","7674767.59"}
		});
		
		expectedSampleDataset = ResultSetUtils.createTableRows(inputColumns, new String[][] {
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","daily_flow","1980-01-01","1.143605948E7"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","daily_flow","1980-02-03","4585894.2"},
			new String[] {"01646580","http://cida.usgs.gov/def/NAR/property/Q","daily_flow","1980-02-04","7674767.59"}
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
	 * Test of transform method, of class ToDayDateTransform.
	 */
	@Test
	public void testTransform() {
		log.debug("transform");
		TableRow row = new TableRow(dateColumn, "1979-12-31T18:00:00.000-06:00");
		ToDayDateTransform instance = new ToDayDateTransform(dateColumn);
		String expResult = "1980-01-01";
		String result = instance.transform(row);
		assertEquals(expResult, result);
	}

	/**
	 * Test of transform method, of class ToDayDateTransform.
	 */
	@Test
	public void testSampleDataset() throws SQLException {
		log.debug("sampleDataset");
		ResultSet expected = new IteratorWrappingResultSet(expectedSampleDataset.iterator());
		ResultSet in = new IteratorWrappingResultSet(inputSampleDataset.iterator());
		ResultSet actual = new NudeFilterBuilder(inputColumns)
				.addFilterStage(new FilterStageBuilder(inputColumns)
						.addTransform(dateColumn, new ToDayDateTransform(dateColumn))
						.buildFilterStage())
				.buildFilter().filter(in);
		assertTrue(ResultSetUtils.checkEqualRows(expected, actual));
	}
	
	public static Column dateColumn = null;
	
	public static ColumnGrouping inputColumns = null;
	public static Iterable<TableRow> inputSampleDataset = null;
	public static Iterable<TableRow> expectedSampleDataset = null;
}
