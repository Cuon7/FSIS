package a3_1801040028.fsis;

import utils.AttrRef;
import utils.DOpt;
import utils.DomainConstraint;
import utils.NotPossibleException;
import utils.OptType;

/**
 * @overview High Earner
 * @attributes
 *  id                  Integer
 *  income              Float 
 * 
 * @object HighEarner is Customer=<id, name, phoneNumber, address, income>, 
 * 
 * @abstract_properties
 *      P_Customer /\  
 *      min(id)=10000000 /\
 *      mutable(income)=true /\ optional(income)=false /\  
 *     
 * @author Cuon7
 */

    /**
     * @effects <pre>
     *     if id, name, phoneNumber, address and income are valid
     *          initialize this as HighEarner<id, name, phoneNumber, address, income>
     *     else
     *          throw NotPossibleException
     * </pre>
     */ 
public class HighEarner extends Customer implements Document  {
    	@DomainConstraint(type = "float", mutable = true, optional = false, min = MIN_HGH_EARNER)
	private float income;    
        
   /**
   * @effects
   * 
   *          <pre>
   *      if id, name, phoneNumber, address, income are valid
   *          initialize <tt>this</tt> as <id,name,phoneNumber,address,income>
   *      else
   *          throws NotPossibleException
   *          </pre>
   *
   */    
    public HighEarner(@AttrRef("id") int id, @AttrRef("name") String name,
                      @AttrRef("phoneNumber") String phoneNumber, @AttrRef("address") String address,
                      @AttrRef("income") float income)  {  
        super(id, name, phoneNumber, address);
        if (validateIncome(income)) {
            this.income = income;
        } else {
            throw new NotPossibleException("HighEarner<init>: invalid income: " + income);
        }
    }
    
    /**
    * @effects <pre>
    *   if income is valid
    *     set this.income=income
    *     return true
    *   else
    *     throws NotPossibleException</pre>
    */
    @DOpt(type=OptType.Mutator) @AttrRef("income")
    public void setIncome(Float income) throws NotPossibleException  {
        if (validateIncome(income)) {
            this.income = income;
        } else {
            throw new NotPossibleException("HighEarner.setIncome: "
                    + "invalid income " + income);
        }
    }
        
    /**
     * @effects return <tt>income</tt>
     */
    @DOpt(type=OptType.Observer) @AttrRef("income")
	public float getIncome() {
		return income;
	}
    /**
    * @effects <pre>
    *  if id is valid 
    *    return true 
    *  else
    *    return false
    *  </pre>
    */
  @Override
  @DomainConstraint(type="Integer",min=MIN_HGH_EARNER,mutable=false,optional=false)
  protected boolean validateId(int id) {
    if (!(super.validateId(id)))
      return false;

    if (id < MIN_HGH_EARNER)
      return false;
    else
      return true;
  }

        
    /**
    * @effects <pre>
    *  if income is valid 
    *    return true 
    *  else
    *    return false
    *  </pre>
    */
    private boolean validateIncome(float income){
        if (income >= MIN_HGH_EARNER)
            return true;
        else
            return false;
    }       
    @Override
    public String toString() {
        return "HighEarner:<" + super.getId() + ", " + super.getName() + ", " + super.getPhoneNumber() + ", "
                + super.getAddress() + ", " + income + ">";
    }
    
    
       // validation methods
    /**
    * @effects <pre>
    *   if this satisfies abstract properties
    *     return true 
    *   else
    *     return false</pre>
    */
    @Override
    public boolean repOK() {
        return (super.repOK() && validateIncome(income));
    }
    
    /**
     * @effect
     *
     * <pre>
     *          return converted String to a html document
     * 					
     * </pre>
     */
    @Override
    public String toHtmlDoc() {
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>HighEarner: " + super.getId() + "-" + super.getName() + "</title>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append(super.getId() + " " + super.getName() + " " + super.getPhoneNumber() + " " + super.getAddress() + " "
                + this.getIncome());
        sb.append("</body></html>");
        return sb.toString();
    }

}
