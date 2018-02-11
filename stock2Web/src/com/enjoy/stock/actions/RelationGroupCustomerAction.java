package com.enjoy.stock.actions;

import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.enjoy.core.actions.ActionControl;
import com.enjoy.core.actions.BaseAction;
import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.exception.EnjoyException;
import com.enjoy.core.main.Constants;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.RelationGroupCustomerBean;
import com.enjoy.stock.business.RelationGroupCustomerBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class RelationGroupCustomerAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(RelationGroupCustomerAction.class);
	
	private String									indexRow;
	private ArrayList<RelationGroupCustomerBean> 	relationGroupCustomerList;
	private String 									cusGroupCodeForDelete;
	private ArrayList<ComboBean> 					groupSalePriceCombo;
	
	@Autowired
	RelationGroupCustomerBusiness relationGroupCustomerBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	cusGroupCodeForDelete 		= "";
    	relationGroupCustomerList 	= relationGroupCustomerBusiness.searchByCriteria(getCurrentUser().getTin());
    	
    	setGroupSalePriceCombo();
    	
		return SUCCESS;
	}
    
    private void setGroupSalePriceCombo() throws Exception{
		
		logger.info("[setGroupSalePriceCombo][Begin]");
		
		String[]	idArray 	= {"", "1", "2","3","4","5"};
		String[]	descArray 	= {"กรุณาระบุ", "ใช้ราคาที่ 1", "ใช้ราคาที่ 2", "ใช้ราคาที่ 3","ใช้ราคาที่ 4","ใช้ราคาที่ 5"};
		
		try{
			
			groupSalePriceCombo = new ArrayList<ComboBean>();
			
			for(int i=0;i<idArray.length;i++){
				groupSalePriceCombo.add(new ComboBean(idArray[i]	, descArray[i]));
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[setGroupSalePriceCombo][End]");
		}
	}
    
    public String newRecord() throws Exception{
		logger.info("[newRecord][Begin]");
		
		RelationGroupCustomerBean	bean	= new RelationGroupCustomerBean();
		
		try{
			
			if(relationGroupCustomerList==null){
				relationGroupCustomerList = new ArrayList<RelationGroupCustomerBean>();
			}
			bean.setCusGroupStatus("A");
			bean.setRowStatus(Constants.NEW);
			
			relationGroupCustomerList.add(bean);
			
			setGroupSalePriceCombo();
			
			logger.info("[newRecord] size :: " + relationGroupCustomerList.size());
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[newRecord][End]");
		}
		
		return SEARCH;
	}
    
    public String deleteRecord() throws Exception{
		logger.info("[deleteRecord][Begin]");
		
		int cou = 0;
		
		try{
			logger.info("[deleteRecord] indexRow :: " + indexRow);
			
			for(int i=0;i<relationGroupCustomerList.size();i++){
				RelationGroupCustomerBean vo = relationGroupCustomerList.get(i);
				
				if(vo.getSeq().equals(indexRow)){
					if(!Constants.NEW.equals(vo.getRowStatus())){
						
						cou = relationGroupCustomerBusiness.countCusGroupCodeInCustomer(vo.getCusGroupCode(), getCurrentUser().getTin());
						if(cou > 0){
							throw new EnjoyException("ไม่สามารถลบกลุ่มลูกค้านี้ได้เนื่องจากมีการใช้กลุ่มลูกค้านี้อยู่");
						}
						
						if(!EnjoyUtils.chkNull(cusGroupCodeForDelete)){
							cusGroupCodeForDelete = vo.getCusGroupCode();
						}else{
							cusGroupCodeForDelete += "," + vo.getCusGroupCode();
						}
					}
					relationGroupCustomerList.remove(i);
					break;
				}
			}
			
			setGroupSalePriceCombo();
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[deleteRecord][End]");
		}
		
		return SEARCH;
	}
    
    public String save()throws Exception {
    	logger.info("[save][Begin]");
		
    	RelationGroupCustomerBean		bean		= null;
    	RelationGroupCustomerBean		beanTemp	= null;
    	
		try{
			if(relationGroupCustomerList!=null && !relationGroupCustomerList.isEmpty()){
				for(int i=0;i<relationGroupCustomerList.size();i++){
					bean = relationGroupCustomerList.get(i);
					for(int j=(i+1);j<relationGroupCustomerList.size();j++){
						beanTemp = relationGroupCustomerList.get(j);
						if(bean.getCusGroupName().equals(beanTemp.getCusGroupName())){
							throw new EnjoyException("ชื่อกลุ่มลูกค้าห้ามซ้ำ");
						}
					}
				}
			}
			
			relationGroupCustomerBusiness.save(relationGroupCustomerList, getCurrentUser().getTin(),cusGroupCodeForDelete);
			
			success();
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[save][End]");
		}
		
		return SEARCH;
    }
    
    @Override
    public String autoComplete()throws Exception {
    	return null;
    }
    
    
}
