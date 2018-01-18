package com.guanyi.zkweb.services;

import com.guanyi.zkweb.services.models.KeyValue;
import com.guanyi.zkweb.services.models.Node;
import com.guanyi.zkweb.utils.ZkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ZKService {
    @Autowired
    private ZkUtils zkUtils;

    /**
     * 创建一个节点，不带数据
     * @param nodeName
     * @return
     * @throws Exception
     */
    public String createNode(String nodeName) throws Exception{
        zkUtils.setNode(nodeName);
        return nodeName;
    }

    /**
     * 创建一个节点带数据
     * @param nodeName
     * @param data
     * @throws Exception
     */
    public void createParameter(String nodeName,String data) throws Exception{
        zkUtils.setNode(nodeName,data);
    }

    /**
     * 获取节点下的所有子节点(没有数据的节点)
     * @param nodeName
     * @return
     * @throws Exception
     */
    public List<Node> getChilds(String nodeName) throws Exception{
        List<String> childrens= zkUtils.getChildNodes(nodeName);
        List<Node> childNodes=new ArrayList<>();
        if(nodeName.equals("/")){
            nodeName="";
        }
        for(String child : childrens){
            String childNode=nodeName.concat("/").concat(child);
            String data=zkUtils.getData(childNode);
            if(StringUtils.isEmpty(data)){
                Node node=new Node(child,childNode);
                childNodes.add(node);
            }
        }
        return childNodes;
    }

    /**
     * 获取节点下的kv
     * @param nodeName
     * @return
     * @throws Exception
     */
    public List<KeyValue> getKVs(String nodeName) throws Exception{
        List<String> childNodes=zkUtils.getChildNodes(nodeName);
        List<KeyValue> kvs=new ArrayList<>();
        if("/".equals(nodeName)){
            nodeName="";
        }
        for(String childNode : childNodes){
            String childNodeName=nodeName.concat("/").concat(childNode);
            String data=zkUtils.getData(childNodeName);
            if(!StringUtils.isEmpty(data)){
                KeyValue kv=new KeyValue();
                kv.setKey(childNode);
                kv.setValue(data);
                kvs.add(kv);
            }
        }
        return kvs;
    }

    /**
     * 删除一个节点或者是一个key&value
     * @param nodeName
     * @throws Exception
     */
    public void deleteNode(String nodeName) throws Exception{
        zkUtils.delNode(nodeName);
    }
    
    /**
     * 强制删除多节点
     * @param nodeNames
     * @throws Exception
     */
    public void mandatoryDeleteNodes(String[] nodeNames) throws Exception{
    	if(nodeNames != null && nodeNames.length > 0) {
    		for(String nodeName : nodeNames) {
    			zkUtils.mandatoryDeleteNode(nodeName);
    		}
    	}
    }
}
