package shp.common.exceptions;

import shp.common.entities.Artikel;

public class ArtikelExistiertNichtException extends Exception {
	private Artikel artikel;
		public ArtikelExistiertNichtException(Artikel artikel , String zusatzMsg) {
			super("Artikel mit Name '" + artikel.getName() + "'" + " existiert nicht" + zusatzMsg);
			this.artikel = artikel;
		}

		public ArtikelExistiertNichtException( String gesucht) {
			super("Artikel mit Name '" + gesucht + "'" + " existiert nicht");
			
		}
	
	public Artikel getArtikel() {
		return artikel;
	}

}
