package com.enjoy.stock.business;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.business.AbstractBusiness;
import com.enjoy.core.main.Constants;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.CompanyVendorBean;
import com.enjoy.stock.bean.ProductQuanHistoryBean;
import com.enjoy.stock.bean.ProductdetailBean;
import com.enjoy.stock.bean.ProductmasterBean;
import com.enjoy.stock.bean.ProductquantityBean;
import com.enjoy.stock.services.ProductQuanHistoryService;
import com.enjoy.stock.services.ProductService;
import com.enjoy.stock.services.ProductquantityService;

@Service
public class ProductBusinessImpl extends AbstractBusiness implements ProductBusiness{
	private static final Logger logger = Logger.getLogger(ProductBusinessImpl.class);

	@Autowired
	ProductService productService;
	@Autowired
	ProductQuanHistoryService productQuanHistoryService;
	@Autowired
	ProductquantityService productquantityService;
	
	@Override
	public ProductmasterBean getProductDetail(String productCode, String tin)throws Exception {
		return productService.getProductDetail(productCode, tin);
	}
	@Override
	public ArrayList<ProductdetailBean> getProductdetailList(String productCode, String tin) throws Exception {
		return productService.getProductdetailList(productCode, tin);
	}
	@Override
	public int checkDupProductCode(String productCodeDis, String tin,String productCode, String pageMode) throws Exception {
		return productService.checkDupProductCode(productCodeDis, tin, productCode, pageMode);
	}
	@Override
	public int checkDupProductName(String productName, String productCode,String tin, String pageMode) throws Exception {
		return productService.checkDupProductName(productName, productCode, tin, pageMode);
	}
	@Override
	public String save(ProductmasterBean vo,ArrayList<ProductdetailBean> productdetailList, String pageMode)throws Exception {
		logger.info("[save][Begin]");
		
		String	productCode		= null;
		int		seqDb			= 1;
		
		try{
			if(Constants.NEW.equals(pageMode)){
				productCode = EnjoyUtils.nullToStr(productService.genId(vo.getTin()));
				vo.setProductCode(productCode);
				vo.setProductStatus("A");
				productService.insertProductmaster(vo);
			}else{
				productCode = vo.getProductCode();
				productService.updateProductmaster(vo);
			}
			
			productService.deleteProductdetail(productCode, vo.getTin());
			
			if(productdetailList!=null){
				for(ProductdetailBean bean:productdetailList){
					bean.setProductCode(productCode);
					bean.setTin(vo.getTin());
					bean.setSeqDb(String.valueOf(seqDb++));
					productService.insertProductdetail(bean);
				}
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[save][End]");
		}
		return productCode;
	}
	@Override
	public void searchByCriteria(PaginatedListBean paginatedList,ProductmasterBean productmasterBean,String tin) throws Exception {
		productService.searchByCriteria(paginatedList, productmasterBean,tin);
		
	}
	@Override
	public ArrayList<ComboBean> productNameList(String productName,String productTypeName, String productGroupName, String tin,boolean flag) throws Exception {
		return productService.productNameList(productName, productTypeName, productGroupName, tin, flag);
	}
	@Override
	public ArrayList<ProductmasterBean> getProductmasterForBarCode(String productCode, String tin) throws Exception {
		return productService.getProductmasterForBarCode(productCode, tin);
	}
	@Override
	public ArrayList<ProductmasterBean> getMultiManageProduct(ProductmasterBean vo) throws Exception {
		return productService.getMultiManageProduct(vo);
	}
	@Override
	public void saveMultiProduct(ProductmasterBean productmasterBean ,ArrayList<ProductmasterBean> productList, String tin ,String productCodeForDelete) throws Exception {
		logger.info("[saveMultiProduct][Begin]");
		
		boolean					chkProductFlag			= true;
		boolean					chkQuanFlag				= true;
		int						productCode				= 1;
		int						hisCode					= 1;
		String[] 				productCodeArray;
		ProductquantityBean		productquantityBean		= null;
		ProductQuanHistoryBean	productQuanHistoryBean	= null;
		String					quantityDb				= null;
		
		try{
			if(EnjoyUtils.chkNull(productCodeForDelete)){
				productCodeArray = productCodeForDelete.split(",");
				for(String code:productCodeArray){
					cancelProductmaster(tin, "", "", code);
				}
			}
			
			for(ProductmasterBean bean:productList){
				if(Constants.NEW.equals(bean.getRowStatus())){
					/*Begin หา productCode*/
					if(chkProductFlag==true){
						productCode 	= productService.genId(tin);
						chkProductFlag  = false;
					}else{
						productCode++;
					}
					/*End หา productCode*/
					
					/*Begin หา hisCode*/
					if(chkQuanFlag==true){
						hisCode		= productQuanHistoryService.genId(tin);
						chkQuanFlag  	= false;
					}else{
						hisCode++;
					}
					/*End หา hisCode*/
					
					/*Begin รายการสินค้า*/
					bean.setTin				(tin);
					bean.setProductCode		(EnjoyUtils.nullToStr(productCode));
					bean.setProductTypeCode	(productmasterBean.getProductTypeCode());
					bean.setProductGroupCode(productmasterBean.getProductGroupCode());
					bean.setUnitCode		(productmasterBean.getUnitCode());
					bean.setProductStatus	("A");
					productService.insertProductmaster(bean);
					/*End รายการสินค้า*/
					
					/*Begin จำนวนสินค้า*/
					quantityDb = productquantityService.getProductquantity(EnjoyUtils.nullToStr(productCode),tin);
					
					productquantityBean = new ProductquantityBean();
					productquantityBean.setProductCode(EnjoyUtils.nullToStr(productCode));
					productquantityBean.setTin(tin);
					productquantityBean.setQuantity(bean.getQuantity());
					if(!EnjoyUtils.chkNull(quantityDb)){
						productquantityService.insertProductquantity(productquantityBean);
					}else{
						productquantityService.updateProductquantity(productquantityBean);
					}
					/*End จำนวนสินค้า*/
					
					/*Begin ส่วนประวัตเพิ่มลดสินค้า*/
					productQuanHistoryBean = new ProductQuanHistoryBean();
					productQuanHistoryBean.setFormRef("MultiManageProduct");
					
					productQuanHistoryBean.setProductType(bean.getProductTypeCode());
					productQuanHistoryBean.setProductGroup(bean.getProductGroupCode());
					productQuanHistoryBean.setProductCode(EnjoyUtils.nullToStr(productCode));
					
					productQuanHistoryBean.setTin(tin);
					
					if(EnjoyUtils.parseDouble(bean.getQuantity()) > 0){
						productQuanHistoryBean.setQuantityPlus(bean.getQuantity());
						productQuanHistoryBean.setQuantityMinus("0.00");
					}else{
						productQuanHistoryBean.setQuantityPlus("0.00");
						productQuanHistoryBean.setQuantityMinus(EnjoyUtils.nullToStr(Math.abs(EnjoyUtils.parseDouble(bean.getQuantity()))));
					}
					productQuanHistoryBean.setQuantityTotal(bean.getQuantity());
					
					productQuanHistoryService.insert(productQuanHistoryBean, hisCode);
					/*End ส่วนประวัตเพิ่มลดสินค้า*/
				}else{//กรณี update รายละเอียดสินค้า
					/*Begin รายการสินค้า*/
					bean.setTin				(tin);
					bean.setProductTypeCode	(productmasterBean.getProductTypeCode());
					bean.setProductGroupCode(productmasterBean.getProductGroupCode());
					bean.setUnitCode		(productmasterBean.getUnitCode());
					productService.updateProductmaster(bean);
					/*End รายการสินค้า*/
					
					/*Begin จำนวนสินค้า*/
					quantityDb = productquantityService.getProductquantity(bean.getProductCode(),tin);
					
					productquantityBean = new ProductquantityBean();
					productquantityBean.setProductCode(bean.getProductCode());
					productquantityBean.setTin(tin);
					productquantityBean.setQuantity(bean.getQuantity());
					if(!EnjoyUtils.chkNull(quantityDb)){
						productquantityService.insertProductquantity(productquantityBean);
					}else{
						productquantityService.updateProductquantity(productquantityBean);
					}
					/*End จำนวนสินค้า*/
					
					/*Begin ส่วนประวัตเพิ่มลดสินค้า*/
					//insert เฉพาะในกรณีที่มีการเปลี่ยนแปลงจำนวน
					if(!quantityDb.equals(bean.getQuantity())){
						productQuanHistoryBean = new ProductQuanHistoryBean();
						productQuanHistoryBean.setFormRef("MultiManageProduct");
						
						productQuanHistoryBean.setProductType(bean.getProductTypeCode());
						productQuanHistoryBean.setProductGroup(bean.getProductGroupCode());
						productQuanHistoryBean.setProductCode(bean.getProductCode());
						
						productQuanHistoryBean.setTin(tin);
						
						if(EnjoyUtils.parseDouble(bean.getQuantity()) > 0){
							productQuanHistoryBean.setQuantityPlus(bean.getQuantity());
							productQuanHistoryBean.setQuantityMinus("0.00");
						}else{
							productQuanHistoryBean.setQuantityPlus("0.00");
							productQuanHistoryBean.setQuantityMinus(EnjoyUtils.nullToStr(Math.abs(EnjoyUtils.parseDouble(bean.getQuantity()))));
						}
						productQuanHistoryBean.setQuantityTotal(bean.getQuantity());
						
						/*Begin หา hisCode*/
						if(chkQuanFlag==true){
							hisCode		= productQuanHistoryService.genId(tin);
							chkQuanFlag  	= false;
						}else{
							hisCode++;
						}
						/*End หา hisCode*/
						
						productQuanHistoryService.insert(productQuanHistoryBean, hisCode);
					}
					/*End ส่วนประวัตเพิ่มลดสินค้า*/
				}
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[saveMultiProduct][End]");
		}
		
	}
	@Override
	public void cancelProductmaster(String tin, String productType,String productGroup, String productCode) throws Exception {
		productService.cancelProductmaster(tin, productType, productGroup, productCode);
		
	}
	@Override
	public ProductmasterBean getProductDetailByName(String productName,String tin) throws Exception {
		return productService.getProductDetailByName(productName, tin);
	}
	@Override
	public void searchByCriteriaforLookUp(PaginatedListBean paginatedList,ProductmasterBean productmasterBean) throws Exception {
		logger.info("[searchByCriteriaforLookUp][End]");
		
		ArrayList<ProductmasterBean> 	list 		= new ArrayList<ProductmasterBean>();
		String							quantityDb	= null;
		
		try{
			productService.searchByCriteria(paginatedList, productmasterBean,productmasterBean.getTin());
			
			list = (ArrayList<ProductmasterBean>) paginatedList.getList();
			
			logger.info("[searchByCriteriaforLookUp] list :: " + list.size());
			
			for(ProductmasterBean vo:list){
				quantityDb = productquantityService.getProductquantity(productmasterBean.getProductCode(),productmasterBean.getTin());
				
				if(quantityDb!=null){
					vo.setQuantity(EnjoyUtils.convertNumberToDisplay(quantityDb));
				}
			}
			
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[searchByCriteriaforLookUp][End]");
		}
		
	}
	@Override
	public String getQuanDiscount(String productCode, String quantity,String invoiceDate, String tin, String availPageFlag)throws Exception {
		return productService.getQuanDiscount(productCode, quantity, invoiceDate, tin, availPageFlag);
	}
	@Override
	public ProductmasterBean getProductDetailByProductCodeDis(String tin,String productCodeDis) throws Exception {
		return productService.getProductDetailByProductCodeDis(tin, productCodeDis);
	}

	

	

}
