package OracleConnection.OrderConfrimEjecutable.Modelos;

public class PosHeader {
    public PosHeader() {
		super();
	}

	public PosHeader(String consecutivo,String posStore, String posTerminal, String posTransaction, String posTransactionDate,
			String posOrderPasillo, String posAssociateNumber, String posTenderType, String posAmountDue,
			String fechaCompleta, String esquema, String autorizacion, String codigoRespuesta, String confirmacion,
			String numTarjeta) {
		super();
		this.consecutivo = consecutivo;
		this.posStore = posStore;
		this.posTerminal = posTerminal;
		this.posTransaction = posTransaction;
		this.posTransactionDate = posTransactionDate;
		this.posOrderPasillo = posOrderPasillo;
		this.posAssociateNumber = posAssociateNumber;
		PosTenderType = posTenderType;
		PosAmountDue = posAmountDue;
		this.fechaCompleta = fechaCompleta;
		this.esquema = esquema;
		this.autorizacion = autorizacion;
		this.codigoRespuesta = codigoRespuesta;
		this.confirmacion = confirmacion;
		this.numTarjeta = numTarjeta;
	}


	private String consecutivo;
	private String posStore;
    private String posTerminal;
    private String posTransaction;
    private String posTransactionDate;   
    private String posOrderPasillo;
    private String posAssociateNumber;
    
    private String PosTenderType;
    private String PosAmountDue;
    
    
    private String fechaCompleta;
    private String esquema;
    private String autorizacion;
    private String codigoRespuesta;
    private String confirmacion;
    private String numTarjeta;
    private String total_order;

    // Constructor
    public PosHeader(String posStore, String posTerminal, String posTransaction, 
                     String posTransactionDate, String posOrderPasillo, String posAssociateNumber) {
        this.posStore = posStore;
        this.posTerminal = posTerminal;
        this.posTransaction = posTransaction;
        this.posTransactionDate = posTransactionDate;
        this.posOrderPasillo = posOrderPasillo;
        this.posAssociateNumber = posAssociateNumber;
    }

    // Getters y Setters
    public String getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(String consecutivo) {
        this.consecutivo = consecutivo;
    }
    
    public String getPosStore() {
        return posStore;
    }

    public void setPosStore(String posStore) {
        this.posStore = posStore;
    }

    public String getPosTerminal() {
        return posTerminal;
    }

    public void setPosTerminal(String posTerminal) {
        this.posTerminal = posTerminal;
    }

    public String getPosTransaction() {
        return posTransaction;
    }

    public void setPosTransaction(String posTransaction) {
        this.posTransaction = posTransaction;
    }

    public String getPosTransactionDate() {
        return posTransactionDate;
    }

    public void setPosTransactionDate(String posTransactionDate) {
        this.posTransactionDate = posTransactionDate;
    }

    public String getPosOrderPasillo() {
        return posOrderPasillo;
    }

    public void setPosOrderPasillo(String posOrderPasillo) {
        this.posOrderPasillo = posOrderPasillo;
    }

    public String getPosTenderType() {
        return PosTenderType;
    }

    public void setPosTenderType(String PosTenderType) {
        this.PosTenderType = PosTenderType;
    }
    
    public String getPosAmountDue() {
        return PosAmountDue;
    }

    public void setPosAmountDue(String PosAmountDue) {
        this.PosAmountDue = PosAmountDue;
    }

    
    public String getesquema() {
        return esquema;
    }

    public void setesquema(String esquema) {
        this.esquema = esquema;
    }
    
    public String getfechaCompleta() {
        return fechaCompleta;
    }

    public void setfechaCompleta(String fechaCompleta) {
        this.fechaCompleta = fechaCompleta;
    }
    
    public String getautorizacion() {
        return autorizacion;
    }

    public void setautorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
    }
    
    public String getcodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setcodigoRespuesta(String codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }
    
    public String getconfirmacion() {
        return confirmacion;
    }

    public void setconfirmacion(String confirmacion) {
        this.confirmacion = confirmacion;
    }
    
    public String getPosAssociateNumber() {
        return posAssociateNumber;
    }

    public void setPosAssociateNumber(String posAssociateNumber) {
        this.posAssociateNumber = posAssociateNumber;
    }
    
    public String getnumTarjeta() {
        return numTarjeta;
    }

    public void setnumTarjeta(String numTarjeta) {
        this.numTarjeta = numTarjeta;
    }
    
    public String getTotal() {
        return total_order;
    }

    public void setTotal(String total_order) {
        this.total_order = total_order;
    }
    
    @Override
    public String toString() {
        return "PosHeader {" +
        		"posConsecutivo='" + consecutivo + '\'' +
                "posStore='" + posStore + '\'' +
                ", posTerminal='" + posTerminal + '\'' +
                ", posTransaction='" + posTransaction + '\'' +
                ", posTransactionDate='" + posTransactionDate + '\'' +
                ", posOrderPasillo='" + posOrderPasillo + '\'' +
                ", posAssociateNumber='" + posAssociateNumber + '\'' +
                ", PosTenderType='" + PosTenderType + '\'' +
                ", PosAmountDue='" + PosAmountDue + '\'' +
                ", fechaCompleta='" + fechaCompleta + '\'' +
                ", esquema='" + esquema + '\'' +
                ", autorizacion='" + autorizacion + '\'' +
                ", codigoRespuesta='" + codigoRespuesta + '\'' +
                ", confirmacion='" + confirmacion + '\'' +
                ", numTarjeta='" + numTarjeta + '\'' +
                ", Total='" + total_order + '\'' +
                '}';
    }



   
}
