package qdh.dao.impl.product;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qdh.dao.entity.product.Product;
import qdh.dao.entity.product.ProductBarcode;
import qdh.dao.impl.BaseDAO;

@Repository
public class ProductBarcodeDaoImpl  extends BaseDAO<ProductBarcode> {

	public Set<Integer> getIds(int yearId, int quarterId, int brandId) {
		Set<Integer> idSet = new HashSet<Integer>();
		
		String ids = "SELECT pb.id FROM ProductBarcode pb JOIN pb.product p WHERE p.year.year_ID =? AND p.quarter.quarter_ID =? AND p.brand.brand_ID=?";

		Object[] values = new Object[]{yearId, quarterId, brandId};
		
		List<Object> idObjects = this.executeHQLSelect(ids, values, null, true);
		if (idObjects != null){
			for (Object obj : idObjects){
				idSet.add((Integer)obj);
			}
		}
		return idSet;
	}

	public Set<Integer> getIdsByCategoryId(int categoryId) {
		Set<Integer> idSet = new HashSet<Integer>();
		
		String ids = "SELECT pb.id FROM ProductBarcode pb JOIN pb.product p WHERE p.category.category_ID=?";

		Object[] values = new Object[]{categoryId};
		
		List<Object> idObjects = this.executeHQLSelect(ids, values, null, true);
		if (idObjects != null){
			for (Object obj : idObjects){
				idSet.add((Integer)obj);
			}
		}
		return idSet;
	}

	public List<ProductBarcode> getBarcodeFromProduct(int productId) {
		DetachedCriteria productBarcodeCriteria = DetachedCriteria.forClass(ProductBarcode.class);
		
		productBarcodeCriteria.add(Restrictions.eq("product.productId", productId));
		
		return this.getByCritera(productBarcodeCriteria, true);
	}
	
	/**
	 * to get one product's barcodes
	 * @param serialNum
	 * @return
	 */
	public List<ProductBarcode> getProductBracodeFromSameGroup(Product product) {
		DetachedCriteria productBarcodeCriteria = DetachedCriteria.forClass(ProductBarcode.class);
		
		productBarcodeCriteria.add(Restrictions.eq("status", ProductBarcode.STATUS_OK));
		DetachedCriteria productCriteria = productBarcodeCriteria.createCriteria("product");
		productCriteria.add(Restrictions.eq("area.area_ID", product.getArea().getArea_ID()));
		productCriteria.add(Restrictions.eq("year.year_ID", product.getYear().getYear_ID()));
		productCriteria.add(Restrictions.eq("quarter.quarter_ID", product.getQuarter().getQuarter_ID()));
		productCriteria.add(Restrictions.eq("brand.brand_ID", product.getBrand().getBrand_ID()));
		productCriteria.add(Restrictions.eq("category.category_ID", product.getCategory().getCategory_ID()));
		productCriteria.add(Restrictions.eq("productCode", product.getProductCode()));

		List<ProductBarcode> productBarcodeList =  this.getByCritera(productBarcodeCriteria, true);
		return productBarcodeList;
	}

	public int updateToDelete(int pbId){
		String queryString = "UPDATE ProductBarcode p SET p.status = ? where p.id = ?";
		
		Object[] values = {ProductBarcode.STATUS_DELETE, pbId};
		
        return this.executeHQLUpdateDelete(queryString, values, true);
	}

}
