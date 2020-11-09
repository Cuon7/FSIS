package a3_1801040028.fsis;

import utils.AttrRef;
import utils.DOpt;
import utils.DomainConstraint;
import utils.NotPossibleException;
import utils.OptType;

/**
 * @overview Customer
 * @attributes
 *  id              Integer
 *  name            String
 *  phoneNumber     String
 *  address         String
 * 
 * @object Customer is Customer=<id, name, phoneNumber, address>, 
 * 
 * @abstract_properties
 *      mutable(id)=false /\ optional(id)=false /\ min(id)=1 /\ max(id)=1000000000 /\
 *      mutable(name)=true /\ optional(name)=false /\ length(name)=50 /\
 *      mutable(phoneNumber)=true /\ optional(phoneNumber)=false /\ length(phoneNumber)=10 /\
 *      mutable(address)=true /\ optional(address)=false /\ length(address)=100 /\
 * @author Cuon7
 */
public class Customer implements Comparable<Customer>, Document {
    	@DomainConstraint(type = "Integer", mutable = false, optional = false, min = MIN_CUSTOMER, max = MAX_CUSTOMER)
	private int id;
	@DomainConstraint(type = "String", mutable = true, optional = false, length = LENGTH_NAME)
	private String name;
	@DomainConstraint(type = "String", mutable = true, optional = false, length = LENGTH_PHONE)
	private String phoneNumber;
	@DomainConstraint(type = "String", mutable = true, optional = false, length = LENGTH_ADDRESS)
	private String address;
        public static final int LENGTH_NAME = 50;
        public static final int LENGTH_PHONE = 10;
        public static final int LENGTH_ADDRESS = 100;
        public static final int MIN_CUSTOMER = 1;
        public static final int MAX_CUSTOMER = 1000000000;
        public static final int MIN_HGH_EARNER = 10000000;
        
