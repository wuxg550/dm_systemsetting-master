package cn.hy.config.zkclient.platform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.curator.framework.state.ConnectionStateListener;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultAttribute;

import cn.hy.config.zkclient.zknode.RelationNode;
import cn.hy.config.zkclient.zknode.interfaces.IAddress;
import cn.hy.config.zkclient.zknode.interfaces.IEntrance;
import cn.hy.config.zkclient.zknode.interfaces.IEntrances;
import cn.hy.config.zkclient.zknode.interfaces.IRelationListener;
import cn.hy.config.zkclient.zknode.interfaces.IServerInfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public abstract class XmlConfig extends Config{

	private String infoXmlPath = "";
	private String relationXmlPath = "";
	public void setXmlPath(String infoXmlPath){
		this.infoXmlPath = infoXmlPath;
	}
	
	public XmlConfig(String zkAddress, String infoXmlPath, ConnectionStateListener... listeners) {
		super(zkAddress, listeners);
		this.infoXmlPath = infoXmlPath;
	}
	public XmlConfig(String zkAddress, String infoXmlPath,String relationXmlPath, ConnectionStateListener... listeners) {
		super(zkAddress, listeners);
		this.infoXmlPath = infoXmlPath;
		this.relationXmlPath = relationXmlPath;
	}

	/**
	 * 获取xml中的服务信息
	 * @return 返回xml数据的结构化json
	 */
	@SuppressWarnings("unchecked")
	public JSONObject readServerInfo(){
		JSONObject serverInfo = new JSONObject();
		try{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new File(infoXmlPath));
			Element root = document.getRootElement();
			
			// 读取服务信息
			serverInfo.put("serverId", (root.elementText("serverId")));
			serverInfo.put("serverType", (root.elementText("serverType")));
			serverInfo.put("serverName", (root.elementText("serverName")));
			serverInfo.put("orgCode", (root.elementText("orgCode")));
			serverInfo.put("addressType", (root.elementText("addressType")));
			serverInfo.put("address", (root.elementText("address")));
			serverInfo.put("cascadeDomain", (root.elementText("cascadeDomain")));
			serverInfo.put("orgId", (root.elementText("orgId")));
			serverInfo.put("orgName", (root.elementText("orgName")));
			
			// 读取入口信息
			JSONArray entrances = new JSONArray();
			Element enElement = root.element("entrances");
			if(enElement != null){
				List<Element> elementList = enElement.elements();
				for(Element el : elementList){
					if(el.elementText("port").trim() != ""){
						JSONObject en = new JSONObject();
						en.put("port", (el.elementText("port")));
						en.put("protocol", (el.elementText("protocol")));
						en.put("url", (el.elementText("url")));
						en.put("userName", (el.elementText("userName")));
						en.put("password", (el.elementText("password")));
						
						//读取功能码
						List<String> fcs = new ArrayList<String>();
						Element fcsElement = el.element("fcs");
						List<Element> fcElements = fcsElement.elements();
						for(Element fc : fcElements){
							fcs.add(fc.getText());
						}
						en.put("fcs", fcs);
						entrances.add(en);
					}
				}
			}
			serverInfo.put("entrances", entrances);
		}catch(Exception e){
			logger.error("读取xml文件服务信息", e);
		}
		return serverInfo;
	}
	
	/**
	 * 根据xml中的服务信息注册服务 并监听
	 * @param infoXmlPath xml文档路径
	 * @param listener 流向监听接口
	 * @return
	 * @throws Exception 
	 */
	public <T extends IServerInfo & IAddress & IEntrances> XmlConfig registerAllByXml(T t,IRelationListener listener) throws Exception{
		// 注册服务型信息// 注册服务地址信息// 注册入口信息
		registerInfo(t).registerAddress(t).registerEntrance(t);
		// 监听流向变化
		relationListener(t, listener);
		return this;
	}
	
	/**
	 * 读取xml流向数据
	 * @param infoXmlPath
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RelationNode> readRelation(){
		List<RelationNode> list = new ArrayList<RelationNode>();
		try{
			SAXReader saxReader = new SAXReader();
			Document document;
			document = saxReader.read(relationXmlPath);
			Element root = document.getRootElement();
			Element relationsElement = root.element("relations");
			// 读取所有流向节点
			if(relationsElement != null){
				List<Element> elements = relationsElement.elements("relation");
				for(Element e : elements){
					JSONObject obj = new JSONObject();
					// 读取属性键值对，装进json对象中
					Iterator<DefaultAttribute> it = e.attributeIterator();
					for(;it.hasNext();){
						DefaultAttribute ae = it.next();
						String qName = ae.getName();
						String value = e.attributeValue(qName);
						obj.put(qName, value);
					}
					// 将json对象转为流向节点类
					RelationNode r = JSON.toJavaObject(obj, RelationNode.class);
					list.add(r);
				}
			}
		}catch(Exception e){
			logger.error("读取xml文件流向信息", e);
		}
		return list;
	}
	
	/**
	 * 将流向数据保存至xml（先删除原数据再保存）
	 * @param infoXmlPath
	 * @param relatinList
	 */
	@SuppressWarnings("unused")
	private void saveRelationToXml(List<RelationNode> relatinList){
		FileOutputStream out = null;
		XMLWriter writer = null;
		try{
			SAXReader saxReader = new SAXReader();
			Document document;
			document = saxReader.read(relationXmlPath);
			Element root = document.getRootElement();
			Element rsElemet = root.element("relations");
			// 删除原数据
			if(rsElemet != null){
				rsElemet.detach();
			}
			// 保存新数据
			rsElemet = root.addElement("relations");
			for(RelationNode r : relatinList){
				// 新建一个流向xml节点
				Element rElement = rsElemet.addElement("relation");
				// 将流向节点实体类转为json对象
				JSONObject obj = (JSONObject) JSON.toJSON(r);
				// 循环将实体类属性及值添加到xml节点
				for(String key : obj.keySet()){
					rElement.addAttribute(key, obj.getString(key));
				}
			}
			
			out = new FileOutputStream(infoXmlPath);
	        OutputFormat format = OutputFormat.createPrettyPrint();
	        format.setEncoding("utf-8");
	        writer = new XMLWriter(out,format);
	        writer.write(document);
		}catch(Exception e){
			logger.error("保存流向信息到xml文件", e);
		}finally{
			try {
				if(writer != null){
					writer.close();
				}
				if(out != null){
					out.close();
				}
			} catch (IOException e) {
				logger.error("关闭流异常", e);
			}
		}
	}
	
	/**
	 * 修改xml中：服务ID、服务类型、服务名称、机构编码
	 * @param infoXmlPath
	 * @param info
	 */
	public void modifyXmlServerInfo(IServerInfo info){
		FileOutputStream out = null;
		XMLWriter writer = null;
		try{
			SAXReader saxReader = new SAXReader();
			Document document;
			document = saxReader.read(new File(infoXmlPath));
			Element root = document.getRootElement();
			root.element("serverId").setText(info.getServerId()!=null?info.getServerId():"");
			root.element("serverType").setText(info.getServerType()!=null?info.getServerType():"");
			root.element("serverName").setText(info.getServerName()!=null?info.getServerName():"");
			root.element("orgCode").setText(info.getOrgCode()!=null?info.getOrgCode():"");
			if(root.element("cascadeDomain") != null){
				root.element("cascadeDomain").setText(info.getCascadeDomain()!=null?info.getCascadeDomain():"");
			}
			if(root.element("orgId") != null){
				root.element("orgId").setText(info.getOrgId()!=null?info.getOrgId():"");
			}
			if(root.element("orgName") != null){
				root.element("orgName").setText(info.getOrgName()!=null?info.getOrgName():"");
			}
			
			out = new FileOutputStream(infoXmlPath);
	        OutputFormat format = OutputFormat.createPrettyPrint();
	        format.setEncoding("utf-8");
	        writer = new XMLWriter(out,format);
	        writer.write(document);
		} catch(Exception e){
			logger.error("保存服务信息到xml文件", e);
		}finally{
			try {
				if(writer != null){
					writer.close();
				}
				if(out != null){
					out.close();
				}
			} catch (IOException e) {
				logger.error("关闭流异常", e);
			}
		}
		
	}
	
	/**
	 * 修改xml中服务的地址信息
	 * @param infoXmlPath
	 * @param address
	 */
	public void modifyXmlAddress(IAddress address){
		FileOutputStream out = null;
		XMLWriter writer = null;
		try{
			SAXReader saxReader = new SAXReader();
			Document document;
			document = saxReader.read(new File(infoXmlPath));
			Element root = document.getRootElement();
			root.element("addressType").setText(address.getAddressType());
			root.element("address").setText(address.getAddress());
			
			out = new FileOutputStream(infoXmlPath);
	        OutputFormat format = OutputFormat.createPrettyPrint();
	        format.setEncoding("utf-8");
	        writer = new XMLWriter(out,format);
	        writer.write(document);
		} catch(Exception e){
			logger.error("保存服务地址信息到xml文件", e);
		}finally{
			try {
				if(writer != null){
					writer.close();
				}
				if(out != null){
					out.close();
				}
			} catch (IOException e) {
				logger.error("关闭流异常", e);
			}
		}
		
	}
	
	/**
	 * 修改xml中入口信息（先删除原数据再保存）
	 * @param infoXmlPath
	 * @param info
	 */
	public void modifyXmlEntrance(IEntrances entrances){
		FileOutputStream out = null;
		XMLWriter writer = null;
		try{
			SAXReader saxReader = new SAXReader();
			Document document;
			document = saxReader.read(new File(infoXmlPath));
			Element root = document.getRootElement();
			// 入口信息修改
			List<IEntrance> entrans = entrances.getEntrances();
			Element enElement = root.element("entrances");
			if(enElement != null){
				enElement.detach();
			}
			Element  newElement = root.addElement("entrances");
			for(IEntrance e : entrans){
				Element newEn = newElement.addElement("entrance");
				newEn.addElement("port").setText(e.getPort()!=null?e.getPort().toString():"");
				newEn.addElement("protocol").setText(e.getProtocol()!=null?e.getProtocol():"");
				newEn.addElement("url").setText(e.getUrl()!=null?e.getUrl():"");
				newEn.addElement("userName").setText(e.getUserName()!=null?e.getUserName():"");
				newEn.addElement("password").setText(e.getPassword()!=null?e.getPassword():"");
				Element fcs = newEn.addElement("fcs");
				for(String fc : e.getFcs()){
					fcs.addElement("fc").setText(fc);
				}
			}
			
			out = new FileOutputStream(infoXmlPath);
	        OutputFormat format = OutputFormat.createPrettyPrint();
	        format.setEncoding("utf-8");
	        writer = new XMLWriter(out,format);
	        writer.write(document);
		} catch(Exception e){
			logger.error("保存服务入口信息到xml文件", e);
		}finally{
			try {
				if(writer != null){
					writer.close();
				}
				if(out != null){
					out.close();
				}
			} catch (IOException e) {
				logger.error("关闭流异常", e);
			}
		}
		
	}
	
	/**
	 * 修改服务的xml所有信息
	 * @param infoXmlPath
	 * @param info
	 */
	public <T extends IServerInfo & IAddress & IEntrances> void modifyXmlAll(T info){
		this.modifyXmlServerInfo(info);
		this.modifyXmlAddress(info);
		this.modifyXmlEntrance(info);
	}
}
