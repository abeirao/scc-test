package scc.srv;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import scc.srv.api.*;

/**
 * Cloud Computing Systems project - Reservation system
 * @author Henrique Realinho - nr 50415
 *
 */
public class MainApplication extends Application
{
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> resources = new HashSet<Class<?>>();

	public MainApplication() { // add resources to resources set
		resources.add(ControlResource.class);
		resources.add(MediaResource.class);
		resources.add(CalendarResource.class);
		resources.add(EntityResource.class);
		resources.add(ForumResource.class);
	}

	@Override
	public Set<Class<?>> getClasses() {
		return resources;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
