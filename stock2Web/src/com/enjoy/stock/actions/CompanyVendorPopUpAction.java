package com.enjoy.stock.actions;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.enjoy.core.actions.ActionControl;
import com.enjoy.core.actions.BaseAction;
import com.enjoy.core.business.AddressBusiness;
import com.enjoy.core.exception.EnjoyException;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.CompanyVendorBean;
import com.enjoy.stock.business.CompanyVendorBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyVendorPopUpAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CompanyVendorPopUpAction.class);
	
	private CompanyVendorBean 			companyVendorBean;
	private String						hidVendorCodePopUp;
	
	@Autowired
	AddressBusiness addressBusiness;
	
	@Autowired
	CompanyVendorBusiness companyVendorBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	logger.info("[success] hidVendorCodePopUp :: " + hidVendorCodePopUp);
    	
    	companyVendorBean = companyVendorBusiness.getCompanyVendor(hidVendorCodePopUp, getCurrentUser().getTin());
    	
    	companyVendorBean.setTin(EnjoyUtils.tinFormat(companyVendorBean.getTin()));
    	companyVendorBean.setRemark(companyVendorBean.getRemark().replaceAll("(\r\n|\n)", "<br />"));
		
		if(companyVendorBean==null){
			throw new EnjoyException("เกิดข้อผิดพลาดในการดึงรายละเอียดบริษัทสั่งซื้อ");
		}
    	
		return SUCCESS;
	}
    
	@Override
	public String autoComplete()throws Exception {
    	return null;
    }


}
