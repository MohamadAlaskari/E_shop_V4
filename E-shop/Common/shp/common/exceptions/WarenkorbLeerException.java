package shp.common.exceptions;

public class WarenkorbLeerException extends Exception {

	public WarenkorbLeerException() {
		super("Warenkorb ist leer, es kann nicht besellt werden");
	}

}
