package com.hk.admin.manager;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akube.framework.dao.Page;
import com.hk.constants.core.Keys;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.CategoryService;

@Component
public class MailingListManager {

  private String mailingListFilePath;

  @Autowired
  private UserService userService;
  private CategoryService categoryService;

  @Value("#{hkEnvProps['" + Keys.Env.mailingListDir + "']}")
  String         mailingListDirPath;

  public List<User> getUserList(Category category, int pageNo, int perPage) {
    Page allMailingListPage = getUserService().getMailingList(category, pageNo, perPage);
    List<User> allMailingList = new ArrayList<User>();
    if (allMailingListPage != null) {
      allMailingList = allMailingListPage.getList();
    }
    return allMailingList;
  }

  public List<User> getAllUserList(int pageNo, int perPage) {
    Page allMailingListPage = getUserService().getAllMailingList(pageNo, perPage);
    List<User> allMailingList = new ArrayList<User>();
    if (allMailingListPage != null) {
      allMailingList = allMailingListPage.getList();
    }
    return allMailingList;
  }

  public List<User> getAllUnverifiedUserList(int pageNo, int perPage) {
    Page allMailingListPage = getUserService().getAllUnverifiedMailingList(pageNo, perPage);
    List<User> allMailingList = new ArrayList<User>();
    if (allMailingListPage != null) {
      allMailingList = allMailingListPage.getList();
    }
    return allMailingList;
  }

  public void writeIntoCSV(List<User> mailingList, String category) {
    this.setMailingListFilePath(mailingListDirPath + "/" + category + "MailingList.csv");
    try {
      FileWriter writer = new FileWriter(this.getMailingListFilePath());
      writer.append("user,email,firstname,userHash\n");
      for (User user : mailingList) {
        writer.append("\"" + user.getName() + "\"");
        writer.append(",");
        writer.append("\"" + user.getEmail() + "\"");
        writer.append(",");
        writer.append("\"" + user.getFirstName() + "\"");
        writer.append(",");
        writer.append("\"" + user.getUserHash() + "\"");
        writer.append("\n");
      }
      writer.flush();
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
      // System.out.println(e);
    }
  }

  public static void main(String[] args) {
    //CategoryDao categoryDao = ServiceLocatorFactory.getService(CategoryDao.class);
    //Category cat = getCategoryService().getCategoryByName("nutrition");
    //(new MailingListManager()).getUserList(cat, 1, 10000);
  }

  public String getMailingListFilePath() {
    return mailingListFilePath;
  }

  public void setMailingListFilePath(String mailingListFilePath) {
    this.mailingListFilePath = mailingListFilePath;
  }

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public CategoryService getCategoryService() {
    return categoryService;
  }

  public void setCategoryService(CategoryService categoryService) {
    this.categoryService = categoryService;
  }


}
