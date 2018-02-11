package com.enjoy.stock.business;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.business.AbstractBusiness;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.ComparePriceBean;
import com.enjoy.stock.bean.ComparePriceRemarkBean;
import com.enjoy.stock.services.ComparePriceService;

@Service
public class ComparePriceBusinessImpl extends AbstractBusiness implements ComparePriceBusiness{
	private static final Logger logger = Logger.getLogger(ComparePriceBusinessImpl.class);

	@Autowired
	ComparePriceService comparePriceService;

	@Override
	public ArrayList<ComparePriceBean> searchByCriteria(String tin,String productCode) throws Exception {
		return comparePriceService.searchByCriteria(tin, productCode);
	}

	@Override
	public void insertCompareprice(ComparePriceBean vo) throws Exception {
		comparePriceService.insertCompareprice(vo);
		
	}

	@Override
	public int couVenderInThisProduct(String productCode, String vendorCode,String tin) throws Exception {
		return comparePriceService.couVenderInThisProduct(productCode, vendorCode, tin);
	}

	@Override
	public int genId(String productCode, String tin) throws Exception {
		return comparePriceService.genId(productCode, tin);
	}

	@Override
	public void deleteCompareprice(String productCode, String tin)throws Exception {
		comparePriceService.deleteCompareprice(productCode, tin);
		
	}

	@Override
	public ComparePriceBean getPrice(ComparePriceBean vo) throws Exception {
		return comparePriceService.getPrice(vo);
	}

	@Override
	public void insertComparepriceRemark(ComparePriceRemarkBean vo)throws Exception {
		comparePriceService.insertComparepriceRemark(vo);
		
	}

	@Override
	public void updateComparepriceRemark(ComparePriceRemarkBean vo)throws Exception {
		comparePriceService.updateComparepriceRemark(vo);
		
	}

	@Override
	public int checkForInsertRemark(String productCode, String tin)throws Exception {
		return comparePriceService.checkForInsertRemark(productCode, tin);
	}

	@Override
	public String getRemark(String productCode, String tin) throws Exception {
		return comparePriceService.getRemark(productCode, tin);
	}

	@Override
	public void save(ArrayList<ComparePriceBean> voList, String tin,String productCode,String comparePriceRemark) throws Exception {
		logger.info("[save][Begin]");
		
		int	seq	= 1;
		ComparePriceRemarkBean	comparePriceRemarkBean;
		
		try{
			
			comparePriceService.deleteCompareprice(productCode, tin);
			
			for(ComparePriceBean bean:voList){
				bean.setSeqDb(EnjoyUtils.nullToStr(seq));
				bean.setTin(tin);
				comparePriceService.insertCompareprice(bean);
				seq++;
			}
			
			comparePriceRemarkBean = new ComparePriceRemarkBean();
			comparePriceRemarkBean.setProductCode(productCode);
			comparePriceRemarkBean.setTin(tin);
			comparePriceRemarkBean.setRemark(comparePriceRemark);
			
			if(comparePriceService.checkForInsertRemark(productCode, tin) > 0){
				comparePriceService.updateComparepriceRemark(comparePriceRemarkBean);
			}else{
				comparePriceService.insertComparepriceRemark(comparePriceRemarkBean);
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[save][End]");
		}
		
	}
	
	
	

	

}
