package com.guanyi.zkweb.controller;

import com.guanyi.zkweb.services.ZKService;
import com.guanyi.zkweb.services.models.KeyValue;
import com.guanyi.zkweb.services.models.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    ZKService zkService;
    Logger logger= LoggerFactory.getLogger(this.getClass().getName());
    @RequestMapping("/index")
    public ModelAndView index(String zkPath){
        ModelAndView view=new ModelAndView("/home");
        if(StringUtils.isEmpty(zkPath)||"/".equals(zkPath)){
            view.addObject("parentNode","");
            zkPath = "/";
        }else{
            String parentNode=zkPath.substring(0,zkPath.lastIndexOf("/"));
            view.addObject("parentNode",parentNode);
        }
        view.addObject("zkPath",zkPath);
        return view;
    }

    /**
     * 获取zk节点数据
     * @param zkPath
     * @return
     */
    @RequestMapping("/getChilds")
    public @ResponseBody ResponseMessage getChilds(String zkPath) throws Exception{
        ResponseMessage rm;
        try{
            String nodeName= StringUtils.isEmpty(zkPath)?"/":zkPath;
            List<Node> childNodes=zkService.getChilds(nodeName);
            rm=new ResponseMessage(true,null,childNodes);
        }catch (Exception ex){
            this.logger.error(this.getClass().getName()+".getChilds",ex);
            rm=new ResponseMessage("获取节点数据失败！");
        }
        return rm;
    }

    /**
     * 获取节点下的数据
     * @param zkPath
     * @return
     * @throws Exception
     */
    @RequestMapping("/getKVs")
    public @ResponseBody ResponseMessage getKVs(String zkPath) throws Exception{
        ResponseMessage rm;
        try{
            String nodeName=StringUtils.isEmpty(zkPath)?"/":zkPath;
            List<KeyValue> kvs=zkService.getKVs(nodeName);
            rm=new ResponseMessage(true,null,kvs);
        }catch (Exception ex){
            this.logger.error(this.getClass().getName()+".getKVs",ex);
            rm=new ResponseMessage("获取数据失败！");
        }
        return rm;
    }

    /**
     * nodeName=parentNode+"/"+当前节点名
     * @param node
     * @throws Exception
     */
    @RequestMapping("/createNode")
    public @ResponseBody ResponseMessage createNode(String parentNode,String node) throws Exception{
        ResponseMessage rm=null;
        try{
            if(parentNode.equals("/")){
                parentNode="";
            }
            String nodeName=parentNode.concat("/").concat(node);
            zkService.createNode(nodeName);
            rm=new ResponseMessage(true,null,nodeName);
        }catch (Exception ex){
            this.logger.error(this.getClass().getName()+".createNode",ex);
            rm=new ResponseMessage("创建节点失败");
        }

        return rm;
    }

    /**
     *
     * @param node
     * @param value
     * @throws Exception
     */
    @RequestMapping("/updateParameter")
    public @ResponseBody ResponseMessage updateParameter(String node,String value) throws Exception{
        ResponseMessage rm=null;
        try{
            zkService.createParameter(node,value);
            rm=new ResponseMessage(true,null,"修改节点数据成功");
        }catch (Exception ex) {
            logger.error(this.getClass().getName()+".updateParameter",ex);
            rm=new ResponseMessage("修改节点数据失败");
        }

        return rm;
    }

    /**
     *
     * @param key
     * @param value
     */
    @RequestMapping("/addParameter")
    public @ResponseBody ResponseMessage addParameter(String parentNode,String key,String value) {
        ResponseMessage rm=new ResponseMessage("添加节点失败");
        try{
            String nodeName=parentNode.concat("/").concat(key);
            zkService.createParameter(nodeName,value);
            rm=new ResponseMessage(true,null,"添加节点成功！");
        }catch (Exception ex){
            logger.error(this.getClass().getName()+".addParameter",ex);

        }finally {
            return rm;
        }
    }

    /**
     * 删除单节点
     * @param nodeName
     * @return
     */
    @RequestMapping("/deleteNode")
    public @ResponseBody ResponseMessage deleteNode(String nodeName){
        try{
        	ResponseMessage rm = null;
        	List<Node> list = zkService.getChilds(nodeName);
        	if(list.size() > 0) {
        		rm=new ResponseMessage("存在子节点，不能删除！");
        		return rm;
        	}
        	List<KeyValue> kvs = zkService.getKVs(nodeName);
        	if(kvs.size() > 0) {
        		rm=new ResponseMessage("该节点存在属性，不能删除！");
        		return rm;
        	}
        	zkService.deleteNode(nodeName);
            rm=new ResponseMessage(true,"删除节点成功！",null);
            return rm;
        }catch (Exception ex){
            this.logger.error(ex.getMessage(),ex);
            ResponseMessage rm=new ResponseMessage("删除节点失败！");
            return rm;
        }
    }
    
    /**
     * 删除节点(强制删除)
     * @param nodeName
     * @return
     */
    @RequestMapping("/deleteNodes")
    public @ResponseBody ResponseMessage deleteNodes(String[] nodeNames){
        try{
            zkService.mandatoryDeleteNodes(nodeNames);
            ResponseMessage rm=new ResponseMessage(true,"删除节点成功！",null);
            return rm;
        }catch (Exception ex){
            this.logger.error(ex.getMessage(),ex);
            ResponseMessage rm=new ResponseMessage("删除节点失败！");
            return rm;
        }
    }

    /**
     *
     */

    public void login(){

    }
}
