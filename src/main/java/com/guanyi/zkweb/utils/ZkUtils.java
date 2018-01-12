package com.guanyi.zkweb.utils;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.util.List;

public class ZkUtils {

    String zkConnection;

    CuratorFramework client;

    /**
     *
     */
    public ZkUtils(String zkConnection){
        this.zkConnection=zkConnection;
        client=getZkClient();
    }
    /**
     * 创建一个连接
     * @return
     */
    private CuratorFramework getZkClient(){
        CuratorFramework client= CuratorFrameworkFactory.builder()
                .connectString(zkConnection)                 //基于zkweb操作
                .retryPolicy(new RetryNTimes(5,1000))
                .connectionTimeoutMs(5000).build();
        client.start();
        return client;
    }

    /**
     * 创建一个节点
     * @param nodeName
     * @throws Exception
     */
    public void setNode(String nodeName) throws Exception{
        nodeName=getNodeName(nodeName);
        Stat stat=this.client.checkExists().forPath(nodeName);
        if(stat==null){
            this.client.create().creatingParentsIfNeeded().forPath(nodeName);
        }
    }

    /**
     *
     * @param nodeName
     * @return
     */
    private String getNodeName(String nodeName){
        if(StringUtils.isEmpty(nodeName)){
            throw new NullPointerException(nodeName+"nodeName is null");
        }
        if(!nodeName.startsWith("/")){
            nodeName="/"+nodeName;
        }
        if(nodeName.endsWith("/")){
            nodeName=nodeName.substring(nodeName.length()-2,nodeName.length()-1);
        }
        return nodeName;
    }

    /**
     *
     * @param nodeName
     * @param data
     * @throws Exception
     */
    public void setNode(String nodeName,String data) throws Exception{
        String chkNodeName=getNodeName(nodeName);
        Stat stat=this.client.checkExists().forPath(chkNodeName);
        if(stat==null){         //如果不存在节点则新建一个
            this.client.create().creatingParentsIfNeeded().forPath(chkNodeName,data.getBytes(Charset.forName("UTF-8")));
        }
    }

    /**
     * 获取节点上的数据
     * @param nodeName
     * @return
     * @throws Exception
     */
    public String getData(String nodeName) throws Exception{
        nodeName=getNodeName(nodeName);
        Stat stat=this.client.checkExists().forPath(nodeName);
        if(stat==null){
            return null;
        }
        byte[] data=this.client.getData().forPath(nodeName);
        if(data==null){
            return  null;
        }else{
            return new String(data,"UTF-8");
        }
    }

    /**
     *
     * @param nodeName
     * @return
     * @throws Exception
     */
    public List<String> getChildNodes(String nodeName) throws Exception{
        nodeName=getNodeName(nodeName);
        Stat stat=this.client.checkExists().forPath(nodeName);
        if(stat==null){
            return null;
        }else{
            return this.client.getChildren().forPath(nodeName);
        }
    }
    public void delNode(String nodeName) throws Exception{
        nodeName=getNodeName(nodeName);
        Stat stat=this.client.checkExists().forPath(nodeName);
        if(stat!=null){
            this.client.delete().forPath(nodeName);
        }
    }
}
