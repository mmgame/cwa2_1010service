package com.cwa.data.config.general;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import baseice.basedao.IKey;

import org.apache.ibatis.session.SqlSession;

import com.cwa.data.config.IFunctionZKInfoConfigDao;
import com.cwa.data.config.domain.FunctionZKInfoConfig;

public class FunctionZKInfoConfigGeneralDao implements IFunctionZKInfoConfigDao {
	protected static String namespace = FunctionZKInfoConfig.class.getCanonicalName();

	public FunctionZKInfoConfigGeneralDao() {
	}

	@Override
	public List<FunctionZKInfoConfig> selectEntity(IKey key, Object attach) {
		//DefaultKey defaultKey = (DefaultKey) key;
		//if (defaultKey.functionName.equals("selectEntityByUserId")) {
		//}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public FunctionZKInfoConfig selectEntityByGid(int gid,Object attach) {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("gid", gid);
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		List<FunctionZKInfoConfig> list = sqlSessionTemplate.selectList(namespace + ".selectByGid", paras);
		return list.isEmpty()?null : list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FunctionZKInfoConfig> selectAllEntity(Object attach) {
		Map<String, Object> paras = (Map<String, Object>) attach;
		
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		return sqlSessionTemplate.selectList(namespace + ".selectAllEntity", paras);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void insertEntity(FunctionZKInfoConfig entity, Object attach) {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("entity", entity);
		
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		sqlSessionTemplate.insert(namespace + ".insert", paras);
	}
	
    @SuppressWarnings("unchecked")
	@Override
	public void updateEntity(FunctionZKInfoConfig entity, Object attach) {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("entity", entity);
		
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		sqlSessionTemplate.update(namespace + ".update", paras);
	}

    @SuppressWarnings("unchecked")
	@Override
	public void removeEntity(FunctionZKInfoConfig entity, Object attach)  {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("entity", entity);
		
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		sqlSessionTemplate.update(namespace + ".remove", paras);
	}

    @SuppressWarnings("unchecked")
	@Override
	public void batchInsertEntity(List<FunctionZKInfoConfig> entitys, Object attach)  {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("entity_list", entitys);
		
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		sqlSessionTemplate.insert(namespace + ".batchInsert", paras);
	}
    
    @SuppressWarnings("unchecked")
	@Override
	public void batchUpdateEntity(List<FunctionZKInfoConfig> entitys, Object attach)  {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("entity_list", entitys);
		
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		sqlSessionTemplate.update(namespace + ".batchUpdate", paras);
	}

    @SuppressWarnings("unchecked")
	@Override
	public void batchRemoveEntity(List<FunctionZKInfoConfig> entitys, Object attach) {
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
