package com.hk.api.impl.service;

import com.hk.api.constants.HKAPIOperationStatus;
import com.hk.api.dto.HKAPIBaseDTO;
import com.hk.api.dto.brand.HKAPIBrandProductsDTO;
import com.hk.api.dto.product.HKAPIProductDTO;
import com.hk.api.dto.product.HKAPIProductOptionDTO;
import com.hk.api.dto.product.HKAPIProductVariantDTO;
import com.hk.api.pact.service.HKAPIBrandService;
import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.pact.service.catalog.ProductService;
import com.hk.util.HKImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 5/14/13
 * Time: 2:24 PM
 */
@Service
public class HKAPIBrandServiceImpl implements HKAPIBrandService {

    @Autowired
    ProductService productService;

    public HKAPIBaseDTO getAllProductsByBrand(String brand){
        HKAPIBaseDTO hkapiBaseDTO=new HKAPIBaseDTO();
        if(!productService.doesBrandExist(brand)){
            hkapiBaseDTO.setStatus(HKAPIOperationStatus.ERROR);
            hkapiBaseDTO.setMessage("brand does not exist in healthkart");
            return hkapiBaseDTO;
        }
        List<Product> brandProducts=productService.getAllProductByBrand(brand);
        HKAPIBrandProductsDTO hkapiBrandProductsDTO=new HKAPIBrandProductsDTO();
        hkapiBrandProductsDTO.setBrand(brand);
        int normalProducts=0;
        for(Product product:brandProducts){
            if (product instanceof Combo) {
                continue;
            }
            normalProducts++;
        }
        HKAPIProductDTO[] hkapiProductDTOs=new HKAPIProductDTO[normalProducts];

        int j=0;
        for(Product product:brandProducts){
            if (product instanceof Combo) {
                continue;
            }
            HKAPIProductDTO productDTO=new HKAPIProductDTO();

            productDTO.setName(product.getName());
            productDTO.setOverview(product.getOverview());
            productDTO.setDeleted(product.isDeleted());
            productDTO.setOutOfStock(product.getOutOfStock());
            productDTO.setProductID(product.getId());

            if(product.getMainImageId()!=null){
                productDTO.setImageUrl(HKImageUtils.getS3ImageUrl(EnumImageSize.MediumSize, product.getMainImageId(),false));
                productDTO.setThumbUrl(HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize, product.getMainImageId(),false));
            }

            List<ProductVariant> productVariantList=product.getProductVariants();
            HKAPIProductVariantDTO[] productVariantDTOs = new HKAPIProductVariantDTO[productVariantList.size()];
            int i=0;
            for(ProductVariant variant:productVariantList){
                HKAPIProductVariantDTO productVariantDTO = new HKAPIProductVariantDTO();
                if(variant.getProductOptions().size()>0){
                    HKAPIProductOptionDTO[] optionDTOs=new HKAPIProductOptionDTO[variant.getProductOptions().size()];
                    int k=0;
                    for(ProductOption option:variant.getProductOptions()){
                        HKAPIProductOptionDTO optionDTO=new HKAPIProductOptionDTO();
                        optionDTO.setName(option.getName());
                        optionDTO.setValue(option.getValue());
                        optionDTOs[k]=optionDTO;
                        k++;
                    }
                    productVariantDTO.setProductOptions(optionDTOs);
                }
                productVariantDTO.setProductVariantID(variant.getId());
                productVariantDTO.setHkDiscountPercent(variant.getDiscountPercent());
                productVariantDTO.setHkPrice(variant.getHkPrice());
                productVariantDTO.setMrp(variant.getMarkedPrice());
                productVariantDTO.setDeleted(variant.isDeleted());
                productVariantDTO.setOutOfStock(variant.isOutOfStock());
                productVariantDTOs[i] = productVariantDTO;
                i++;
            }
            productDTO.setProductVariantDTOs(productVariantDTOs);
            hkapiProductDTOs[j]=productDTO;
            j++;
        }
        hkapiBrandProductsDTO.setProducts(hkapiProductDTOs);
        hkapiBaseDTO.setData(hkapiBrandProductsDTO);
        return hkapiBaseDTO;
    }

}
