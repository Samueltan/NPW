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
public class TemperatureRecord extends org.jooq.impl.UpdatableRecordImpl<dao.generated.tables.records.TemperatureRecord> implements org.jooq.Record3<java.lang.String, java.lang.Integer, java.lang.Float> {

	private static final long serialVersionUID = -517330751;

	/**
	 * Setter for <code>temperature.addr</code>.
	 */
	public void setAddr(java.lang.String value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>temperature.addr</code>.
	 */
	public java.lang.String getAddr() {
		return (java.lang.String) getValue(0);
	}

	/**
	 * Setter for <code>temperature.time</code>.
	 */
	public void setTime(java.lang.Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>temperature.time</code>.
	 */
	public java.lang.Integer getTime() {
		return (java.lang.Integer) getValue(1);
	}

	/**
	 * Setter for <code>temperature.temperature</code>.
	 */
	public void setTemperature(java.lang.Float value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>temperature.temperature</code>.
	 */
	public java.lang.Float getTemperature() {
		return (java.lang.Float) getValue(2);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.String> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record3 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row3<java.lang.String, java.lang.Integer, java.lang.Float> fieldsRow() {
		return (org.jooq.Row3) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row3<java.lang.String, java.lang.Integer, java.lang.Float> valuesRow() {
		return (org.jooq.Row3) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field1() {
		return dao.generated.tables.Temperature.TEMPERATURE.ADDR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return dao.generated.tables.Temperature.TEMPERATURE.TIME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Float> field3() {
		return dao.generated.tables.Temperature.TEMPERATURE.TEMPERATURE_;
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
	public java.lang.Integer value2() {
		return getTime();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Float value3() {
		return getTemperature();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TemperatureRecord value1(java.lang.String value) {
		setAddr(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TemperatureRecord value2(java.lang.Integer value) {
		setTime(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TemperatureRecord value3(java.lang.Float value) {
		setTemperature(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TemperatureRecord values(java.lang.String value1, java.lang.Integer value2, java.lang.Float value3) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached TemperatureRecord
	 */
	public TemperatureRecord() {
		super(dao.generated.tables.Temperature.TEMPERATURE);
	}

	/**
	 * Create a detached, initialised TemperatureRecord
	 */
	public TemperatureRecord(java.lang.String addr, java.lang.Integer time, java.lang.Float temperature) {
		super(dao.generated.tables.Temperature.TEMPERATURE);

		setValue(0, addr);
		setValue(1, time);
		setValue(2, temperature);
	}
}
