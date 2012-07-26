package com.hk.web.action.core.catalog;

import net.sourceforge.stripes.action.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.catalog.product.combo.SuperSaverImage;
import com.hk.pact.service.catalog.combo.SuperSaverImageService;

import java.util.List;

@UrlBinding("/super-savers")
@Component
public class SuperSaversAction extends BaseAction {
    List<SuperSaverImage> superSaverImages;

    @Autowired
    SuperSaverImageService superSaverImageService;

    public Resolution pre() {
        superSaverImages = superSaverImageService.getSuperSaverImages(Boolean.TRUE, Boolean.TRUE);
        return new ForwardResolution("/pages/superSavers.jsp");
    }

    public List<SuperSaverImage> getSuperSaverImages() {
        return superSaverImages;
    }

    public void setSuperSaverImages(List<SuperSaverImage> superSaverImages) {
        this.superSaverImages = superSaverImages;
    }
}
