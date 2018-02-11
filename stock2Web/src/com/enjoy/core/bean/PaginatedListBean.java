package com.enjoy.core.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaginatedListBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(PaginatedListBean.class);
	
	/** default page size */
    private static int DEFAULT_PAGE_SIZE = 10;
    private static int DEFAULT_DISPLAY_PAGE = 10;
    /** current page index, starts at 0 */
    private int index;
    /** number of results per page */
    private int pageSize;
    /** total results (records, not pages) */
    private int fullListSize;
    /** total number of page (fullListSize%pageSize) */
    private int totalPage;
    private ArrayList<?> page;
    /** list of results in the current page */
    private ArrayList<?> list;
    /** default sorting order */
    /** sort criteria (sorting property name) */
    private String sortCriterion;

    private int beginPage = 1;
    private int endPage = 10;
    private int nextPage = 1;
    private int previousPage = 10;
    private boolean isSearch = false;
    private boolean isChangePage = false;
    
    public PaginatedListBean(Map paramMap) {

      for (Iterator it = paramMap.entrySet().iterator(); it.hasNext();) {
          Map.Entry entry = (Map.Entry) it.next();
          String key = (String) entry.getKey();
          String[] value = (String[]) entry.getValue();
          if (logger.isDebugEnabled()) {
              logger.debug("key:" + key + ", value:" + value[0]);
          } 

          if ("dir".equals(key)) {
//              sortDirection = "desc".equals(value[0]) ? SortOrderEnum.DESCENDING : SortOrderEnum.ASCENDING;
          } else if ("sort".equals(key)) {
              sortCriterion = value[0];
          }
      }


      if(pageSize == 0){
          pageSize = DEFAULT_PAGE_SIZE;
      }
      
      String page = "1";
      index = page == null ? 0 : Integer.parseInt(page) - 1;

  }
    
    public void setFullListSize(int fullListSize) {
        this.fullListSize = fullListSize;
        if(this.pageSize>0){
            /* case 7/3 จะ error
            BigDecimal tpage = (new BigDecimal(fullListSize)).divide(new BigDecimal(pageSize));
             */
            BigDecimal tpage = (new BigDecimal((double)fullListSize/pageSize));
            logger.info("[setFullListSize] tpage :: " + tpage);
            tpage = tpage.setScale(0, BigDecimal.ROUND_CEILING);
            this.totalPage = tpage.intValue();

            if((index-(DEFAULT_DISPLAY_PAGE/2))<1){
                beginPage = 1;
            }else{
                beginPage = index - (DEFAULT_DISPLAY_PAGE/2);
            }

            endPage = beginPage+DEFAULT_DISPLAY_PAGE-1;

            if(beginPage>totalPage&&totalPage>0){
                beginPage = totalPage;
            }
            if(endPage>totalPage){
                beginPage = totalPage-DEFAULT_DISPLAY_PAGE+1;
                endPage = totalPage;
            }
            if(beginPage<1){
                beginPage = 1;
            }
            if(totalPage<1){
                beginPage = 1;
                endPage = 1;
            }
            nextPage=index+1;
            previousPage=index-1;
        }

    }
    
    public int getTotalPages() {
        return (int) Math.ceil(((double) fullListSize) / pageSize);
    }
    
    public int getPageNumber() {
        return index + 1;
    }
}
