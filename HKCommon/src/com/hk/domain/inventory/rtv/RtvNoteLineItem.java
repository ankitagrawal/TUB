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

@NamedQueries({
    @NamedQuery(name = "getRtvNoteLineItemsByRtvNote" , query = "select rli from RtvNoteLineItem rli where rtvNote = :rtvNote"),
    @NamedQuery(name = "getRtvNoteLineItemById", query = "select rli from RtvNoteLineItem rli where id = :rtvNoteLineItemId"),
    @NamedQuery(name = "getRtvNoteLineItemByExtraInventoryLineItem", query = "select rli from RtvNoteLineItem rli where extraInventoryLineItem.id = :extraInventoryLineItemId")
})

public class RtvNoteLineItem implements Serializable{

  @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column (name = "id", unique = true, nullable = false)
	private Long id;

  @ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn (name = "rtv_note_id", nullable = false)
  private RtvNote rtvNote;

  @OneToOne
  @JoinColumn (name = "extra_inventory_line_item_id" , nullable = false)
  private ExtraInventoryLineItem extraInventoryLineItem;

  public ExtraInventoryLineItem getExtraInventoryLineItem() {
    return extraInventoryLineItem;
  }

  public void setExtraInventoryLineItem(ExtraInventoryLineItem extraInventoryLineItem) {
    this.extraInventoryLineItem = extraInventoryLineItem;
  }

   public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public RtvNote getRtvNote() {
    return rtvNote;
  }

  public void setRtvNote(RtvNote rtvNote) {
    this.rtvNote = rtvNote;
  }
}
