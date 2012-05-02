package web.action.core.catalog.image;

import java.io.File;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.util.ImageManager;


@Component
public class BulkUploadImageAction extends BaseAction {
   //@Named(Keys.Env.adminUploads) 
    @Value("#{hkEnvProps['adminUploads']}")
   String adminUploadsPath;
   ImageManager imageManager;

  @DefaultHandler
  public Resolution bulkUpload() throws Exception {
    File directory = new File(adminUploadsPath + "/imageFiles/");
    directory.mkdirs();
    Long imagesUploaded=imageManager.bulkUploadProduct(directory);
    addRedirectAlertMessage(new SimpleMessage(imagesUploaded.toString()+" new images uploaded"));
    return new ForwardResolution("/pages/admin/adminHome.jsp");
  }
}
