package kohdeluokat;

public class Tili {
	private String tilinro;
	private double saldo;
	private Asiakas omistaja;
	
	public Tili() {
		super();
		tilinro = null;
		saldo=0;
		omistaja = null;
	}
	public Tili(String tilinro, double saldo, Asiakas as) {
		this();
		this.tilinro = tilinro;
		if ( saldo >= 0)
			this.saldo = saldo;
		omistaja = as;
	}
	public String getTilinro() {
		return tilinro;
	}
	public void setTilinro(String tilinro) {
		this.tilinro = tilinro;
	}
	public double getSaldo() {
		return saldo;
	}
	public void setSaldo(double saldo) {
		if (saldo >= 0)
			this.saldo = saldo;
	}
	
	public Asiakas getOmistaja() {
		return omistaja;
	}
	public void setOmistaja(Asiakas omistaja) {
		this.omistaja = omistaja;
	}
	public String toString() {
		return "Tili tilinro=" + tilinro + ", saldo=" + saldo + " eur" +
	"\n" + omistaja;
	}

}
