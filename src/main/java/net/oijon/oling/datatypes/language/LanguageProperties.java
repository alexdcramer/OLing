package net.oijon.oling.datatypes.language;

import java.time.Instant;
import java.util.Date;
import net.oijon.oling.Parser;
import net.oijon.oling.datatypes.tags.Multitag;
import net.oijon.oling.datatypes.tags.Tag;
import net.oijon.oling.info.Info;
import net.oijon.olog.Log;

public class LanguageProperties {

	public static Log log = Parser.getLog();
	
	// 0 = autonym, 1 = id, 2 = name, 3 = versionEdited
	private String[] strings = {"null", "null", "null", Info.getVersion()};	
	// 0 = created, 1 = edited
	private Date[] dates = {Date.from(Instant.now()), Date.from(Instant.now())};
	
	private boolean isReadOnly = false;
	
	public LanguageProperties() {
		
	}
	
	public LanguageProperties(LanguageProperties lp) {
		this.isReadOnly = lp.isReadOnly();
		this.strings[0] = lp.getProperty(LanguageProperty.AUTONYM);
		this.strings[1] = lp.getProperty(LanguageProperty.ID);
		this.strings[2] = lp.getProperty(LanguageProperty.NAME);
		this.strings[3] = lp.getProperty(LanguageProperty.VERSION_EDITED);
		this.dates[0] = lp.getCreated();
		this.dates[1] = lp.getEdited();
	}
	
	/**
	 * Parses language properties from a multitag
	 * @param docTag The multitag to parse from
	 * @return The language properties parsed
	 * @throws Exception Thrown when properties are mangled or invalid
	 */
	public static LanguageProperties parse(Multitag docTag) throws Exception {
		LanguageProperties lp = new LanguageProperties();
		
		Multitag meta = docTag.getMultitag("Meta");
		lp.setProperty(LanguageProperty.NAME, meta.getDirectChild("name").value());
		lp.checkID(meta);
		lp.setProperty(LanguageProperty.AUTONYM, meta.getDirectChild("autonym").value());
		lp.setReadOnly(Boolean.parseBoolean(meta.getDirectChild("readonly").value()));
		lp.setCreated(new Date(Long.parseLong(meta.getDirectChild("timeCreated").value())));
		lp.setEdited(new Date(Long.parseLong(meta.getDirectChild("lastEdited").value())));
		lp.checkVersion(meta);
		return lp;
	}
	
	@SuppressWarnings("deprecation")
	/**
	 * Generates an ID for the language
	 */
	private void generateID() {
		int rand = (int) (Math.random() * 100000);
		// "its deprecated" i dont care
		// why does DateTimeFormatter not accept date objects :(
		Date created = dates[0];
		this.setProperty(LanguageProperty.ID, strings[2].toUpperCase() +
				created.getYear() +
				created.getMonth() +
				created.getDay() +
				created.getHours() +
				created.getMinutes() +
				created.getSeconds()
				+ rand);
	}
	
	private void checkID(Multitag meta) {
		Tag id = new Tag("id");
		try {
			id = meta.getDirectChild("id");
			if (!id.value().isBlank() & !id.value().equals("null")) {
				log.info("ID of language is " + id.value());
				this.setProperty(LanguageProperty.ID, id.value());
			} else {
				log.err("This language appears to have a blank or null ID!");
				log.warn("Generating new ID, this may break relations with other languages!");
				generateID();
				log.warn("New ID: " + this.getProperty(LanguageProperty.ID) + ". If other languages are related to this language, "
						+ "a manual switch to the new ID will be neccessary.");
			}
		} catch (Exception e) {
			log.warn("This language appears to have been created with a very early version of Oijon Utils!");
			log.warn("The id tag was required as of 1.2.0.");
			generateID();
		}
	}
	
	/**
	 * Checks if the version tag is using a deprecated format
	 * @param meta The meta tag
	 * @param ver The version tag
	 * @throws Exception Thrown if neither utilsVersion nor susquehannaVersion exist
	 */
	private void checkVersion(Multitag meta) throws Exception {
		Tag ver;
		try {
			ver = meta.getDirectChild("utilsVersion");
			if (!ver.value().isBlank()) {
				log.info("Language created with " + ver.value());
				setProperty(LanguageProperty.VERSION_EDITED, ver.value());
			}
		} catch (Exception e) {
			ver = meta.getDirectChild("susquehannaVersion");
			if (!ver.value().isBlank()) {
				log.info("Language created with " + ver.value());
				setProperty(LanguageProperty.VERSION_EDITED, ver.value());
			}
			log.warn("This language appears to have been created with a very early version of Oijon Utils!");
			log.warn("The susquehannaVersion tag was deprecated as of Oijon Utils 1.2.0.");
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof LanguageProperties) {
			LanguageProperties lp = (LanguageProperties) o;
			/*
			 * Does not check for:
			 * - date edited (will never be equal)
			 * - version edited (may or may not be equal)
			 */
			if (lp.getProperty(LanguageProperty.AUTONYM).equals(strings[0]) & 
					lp.getProperty(LanguageProperty.ID).equals(strings[1]) & 
					lp.getProperty(LanguageProperty.NAME).equals(strings[2]) &
					lp.isReadOnly() == isReadOnly &
					lp.getCreated().equals(dates[0])) {
				return true;
			}
		}
		return false;
	}
	
	public String getProperty(LanguageProperty p) {
		switch(p) {
			case AUTONYM: return strings[0];
			case ID: return strings[1];
			case NAME: return strings[2];
			case VERSION_EDITED: return strings[3];
		}
		return " ";
	}
	
	public void setProperty(LanguageProperty p, String value) {
		switch(p) {
			case AUTONYM: strings[0] = value;
			break;
			case ID: strings[1] = value;
			break;
			case NAME: strings[2] = value;
			break;
			case VERSION_EDITED: strings[3] = value;
			break;
		}
	}
	
	public boolean isReadOnly() {
		return isReadOnly;
	}
	public Date getCreated() {
		return (Date) dates[0].clone();
	}
	public Date getEdited() {
		return (Date) dates[1].clone();
	}
	
	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}
	public void setCreated(Date created) {
		this.dates[0] = (Date) created.clone();
	}
	public void setEdited(Date edited) {
		this.dates[1] = (Date) edited.clone();
	}
	
}
