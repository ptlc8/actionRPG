package fr.actionrpg3d.multiplayer;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface Packageable<T> {
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface UnpackMethod {}

	void pack(OutputPackage pack) throws IOException;

}
