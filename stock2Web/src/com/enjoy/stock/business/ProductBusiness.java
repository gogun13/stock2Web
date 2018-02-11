package com.enjoy.stock.business;

import java.util.ArrayList;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.stock.bean.ProductdetailBean;
import com.enjoy.stock.bean.ProductmasterBean;



public interface ProductBusiness {
	public ProductmasterBean getProductDetail(String productCode,String tin) throws Exception;
	public ArrayList<ProductdetailBean> getProductdetailList(String productCode, String tin) throws Exception;
	public int checkDupProductCode(String productCodeDis, String tin, String productCode, String pageMode) throws Exception;
	public int checkDupProductName(String productName, String productCode, String tin, String pageMode) throws Exception;
	public String save(ProductmasterBean vo,ArrayList<ProductdetailBean> productdetailList, String pageMode) throws Exception;
	public void searchByCriteria(PaginatedListBean paginatedList,ProductmasterBean productmasterBean,String tin) throws Exception;
	public ArrayList<ComboBean> productNameList(String productName, String productTypeName, String productGroupName, String tin, boolean flag) throws Exception ;
	public ArrayList<ProductmasterBean> getProductmasterForBarCode(String productCode, String tin) throws Exception;
	public ArrayList<ProductmasterBean> getMultiManageProduct(ProductmasterBean vo) throws Exception;
	public void saveMultiProduct(ProductmasterBean productmasterBean ,ArrayList<ProductmasterBean> voList ,String tin ,String productCodeForDelete) throws Exception;
	public void cancelProductmaster(String tin, String productType, String productGroup, String productCode)throws Exception;
	public ProductmasterBean getProductDetailByName(String 	productName, String tin) throws Exception;
	public void searchByCriteriaforLookUp(PaginatedListBean paginatedList,ProductmasterBean productmasterBean) throws Exception;
	public String getQuanDiscount(String productCode, String quantity, String invoiceDate, String tin, String availPageFlag) throws Exception;
	public ProductmasterBean getProductDetailByProductCodeDis(String tin, String productCodeDis) throws Exception;
}
