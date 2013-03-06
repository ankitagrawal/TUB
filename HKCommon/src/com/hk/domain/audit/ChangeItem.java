package com.hk.domain.audit;

import javax.persistence.*;

/**
 * @author adlakha.vaibhav
 */
@Entity
@Table(name = "change_item")
@NamedQueries(
    @NamedQuery(name = "getChangeItemByChangeGroup", query = "select ci from ChangeItem ci where ci.changeGroup.id =:changeGroupId")
)
public class ChangeItem {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "field_name", nullable = false, length = 100)
  private String fieldName;

  @Column(name = "field_type", nullable = false, length = 50)
  private String fieldType;

  @Column(name = "old_value", nullable = true, columnDefinition = "LONGTEXT")
  private String oldValue;

  @Column(name = "new_value", nullable = true, columnDefinition = "LONGTEXT")
  private String newValue;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "change_group_id", nullable = false)
  private ChangeGroup changeGroup;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }


  public String getFieldType() {
    return fieldType;
  }


  public void setFieldType(String fieldType) {
    this.fieldType = fieldType;
  }


  public String getOldValue() {
    return oldValue;
  }

  public void setOldValue(String oldValue) {
    this.oldValue = oldValue;
  }


  public String getNewValue() {
    return newValue;
  }

  public void setNewValue(String newValue) {
    this.newValue = newValue;
  }


  public ChangeGroup getChangeGroup() {
    return changeGroup;
  }

  public void setChangeGroup(ChangeGroup changeGroup) {
    this.changeGroup = changeGroup;
  }

}
