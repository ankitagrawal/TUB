package com.hk.domain.inventory.rtv;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 18, 2012
 * Time: 6:23:27 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "rtv_note_line_item")

public class RtvNoteLineItem implements Serializable{

  @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column (name = "id", unique = true, nullable = false)
	private Long id;

  @OneToOne
  @JoinColumn (name = "extra_inventory_line_item" , nullable = false)
  private ExtraInventoryLineItem extraInventoryLineItem;

  @Column (name = "remarks")
  private String remarks;

  public ExtraInventoryLineItem getExtraInventoryLineItem() {
    return extraInventoryLineItem;
  }

  public void setExtraInventoryLineItem(ExtraInventoryLineItem extraInventoryLineItem) {
    this.extraInventoryLineItem = extraInventoryLineItem;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
