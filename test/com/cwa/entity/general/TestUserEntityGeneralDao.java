package com.cwa.entity.general;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import baseice.basedao.IKey;

import com.cwa.entity.ITestUserEntityDao;
import com.cwa.entity.domain.TestUserEntity;

public class TestUserEntityGeneralDao implements ITestUserEntityDao {
	protected static String namespace = TestUserEntity.class.getCanonicalName();

	public TestUserEntityGeneralDao() {
	}

	@Override
	public List<TestUserEntity> selectEntity(IKey key, Object attach) {
		// DefaultKey defaultKey = (DefaultKey) key;
		// if (defaultKey.functionName.equals("selectEntityByUserId")) {
		// }
		return null;
	}

	public List<TestUserEntity> selectEntityByUserId(long userId, Object attach) {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("userId", userId);
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		return sqlSessionTemplate.selectList(namespace + ".selectByUserId", paras);
	}
	
	@Override
	public List<TestUserEntity> selectAllEntity(Object attach) {
		Map<String, Object> paras = (Map<String, Object>) attach;
		
		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		return sqlSessionTemplate.selectList(namespace + ".selectAllEntity", paras);
	}
	
	@Override
	public void insertEntity(TestUserEntity entity, Object attach) {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("entity", entity);

		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		sqlSessionTemplate.insert(namespace + ".insert", paras);

		paras.remove("entity");
	}

	@Override
	public void updateEntity(TestUserEntity entity, Object attach)  {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("entity", entity);

		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		sqlSessionTemplate.update(namespace + ".update", paras);
		
		paras.remove("entity");
	}

	@Override
	public void removeEntity(TestUserEntity entity, Object attach) {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("entity", entity);

		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		sqlSessionTemplate.update(namespace + ".remove", paras);
		
		paras.remove("entity");
	}

	@Override
	public void batchInsertEntity(List<TestUserEntity> entitys, Object attach){
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("entity_list", entitys);

		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		sqlSessionTemplate.insert(namespace + ".batchInsert", paras);
		
		paras.remove("entity_list");
	}

	@Override
	public void batchUpdateEntity(List<TestUserEntity> entitys, Object attach) {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("entity_list", entitys);

		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		sqlSessionTemplate.update(namespace + ".batchUpdate", paras);
		
		paras.remove("entity_list");
	}

	@Override
	public void batchRemoveEntity(List<TestUserEntity> entitys, Object attach)  {
		Map<String, Object> paras = (Map<String, Object>) attach;
		paras.put("entity_list", entitys);

		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		sqlSessionTemplate.update(namespace + ".batchRemove", paras);
		
		paras.remove("entity_list");
	}

	@Override
	public void createTable(Object attach) {
		Map<String, Object> paras = (Map<String, Object>) attach;

		SqlSession sqlSessionTemplate = (SqlSession) paras.get("sql_session");
		sqlSessionTemplate.insert(namespace + ".createTable", paras);
	}

	@Override
	public List<String> selectAllTableName(Object attach) {
		// Map<String, Object> paras = new HashMap<String, Object>();
		SqlSession sqlSessionTemplate = (SqlSession) attach;
		return sqlSessionTemplate.selectList(namespace + ".selectAllTableName", null);
	}
}
