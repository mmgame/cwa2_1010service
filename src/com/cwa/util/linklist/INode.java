package com.cwa.util.linklist;

public interface INode {
	String key();										//节点key
	INode prev();										//前一个节点
	void sprev(INode node);								//设置前一个节点
	INode next();										//后一个节点
	void snext(INode node);								//设置后一个节点
}
