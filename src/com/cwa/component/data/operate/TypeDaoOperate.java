package com.cwa.component.data.operate;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import baseice.basedao.IEntity;

import com.cwa.component.data.IEntityDao;

/**
 * 根据操作类型调用dao
 * 
 * @author Administrator
 * 
 */
public class TypeDaoOperate implements IDaoOperate, Runnable {
	protected static final Logger logger = LoggerFactory.getLogger(TypeDaoOperate.class);
	private String key;// 相同key才merge
	private IEntityDao entityDao;// dao
	private List<? extends IEntity> entitys;// 实体
	private DaoOperateType doType;// 操作类型
	private IDaoCallBack daoCallBack;// 回调接口
	private Object attach;

	public TypeDaoOperate(String key) {
		this.key = key;
	}

	@Override
	public void merge(IDaoOperate newBc) {
		if (!(newBc instanceof TypeDaoOperate)) {
			return;
		}
		TypeDaoOperate newDO = (TypeDaoOperate) newBc;
		if (!canMerge(newDO)) {
			return;
		}
		if (this.doType == null) {
			this.doType = newDO.doType;
			this.entityDao = newDO.entityDao;
			this.entitys = newDO.entitys;
			logger.error("[DaoOperate replace!] entitys=" + entitys);
			return;
		}
		if (this.doType == DaoOperateType.Insert) {
			if (newDO.doType == DaoOperateType.Insert) {
				// 错误的
				logger.error("[DaoTask Error Insert] entity=" + this.entitys);
			} else if (newDO.doType == DaoOperateType.Update) {
				// 更新entity,还是insert
				// 这里不操作
			} else if (newDO.doType == DaoOperateType.Remove) {
				// 移除不操作
				this.doType = null;
			}
		} else if (this.doType == DaoOperateType.Update) {
			if (newDO.doType == DaoOperateType.Insert) {
				// 错误的
				logger.error("[DaoTask Error Insert] entity=" + this.entitys);
			} else if (newDO.doType == DaoOperateType.Update) {
				// 更新entity,还是update
				// 这里不操作
			} else if (newDO.doType == DaoOperateType.Remove) {
				// 直接删除
				this.doType = DaoOperateType.Remove;
			}
		} else if (this.doType == DaoOperateType.Remove) {
			if (newDO.doType == DaoOperateType.Insert) {
				// 相当于直接更新操作
				this.entitys = newDO.entitys;
				this.doType = DaoOperateType.Update;
			} else if (newDO.doType == DaoOperateType.Update) {
				// 不能更新
				logger.error("[DaoTask Error Update] entity=" + this.entitys);
			} else if (newDO.doType == DaoOperateType.Remove) {
				// 重复删除
				logger.error("[DaoTask Error Delete] entity=" + this.entitys);
			}
		}
	}

	@Override
	public Object execute() {
		Object resulte = null;
		if (!canExecute()) {
			return resulte;
		}
		try {
			if (doType == DaoOperateType.Insert) {
				IEntity entity = entitys.get(0);
				entityDao.insertEntity(entity, attach);
				if (logger.isInfoEnabled()) {
					logger.info("[Dao Insert over] entity=" + entity);
				}
			} else if (doType == DaoOperateType.Update) {
				IEntity entity = entitys.get(0);
				entityDao.updateEntity(entity, attach);
				if (logger.isInfoEnabled()) {
					logger.info("[Dao Update over] entity=" + entity);
				}
			} else if (doType == DaoOperateType.Remove) {
				IEntity entity = entitys.get(0);
				entityDao.removeEntity(entity, attach);
				if (logger.isInfoEnabled()) {
					logger.info("[Dao Delete over] entity=" + entity);
				}
			} else if (doType == DaoOperateType.BatchInsert) {
				entityDao.batchInsertEntity(entitys, attach);
				if (logger.isInfoEnabled()) {
					logger.info("[Dao BatchInsert over] entitys=" + entitys);
				}
			} else if (doType == DaoOperateType.BatchUpdate) {
				entityDao.batchUpdateEntity(entitys, attach);
				if (logger.isInfoEnabled()) {
					logger.info("[Dao BatchUpdate over] entitys=" + entitys);
				}
			} else if (doType == DaoOperateType.BatchRemove) {
				entityDao.batchRemoveEntity(entitys, attach);
				if (logger.isInfoEnabled()) {
					logger.info("[Dao BatchRemove over] entitys=" + entitys);
				}
			}
			if (daoCallBack != null) {
				// 回调接口
				daoCallBack.executeOver(resulte);
			}
		} catch (Exception e) {
			logger.error("", e);
			if (daoCallBack != null) {
				// 回调接口
				daoCallBack.excuteException(e);
			}
		}
		return resulte;
	}

	@Override
	public boolean canExecute() {
		return doType != null && entitys != null && entityDao != null && !entitys.isEmpty();
	}

	@Override
	public boolean canMerge() {
		return canMerge(this);
	}

	private boolean canMerge(TypeDaoOperate daoOperate) {
		if (daoCallBack != null) {
			// 回调不为空就不能合并
			return false;
		}
		if (entitys == null || entitys.isEmpty()) {
			return false;
		}
		return (daoOperate.doType.value / 10) <= 0;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void run() {
		execute();
	}

	public void setKey(String key) {
		this.key = key;
	}

	public enum DaoOperateType {
		Insert(1), // 插入
		Update(2), // 更新
		Remove(3), // 移除
		BatchInsert(11), // 批量插入
		BatchUpdate(12), // 批量更新
		BatchRemove(13), // 批量移除
		;
		private int value;

		DaoOperateType(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}
	}

	// ----------------------------------------------
	public void setDaoCallBack(IDaoCallBack daoCallBack) {
		this.daoCallBack = daoCallBack;
	}

	public void setEntityDao(IEntityDao entityDao) {
		this.entityDao = entityDao;
	}

	public void setEntitys(List<? extends IEntity> entitys) {
		this.entitys = entitys;
	}

	public void setDoType(DaoOperateType doType) {
		this.doType = doType;
	}

	public void setAttach(Object attach) {
		this.attach = attach;
	}
}
