package shp.common.entities;

public class Massengutartikel extends Artikel {
	


	public int packungsGroesse;

	
	public Massengutartikel( int id,String name, String beschreibung, int bestand, double preis,boolean verfuegbar, boolean istPackung, int packungsGroesse ) {
		super(id,name, beschreibung, bestand, preis,verfuegbar, istPackung);
		this.packungsGroesse = packungsGroesse;
	}

	public Massengutartikel( String name, String beschreibung, int bestand, double preis, boolean istPackung, int packungsGroesse ) {
		super(name, beschreibung, bestand, preis, istPackung);
		this.packungsGroesse = packungsGroesse;
	}

	
	public int getPackungsGroesse() {
		return packungsGroesse;
	}



	public void setPackungsGroesse(int packungsGroesse) {
		this.packungsGroesse = packungsGroesse;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(", PackungsGroesse = " + packungsGroesse);
		builder.append(")");
		return builder.toString();
	}


}
