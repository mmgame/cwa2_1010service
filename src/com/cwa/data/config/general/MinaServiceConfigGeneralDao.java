package com.cwa.data.config.general;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import baseice.basedao.IKey;

import org.apache.ibatis.session.SqlSession;

import com.cwa.data.config.IMinaServiceConfigDao;
import com.cwa.data.config.domain.MinaServiceConfig;

public class MinaServiceConfigGeneralDao implements IMinaServiceConfigDao {
	protected static String namespace = MinaServiceConfig.class.getCanonicalName();

	public MinaServiceConfigGeneralDao() {
	}

	@Override
	public List<MinaServiceConfig> selectEntity(IKey key, Object attach) {
		//DefaultKey defaultKey = (DefaultKey) key;
		//if (defaultKey.functionName.equals("selectEntityByUserId")) {
		//}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MinaServiceConfig selectEntityByGidAndFtypeAndKey(int gid,int ftype,String key,Object attach) {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("gid", gid);
		paras.put("ftype", ftype);
		paras.put("key", key);
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		List<MinaServiceConfig> list = sqlSessionTemplate.selectList(namespace + ".selectByGidAndFtypeAndKey", paras);
		return list.isEmpty()?null : list.get(0);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<MinaServiceConfig> selectEntityByGidAndFtype(int gid,int ftype,Object attach) {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("gid", gid);
		paras.put("ftype", ftype);
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		return sqlSessionTemplate.selectList(namespace + ".selectByGidAndFtype", paras);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<MinaServiceConfig> selectEntityByGid(int gid,Object attach) {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("gid", gid);
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		return sqlSessionTemplate.selectList(namespace + ".selectByGid", paras);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MinaServiceConfig> selectAllEntity(Object attach) {
		Map<String, Object> paras = (Map<String, Object>) attach;
		
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		return sqlSessionTemplate.selectList(namespace + ".selectAllEntity", paras);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void insertEntity(MinaServiceConfig entity, Object attach) {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("entity", entity);
		
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		sqlSessionTemplate.insert(namespace + ".insert", paras);
	}
	
    @SuppressWarnings("unchecked")
	@Override
	public void updateEntity(MinaServiceConfig entity, Object attach) {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("entity", entity);
		
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		sqlSessionTemplate.update(namespace + ".update", paras);
	}

    @SuppressWarnings("unchecked")
	@Override
	public void removeEntity(MinaServiceConfig entity, Object attach)  {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("entity", entity);
		
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		sqlSessionTemplate.update(namespace + ".remove", paras);
	}

    @SuppressWarnings("unchecked")
	@Override
	public void batchInsertEntity(List<MinaServiceConfig> entitys, Object attach)  {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("entity_list", entitys);
		
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		sqlSessionTemplate.insert(namespace + ".batchInsert", paras);
	}
    
    @SuppressWarnings("unchecked")
	@Override
	public void batchUpdateEntity(List<MinaServiceConfig> entitys, Object attach)  {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("entity_list", entitys);
		
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		sqlSessionTemplate.update(namespace + ".batchUpdate", paras);
	}

    @SuppressWarnings("unchecked")
	@Override
	public void batchRemoveEntity(List<MinaServiceConfig> entitys, Object attach) {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("entity_list", entitys);
		
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		sqlSessionTemplate.update(namespace + ".batchRemove", paras);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void createTable(Object attach) {
		Map<String, Object> paras = (Map<String, Object>) attach;
		
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		sqlSessionTemplate.insert(namespace + ".createTable", paras);
	}

	@Override
	public List<String> selectAllTableName(Object attach) {
		Map<String, Object> paras = new HashMap<String, Object>();
		
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		return sqlSessionTemplate.selectList(namespace + ".selectAllTableName", paras);
	}
}