    /**
     * @effects <pre>
     *     if id, name, phoneNumber, address are valid
     *          initialize this as Customer<id, name, phoneNumber, address>
     *     else
     *          throw NotPossibleException
     * </pre>
     */ 
    public Customer(@AttrRef("id") int id, @AttrRef("name") String name,
              @AttrRef("phoneNumber") String phoneNumber, @AttrRef("address") String address) {
        if (validateId(id)) {
            this.id = id;
        } else {
            throw new NotPossibleException("Customer<init>: invalid id: " + id);
        }
        if (validateName(name)) {
            this.name = name;
        } else {
            throw new NotPossibleException("Customer<init>: invalid name: " + name);
        }
        if (validatePhoneNumber(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new NotPossibleException("Customer<init>: invalid phone number: " + phoneNumber);
        }
        if (validateAddress(address)) {
            this.address = address;
        } else {
            throw new NotPossibleException("Customer<init>: invalid addrees: " + address);
        }
    }
    
    /**
    * @effects <pre>
    *   if name is valid
    *     set this.name=name
    *     return true
    *   else
    *     throws NotPossibleException</pre>
    */
    @DOpt(type=OptType.Mutator) @AttrRef("name")
    public void setName(String name) throws NotPossibleException {
        if (validateName(name)) {
            this.name = name;
        } else {
            throw new NotPossibleException("Customer.setName: "
                    + "invalid name " + name);
        }
    }

    /**
    * @effects <pre>
    *   if phoneNumber is valid
    *     set this.phoneNumber=phoneNumber
    *     return true
    *   else
    *     throws NotPossibleException</pre>
    */
    @DOpt(type=OptType.Mutator) @AttrRef("phoneNumber")
	public void setPhoneNumber(String phoneNumber) throws NotPossibleException {
		if (validatePhoneNumber(phoneNumber))
			this.phoneNumber = phoneNumber;
		else
			throw new NotPossibleException("Customer.setPhoneNumber: "
                                + "invalid phone number " + phoneNumber);
	}
        
    /**
    * @effects <pre>
    *   if address is valid
    *     set this.address=address
    *     return true
    *   else
    *     throws NotPossibleException</pre>
    */
    @DOpt(type=OptType.Mutator) @AttrRef("address")
	public void setAddress(String address) throws NotPossibleException {
		if (validateAddress(address))
			this.address = address;
		else
			throw new NotPossibleException("Customer.setAddress: "
                                + "invalid address " + address);
	}
        
    /**
     * @effects return <tt>id</tt>
     */
    @DOpt(type=OptType.Observer) @AttrRef("id")
	public int getId() {
		return id;
	}
        
    /**
     * @effects return <tt>name</tt>
     */
    @DOpt(type=OptType.Observer) @AttrRef("name")
	public String getName() {
		return name;
	}
        
    /**
     * @effects return <tt>phoneNumber</tt>
     */
    @DOpt(type=OptType.Observer) @AttrRef("phoneNumber")
	public String getPhoneNumber() {
		return phoneNumber;
	}
        
    /**
     * @effects return <tt>address</tt>
     */
    @DOpt(type=OptType.Observer) @AttrRef("address")
	public String getAddress() {
		return address;
	}
        
   // validation methods
   /**
   * @effects <pre>
   *            if < id,name,phoneNumber,address > is a valid tuple 
   *              return true
   *            else
   *              return false </pre>
   */
    private boolean validate(int id, String name, String phoneNumber, String address) {
        return validateId(id) && validateName(name)
                && validatePhoneNumber(phoneNumber) && validateAddress(address);
  }
    /**
    * @effects <pre>
    *  if id is valid 
    *    return true 
    *  else
    *    return false
    *  </pre>
    */
    protected boolean validateId(int id){
        if (id >= MIN_CUSTOMER && id <= MAX_CUSTOMER) 
            return true;
        else
            return false;
    }
    
    /**
    * @effects <pre>
    *  if name is valid 
    *    return true 
    *  else
    *    return false
    *  </pre>
    */
    private boolean validateName(String name){
        if (name != null && name.length() > 0 && name.length() <= LENGTH_NAME)
            return true;
        else
            return false;
        
    }
    /**
    * @effects <pre>
    *  if phoneNumber is valid 
    *    return true 
    *  else
    *    return false
    *  </pre>
    */
    private boolean validatePhoneNumber(String phoneNumber){
        if (phoneNumber != null && phoneNumber.length() > 0 && phoneNumber.length() <= LENGTH_PHONE)
            return true;
        else
            return false;
    }
    
    /**
    * @effects <pre>
    *  if address is valid 
    *    return true 
    *  else
    *    return false
    *  </pre>
    */
    private boolean validateAddress(String address){
        if (address != null && address.length() > 0 && address.length() <= LENGTH_ADDRESS)
            return true;
        else
            return false;
    }
    
    @Override
    public String toString(){
        return "Customer:<" + id + "," + name + "," + phoneNumber + "," + address + ">";
    }
    
    /**
    * @effects <pre>
    *   if this satisfies abstract properties
    *     return true 
    *   else
    *     return false</pre>
    */
    public boolean repOK(){
          return validate(id, name, phoneNumber, address);
    }   

    
    	/**
	 * @param o
	 * @effect
	 * 
	 *         <pre>
	 *    if c is null throws NullPointerException else if c is not
	*         instance of Customer throw ClassCastException else return
	*         this.name.compareTo(c.name)
	 *         </pre>
	 */
	@Override
	public int compareTo(Customer o) throws NullPointerException, ClassCastException {
		if (o == null) {
			throw new NullPointerException("Customer.compareByName");
		} else if (!(o instanceof Customer)) {
			throw new ClassCastException("Customer.compareByName: not a Customer " + o);
		} else
			return this.name.compareTo(o.name);
	}
    
    
  	/**
	 * @effect return this in String in HTML file
	 */
	@Override
	public String toHtmlDoc() {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<title>Customer: " + this.getId() + "-" + this.getName() + "</title>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append(this.getId() + " " + this.getName() + " " + this.getPhoneNumber() + " " + this.getAddress());
		sb.append("</body></html>");
		return sb.toString();
	}
}
