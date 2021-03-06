package org.cocome.ordersclient.domain;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * The ProductOrder class represents an ProductOrder of a Store in the database.
 *
 * @author Yannick Welsch
 * @author Nils Sommer
 */
@XmlRootElement(name = "Order")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Order", propOrder = { "deliveryDate", "orderingDate" })
public class ProductOrder implements Serializable {

	@XmlTransient
	private static final long serialVersionUID = -8340585715760459030L;

	@XmlTransient
	private long id;
	
	@XmlElement(name = "DeliveryDate")
	private Date deliveryDate;
	
	@XmlElement(name = "OrderingDate")
	private Date orderingDate;
	
	@XmlTransient
	private long storeId;

	/** Cechkstyle basic constructor. */
	public ProductOrder() {}

	/**
	 * @return A unique identifier for ProductOrder objects
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            A unique identifier for ProductOrder objects
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * @return The date of ordering.
	 */
	public Date getOrderingDate() {
		return this.orderingDate;
	}

	/**
	 * @param orderingDate
	 *            the date of ordering
	 */
	public void setOrderingDate(final Date orderingDate) {
		this.orderingDate = orderingDate;
	}

	/**
	 * The delivery date is used for computing the mean time to delivery
	 *
	 * @return The date of order fulfillment.
	 */
	public Date getDeliveryDate() {
		return this.deliveryDate;
	}

	/**
	 * The delivery date is used for computing the mean time to delivery
	 *
	 * @param deliveryDate
	 *            the date of order fulfillment
	 */
	public void setDeliveryDate(final Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	/**
	 * @return The store where the order is placed.
	 */
	public long getStoreId() {
		return this.storeId;
	}

	/**
	 * @param store
	 *            the store where the order is placed
	 */
	public void setStoreId(final long storeId) {
		this.storeId = storeId;
	}

	@Override
	public String toString() {
		return "[Class:" + this.getClass().getSimpleName() + ",Id:" + this.getId() + ",StoreId:" + this.getStoreId() + "]";
	}

}


