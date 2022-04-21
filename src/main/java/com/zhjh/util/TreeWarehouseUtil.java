package com.zhjh.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 把一个list集合,里面的bean含有 parentId 转为树形式
 *
 */
public class TreeWarehouseUtil {
	
	
	/**
	 * 根据父节点的ID获取所有子节点
	 * @param list 分类表
	 * @param praentId 传入的父节点ID
	 * @return String
	 */
	public List<TreeWarehouseObject> getChildTreeWarehouseObjects(List<TreeWarehouseObject> list,int praentId) {
		List<TreeWarehouseObject> returnList = new ArrayList<TreeWarehouseObject>();
		for (Iterator<TreeWarehouseObject> iterator = list.iterator(); iterator.hasNext();) {
			TreeWarehouseObject t = (TreeWarehouseObject) iterator.next();
			// 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
			if (t.getParentId().equals(praentId)) {
				recursionFn(list, t);
				returnList.add(t);
			}
		}
		return returnList;
	}
	
	/**
	 * 递归列表
	 * @author lanyuan
	 * Email: mmm333zzz520@163.com
	 * @date 2013-12-4 下午7:27:30
	 * @param list
	 */
	private void  recursionFn(List<TreeWarehouseObject> list, TreeWarehouseObject t) {
		List<TreeWarehouseObject> childList = getChildList(list, t);// 得到子节点列表
		t.setChildren(childList);
		for (TreeWarehouseObject tChild : childList) {
			if (hasChild(list, tChild)) {// 判断是否有子节点
				//returnList.add(TreeWarehouseObject);
				Iterator<TreeWarehouseObject> it = childList.iterator();
				while (it.hasNext()) {
					TreeWarehouseObject n = (TreeWarehouseObject) it.next();
					recursionFn(list, n);
				}
			}
		}
	}
	
	// 得到子节点列表
	private List<TreeWarehouseObject> getChildList(List<TreeWarehouseObject> list, TreeWarehouseObject t) {
		
		List<TreeWarehouseObject> tlist = new ArrayList<TreeWarehouseObject>();
		Iterator<TreeWarehouseObject> it = list.iterator();
		while (it.hasNext()) {
			TreeWarehouseObject n = (TreeWarehouseObject) it.next();
			if (n.getParentId().equals(t.getId())) {
				tlist.add(n);
			}
		}
		return tlist;
	} 
	List<TreeWarehouseObject> returnList = new ArrayList<TreeWarehouseObject>();
	/**
     * 根据父节点的ID获取所有子节点
     * @param list 分类表
     * @param typeId 传入的父节点ID
     * @param prefix 子节点前缀
     */
    public List<TreeWarehouseObject> getChildTreeWarehouseObjects(List<TreeWarehouseObject> list, int typeId,String prefix){
        if(list == null) return null;
        for (Iterator<TreeWarehouseObject> iterator = list.iterator(); iterator.hasNext();) {
            TreeWarehouseObject node = (TreeWarehouseObject) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (node.getParentId().equals(typeId)) {
                recursionFn(list, node,prefix);
            }
            // 二、遍历所有的父节点下的所有子节点
            /*if (node.getParentId()==0) {
                recursionFn(list, node);
            }*/
        }
        return returnList;
    }
     
    private void recursionFn(List<TreeWarehouseObject> list, TreeWarehouseObject node,String p) {
        List<TreeWarehouseObject> childList = getChildList(list, node);// 得到子节点列表
        if (hasChild(list, node)) {// 判断是否有子节点
            returnList.add(node);
            Iterator<TreeWarehouseObject> it = childList.iterator();
            while (it.hasNext()) {
                TreeWarehouseObject n = (TreeWarehouseObject) it.next();
                n.setName(p+n.getName());
                recursionFn(list, n,p+p);
            }
        } else {
            returnList.add(node);
        }
    }

	// 判断是否有子节点
	private boolean hasChild(List<TreeWarehouseObject> list, TreeWarehouseObject t) {
		return getChildList(list, t).size() > 0 ? true : false;
	}
	
	// 本地模拟数据测试
	public void main(String[] args) {
		/*long start = System.currentTimeMillis();
		List<TreeWarehouseObject> TreeWarehouseObjectList = new ArrayList<TreeWarehouseObject>();
		
		TreeWarehouseObjectUtil mt = new TreeWarehouseObjectUtil();
		List<TreeWarehouseObject> ns=mt.getChildTreeWarehouseObjects(TreeWarehouseObjectList,0);
		for (TreeWarehouseObject m : ns) {
			System.out.println(m.getName());
			System.out.println(m.getChildren());
		}
		long end = System.currentTimeMillis();
		System.out.println("用时:" + (end - start) + "ms");*/
	}
	
}
