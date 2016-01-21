package gov.usgs.cida.nar.mybatis.dao;

import gov.usgs.cida.nar.mybatis.MyBatisConnectionFactory;
import gov.usgs.cida.nar.mybatis.model.Mloads;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jordan Walker <jiwalker@usgs.gov>
 */
public class MloadsDao {

	private static final Logger log = LoggerFactory.getLogger(MloadsDao.class);
	
	private final SqlSessionFactory sqlSessionFactory;

	public MloadsDao() {
		this.sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
	}

	public MloadsDao(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	public List<Mloads> getMloads(String siteQwId, String constit, List<String> modtypeExcludes,
			Integer startWy, Integer endWy) {
		List<Mloads> result = null;
		
		Map<String, Object> params = new HashMap<>(11);
		params.put("siteQwId", siteQwId);
		params.put("constit", constit);
		params.put("modtypeExcludes", modtypeExcludes);
		params.put("startWy", startWy);
		params.put("endWy", endWy);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList(MyBatisConnectionFactory.QUERY_PACKAGE + ".MloadsMapper.getMloads", params);
		}
		
		return result;
	}
	
}
