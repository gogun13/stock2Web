package com.enjoy.stock.actions;

import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.enjoy.core.actions.ActionControl;
import com.enjoy.core.actions.BaseAction;
import com.enjoy.core.exception.EnjoyException;
import com.enjoy.core.main.Constants;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.ManageUnitTypeBean;
import com.enjoy.stock.business.UnitTypeBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class UnitTypeAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(UnitTypeAction.class);
	
	private String							indexRow;
	private ArrayList<ManageUnitTypeBean> 	unitTypeList;
	private String							unitCodeForDelete;
	
	@Autowired
	UnitTypeBusiness unitTypeBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	unitCodeForDelete 	= "";
    	unitTypeList 		= unitTypeBusiness.getUnitTypeList(getCurrentUser().getTin());
    	
		return SUCCESS;
	}
    
    public String newRecord() throws Exception{
		logger.info("[newRecord][Begin]");
		
		ManageUnitTypeBean	bean	= new ManageUnitTypeBean();
		
		try{
			
			if(unitTypeList==null){
				unitTypeList = new ArrayList<ManageUnitTypeBean>();
			}
			bean.setUnitStatus("A");
			bean.setRowStatus(Constants.NEW);
			
			unitTypeList.add(bean);
			
			logger.info("[newRecord] size :: " + unitTypeList.size());
			
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
			
			for(int i=0;i<unitTypeList.size();i++){
				ManageUnitTypeBean vo = unitTypeList.get(i);
				
				if(vo.getSeq().equals(indexRow)){
					if(!Constants.NEW.equals(vo.getRowStatus())){
						
						cou = unitTypeBusiness.countUnitCodeInProduct(vo.getUnitCode(), getCurrentUser().getTin());
						if(cou > 0){
							throw new EnjoyException("ไม่สามารถลบหน่วยสินค้านี้ได้เนื่องจากมีการใช้หน่วยสินค้านี้อยู่");
						}
						
						if(!EnjoyUtils.chkNull(unitCodeForDelete)){
							unitCodeForDelete = vo.getUnitCode();
						}else{
							unitCodeForDelete += "," + vo.getUnitCode();
						}
					}
					unitTypeList.remove(i);
					break;
				}
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[deleteRecord][End]");
		}
		
		return SEARCH;
	}
    
    public String save()throws Exception {
    	logger.info("[save][Begin]");
		
    	ManageUnitTypeBean		bean		= null;
    	ManageUnitTypeBean		beanTemp	= null;
    	
		try{
			
			for(int i=0;i<unitTypeList.size();i++){
				bean = unitTypeList.get(i);
				for(int j=(i+1);j<unitTypeList.size();j++){
					beanTemp = unitTypeList.get(j);
					if(bean.getUnitName().equals(beanTemp.getUnitName())){
						throw new EnjoyException("ชื่อหน่วยสินค้า" + bean.getUnitName() + "มีอยู่ในระบบแล้ว");
					}
				}
			}
			
			unitTypeBusiness.save(unitTypeList, getCurrentUser().getTin(),unitCodeForDelete);
			
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
