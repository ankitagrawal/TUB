package com.hk.domain.Faq;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Jul 12, 2012
 * Time: 5:02:21 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "faq")
public class faq implements java.io.Serializable {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 20)
    private String               id;
    
}
