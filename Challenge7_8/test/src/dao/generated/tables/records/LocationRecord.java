/**
 * This class is generated by jOOQ
 */
package dao.generated.tables.records;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.2" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class LocationRecord extends org.jooq.impl.TableRecordImpl<dao.generated.tables.records.LocationRecord> implements org.jooq.Record4<java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer> {

	private static final long serialVersionUID = -1804455811;

	/**
	 * Setter for <code>test.location.addr</code>.
	 */
	public void setAddr(java.lang.String value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>test.location.addr</code>.
	 */
	public java.lang.String getAddr() {
		return (java.lang.String) getValue(0);
	}

	/**
	 * Setter for <code>test.location.time</code>.
	 */
	public void setTime(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>test.location.time</code>.
	 */
	public java.lang.String getTime() {
		return (java.lang.String) getValue(1);
	}

	/**
	 * Setter for <code>test.location.distanceR</code>.
	 */
	public void setDistancer(java.lang.Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>test.location.distanceR</code>.
	 */
	public java.lang.Integer getDistancer() {
		return (java.lang.Integer) getValue(2);
	}

	/**
	 * Setter for <code>test.location.centerX</code>.
	 */
	public void setCenterx(java.lang.Integer value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>test.location.centerX</code>.
	 */
	public java.lang.Integer getCenterx() {
		return (java.lang.Integer) getValue(3);
	}

	// -------------------------------------------------------------------------
	// Record4 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer> fieldsRow() {
		return (org.jooq.Row4) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer> valuesRow() {
		return (org.jooq.Row4) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field1() {
		return dao.generated.tables.Location.LOCATION.ADDR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return dao.generated.tables.Location.LOCATION.TIME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field3() {
		return dao.generated.tables.Location.LOCATION.DISTANCER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field4() {
		return dao.generated.tables.Location.LOCATION.CENTERX;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value1() {
		return getAddr();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value2() {
		return getTime();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value3() {
		return getDistancer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value4() {
		return getCenterx();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord value1(java.lang.String value) {
		setAddr(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord value2(java.lang.String value) {
		setTime(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord value3(java.lang.Integer value) {
		setDistancer(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord value4(java.lang.Integer value) {
		setCenterx(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord values(java.lang.String value1, java.lang.String value2, java.lang.Integer value3, java.lang.Integer value4) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached LocationRecord
	 */
	public LocationRecord() {
		super(dao.generated.tables.Location.LOCATION);
	}

	/**
	 * Create a detached, initialised LocationRecord
	 */
	public LocationRecord(java.lang.String addr, java.lang.String time, java.lang.Integer distancer, java.lang.Integer centerx) {
		super(dao.generated.tables.Location.LOCATION);

		setValue(0, addr);
		setValue(1, time);
		setValue(2, distancer);
		setValue(3, centerx);
	}
}
